package event.external;

import javax.swing.JOptionPane;

import server.Server;
import client.GameClient;
import client.Lobby;
import client.WaitForOtherPlayerWindow;
import event.RequestGameEvent;
import game.Game;

public class JoinGameRequest extends RequestGameEvent {
	private static final long serialVersionUID = 7268628291962917541L;
	int gameId;
	boolean success = false;
	String error;
	
	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public JoinGameRequest(int gameId) {
		this.gameId = gameId;
		setType(Type.UNICAST);
	}
	
	@Override
	public void process() {
		if (success) {
			if (!GameClient.getInstance().isVisible()) {
				Lobby.getInstance().setVisible(false);
				WaitForOtherPlayerWindow.getInstance().setVisible(true);
				WaitForOtherPlayerWindow.getInstance().setGameId(gameId);
			}
		} else {
			JOptionPane.showMessageDialog(null, error);
			Lobby.getInstance().enableDisableControls(true);
			Lobby.getInstance().updateGames();
		}
	}

	@Override
	public boolean verify() {
		for (Game game : Server.getGames()) {
			if (game.getId() == gameId) {
				if (game.getPlayers().size() >= 2) {
					error = "This game is already running.";
					success = false;
					return true; // if we set to false, the server will never send the object back, so the client will keep waiting
				} else {
					getClientHandler().setGame(game);
					game.getPlayers().add(getClientHandler());
					if(game.getPlayers().size() == 2)
						game.nextRound();
					success = true;
					return true;
				}
			}
		}
		error = "The game with id " + gameId + "wasn't found.";
		success = false;
		return true; // if we set to false, the server will never send the object back, so the client will keep waiting
	}

}
