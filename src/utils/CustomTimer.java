package utils;



public class CustomTimer {

	private long end;
	private final long period;

	/**
	 * Instantiates a new Timer with a given time period in milliseconds.
	 * 
	 * @param period
	 *            Time period in milliseconds.
	 */
	public CustomTimer(final long period) {
		this.period = period;
		end = System.currentTimeMillis() + period;
	}

	/**
	 * Returns the number of milliseconds remaining until the timer is up.
	 * 
	 * @return The remaining time in milliseconds.
	 */
	public long getRemaining() {
		if (isRunning()) {
			return end - System.currentTimeMillis();
		}
		return 0;
	}

	public double getRemainingPecentage() {
		if (isRunning() && period > 0) {
			return (double) getRemaining() / period;
		}
		return 0;
	}

	/**
	 * Returns true if this timer's time period has not yet elapsed.
	 * 
	 * @return true if the time period has not yet passed.
	 */
	public boolean isRunning() {
		return System.currentTimeMillis() < end;
	}

	/**
	 * Restarts this timer using its period.
	 */
	public void reset() {
		end = System.currentTimeMillis() + period;
	}
}