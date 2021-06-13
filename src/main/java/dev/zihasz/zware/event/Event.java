package dev.zihasz.zware.event;

public abstract class Event {

	private final EventState state;
	private boolean canceled = false;

	public Event(EventState state) {
		this.state = state;
	}

	public EventState getState() {
		return state;
	}

	public void cancel() {
		setCanceled(true);
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
