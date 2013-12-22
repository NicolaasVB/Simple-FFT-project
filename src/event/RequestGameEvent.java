package event;



import java.io.Serializable;
import java.util.EventObject;

import server.ClientHandler;
import event.external.ServerCommunicator;



/**
 * A game event that we have to send to the server for verification before
 * processing.
 * 
 * @author Nicolaas
 * 
 */
public abstract class RequestGameEvent extends EventObject implements Serializable {

	private static final long serialVersionUID = -474394674616888764L;

	public enum Type {UNICAST, MULTICAST_GAME, BROADCAST}
	
	private static final Object SOURCE = new Object();
	private Type type = Type.BROADCAST;
	private transient ClientHandler sender;

	public ClientHandler getClientHandler() 
	{
		return sender;
	}

	public void setClientHandler(ClientHandler sender) {
		this.sender = sender;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public RequestGameEvent() {
		super(RequestGameEvent.SOURCE);
	}

	public void sendEvent() {
		System.out.println("sending event");
		ServerCommunicator.getInstance().sendObject(this);
		System.out.println("event sent.");
	}

	/**
	 * for the client. Once the server verified a GameEvent, it'll send this
	 * object back, so we can process it.
	 */
	public abstract void process();

	/**
	 * for the server. It'll verify if this GameEvent is legit or not.
	 */
	public abstract boolean verify();

}
