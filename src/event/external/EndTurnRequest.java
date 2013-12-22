package event.external;

import event.RequestGameEvent;

public class EndTurnRequest extends RequestGameEvent {
	
	private static final long serialVersionUID = -940914978723032102L;

	public EndTurnRequest() {
		super();	
		setType(Type.MULTICAST_GAME);
	}

	@Override
	public void process() {
		// nothing to do

	}

	@Override
	public boolean verify() {
		return getClientHandler().getGame().endTurn();
	}

}
