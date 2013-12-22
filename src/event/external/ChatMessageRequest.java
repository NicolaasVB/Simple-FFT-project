package event.external;

import client.ChatWindow;
import client.GameClient;
import event.RequestGameEvent;

public class ChatMessageRequest extends RequestGameEvent {

	private static final long serialVersionUID = -7352158868361612418L;
	private String sender, message;
	private int team;
	
	public String getMessage() {
		return message;
	}
	
	public ChatMessageRequest(String message) {
		setType(Type.MULTICAST_GAME);
		this.message = message;
		team = GameClient.getInstance().getTeam();
	}
	
	@Override
	public void process() {
		ChatWindow.getInstance().appendMessage(sender, message, GameClient.getInstance().getTeam() == team);
	}

	@Override
	public boolean verify() {
		sender = this.getClientHandler().getNickname();
		return true;
	}

}
