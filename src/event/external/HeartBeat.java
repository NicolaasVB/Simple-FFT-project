package event.external;

import event.RequestGameEvent;

public class HeartBeat extends RequestGameEvent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9069452201548040292L;

	public HeartBeat()
	{
		super();
		setType(Type.UNICAST);
	}
	
	@Override
	public void process() 
	{
		ServerCommunicator.getInstance().heartBeatReceived();
	}

	@Override
	public boolean verify() {
		getClientHandler().heartBeatReceived();
		
		// Will always be returned to client
		return true;
	}

}
