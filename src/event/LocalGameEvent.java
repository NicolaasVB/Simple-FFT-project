package event;


import java.util.EventObject;

/**
 * A game event that we can process locally, so there's no need to notify the
 * server of it. (examples like selecting a different tile)
 * 
 * @author Nicolaas
 * 
 */
public abstract class LocalGameEvent extends EventObject {

	private static final long serialVersionUID = -4460283810788310480L;
	private static final Object SOURCE = new Object();

	public LocalGameEvent() {
		super(LocalGameEvent.SOURCE);
	}

	public abstract void process();

}
