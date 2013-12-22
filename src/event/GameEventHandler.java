package event;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;

public class GameEventHandler implements Runnable {
	private static final GameEventHandler INSTANCE = new GameEventHandler();

	private final Map<Integer, EventObject> queue;

	private GameEventHandler() {
		queue = new HashMap<Integer, EventObject>();
	}

	public static GameEventHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * Adds an event to the queue for processing.
	 * 
	 */
	public void dispatchEvent(EventObject e) {
		synchronized (queue) {
			for (int i = 0; i < queue.size(); i++) {
				if (!queue.containsKey(i)) {
					queue.put(i, e);
					queue.notifyAll();
					return;
				}
			}
			queue.put(queue.size(), e);
			queue.notifyAll();
		}
	}

	/**
	 * Dispatches the given event. Calling this avoids the use of the event
	 * queue.
	 */
	public void processEvent(final EventObject event) {
		if (event instanceof LocalGameEvent) {
			((LocalGameEvent) event).process();
		} else if (event instanceof RequestGameEvent) {
			((RequestGameEvent) event).sendEvent();
		} else {
			System.err.println("We were asked to process a weird event?");
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				EventObject event = null;
				synchronized (queue) {
					while (queue.isEmpty()) {
						try {
							queue.wait();
						} catch (final Exception e) {
							System.out.println("Event Queue: " + e.toString());
						}
					}
					int i = 0;
					boolean found = false;
					while (!found) {
						if (queue.containsKey(i)) {
							event = queue.remove(i);
							found = true;
						} else {
							i++;
						}
					}
				}
				try {
					processEvent(event);
				} catch (final Throwable e) {
					e.printStackTrace();
				}
				synchronized (event) {
					event.notifyAll();
				}
			} catch (final Exception e) {
				System.err.println("Event Queue: " + e.toString());
				e.printStackTrace();
			}
		}
	}
}
