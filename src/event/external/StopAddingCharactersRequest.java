package event.external;

import client.GameClient;
import client.GameClient.GameState;
import event.RequestGameEvent;

public class StopAddingCharactersRequest extends RequestGameEvent {

	private static final long serialVersionUID = -1224504557178804L;

	public StopAddingCharactersRequest() {
		setType(Type.UNICAST);
	}
	
	@Override
	public void process() {
		GameClient.getInstance().setGameState(GameState.IDLE);
		GameClient.getInstance().getMap().clearHighlights();
	}

	@Override
	public boolean verify() {
		return true;
	}

}
