package event.external;

import Application.CharacterConfiguration;
import client.Lobby;
import client.LoginWindow;
import event.RequestGameEvent;

public class EstablishConnectionRequest extends RequestGameEvent {
	private static final long serialVersionUID = -6590977867655775159L;
	private String nickname;
	private CharacterConfiguration conf;
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public EstablishConnectionRequest(String nickname) {
		this.setNickname(nickname);
		setType(Type.UNICAST);
	}
	
	@Override
	public void process() {
		LoginWindow.getInstance().setVisible(false);
		Lobby.getInstance().setVisible(true);
		CharacterConfiguration.getInstance().applySettings(conf);
	}

	@Override
	public boolean verify() {
		conf = CharacterConfiguration.getInstance();
		getClientHandler().setNickname(nickname);
		return true;
	}

}
