package event.external;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import client.ChatWindow;
import client.GameClient;
import client.Lobby;
import client.WaitForOtherPlayerWindow;
import event.RequestGameEvent;

public class GameFinishedRequest extends RequestGameEvent {
	private static final long serialVersionUID = 2965191201224324148L;
	private int winner;
	
	public GameFinishedRequest(int winner) {
		super();
		setType(Type.MULTICAST_GAME);
		this.winner = winner;
	}

	@Override
	public void process() {
		Timer t = new Timer();
		t.schedule(new TimerTask(){

			@Override
			public void run() {
				
				if (GameClient.getInstance().getTeam() == winner)
					JOptionPane.showMessageDialog(null, "Congratulations!!! You won the game!", "You won!!", JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(null, "You have lost the game, try again...", "You lost", JOptionPane.INFORMATION_MESSAGE);

				GameClient.getInstance().setTeam(-1);
				GameClient.getInstance().setGameId(-1);
				GameClient.getInstance().setCharacterId(-1);
				WaitForOtherPlayerWindow.getInstance().setGameId(-1);
				GameClient.getInstance().setVisible(false);
				ChatWindow.getInstance().setVisible(false);
				ChatWindow.getInstance().clear();
				Lobby.getInstance().setVisible(true);
				Lobby.getInstance().enableDisableControls(true);
			}
			
		}, 2000);
		
	}

	@Override
	public boolean verify() {
		return false;
	}

}
