package event.external;

import javax.swing.JOptionPane;

import server.Server;
import client.ChatWindow;
import client.GameClient;
import client.Lobby;
import client.WaitForOtherPlayerWindow;
import event.RequestGameEvent;

public class StopGameRequest extends RequestGameEvent {
	private static final long serialVersionUID = 3823915063968143187L;
	int gameId;
	
	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public StopGameRequest(int gameId) {
		super();
		this.gameId = gameId;
	}

	@Override
	public void process() {
		if (GameClient.getInstance().getGameId() == gameId || WaitForOtherPlayerWindow.getInstance().getGameId() == gameId) {
			if (WaitForOtherPlayerWindow.getInstance().isVisible()) {
				WaitForOtherPlayerWindow.getInstance().setVisible(false);
			} else {
				JOptionPane.showMessageDialog(null, "Your opponent stopped the game, you won!", "You won!!", JOptionPane.INFORMATION_MESSAGE);
				GameClient.getInstance().setVisible(false);
				ChatWindow.getInstance().setVisible(false);
			}
			
			Lobby.getInstance().setVisible(true);
			Lobby.getInstance().enableDisableControls(true);

			ServerCommunicator.getInstance().sendObject(new GetGamesRequest());
		}
	}

	@Override
	public boolean verify() {
		if (getClientHandler().getGame() != null && getGameId() == getClientHandler().getGame().getId()) {
			if (getClientHandler().getGame().getPlayers().size() == 2) {
				Server.getGames().remove(getClientHandler().getGame());
			} else {
				getClientHandler().getGame().getPlayers().remove(getClientHandler());
			}
			return true;
		} else {
			// Stoppen met luisteren naar requests van speler
			System.out.println("" + this + " tried to cheat and got kicked");
			getClientHandler().kill();
			return false;
		}
	}

}
