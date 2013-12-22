package event.external;

import client.GameClient;
import client.GameClient.GameState;
import event.GameEventHandler;
import event.RequestGameEvent;
import event.local.HighlightStartTiles;

public class NewCharacterRequest extends RequestGameEvent {

	private static final long serialVersionUID = 7225469024856332551L;

	public NewCharacterRequest() {
		super();
		setType(Type.UNICAST);
	}
	
	@Override
	public void process() {
		GameClient.getInstance().setGameState(GameState.ADDING_CHARACTER);
		GameEventHandler.getInstance().dispatchEvent(new HighlightStartTiles());
	}

	@Override
	public boolean verify() {
		return true;
	}

}
