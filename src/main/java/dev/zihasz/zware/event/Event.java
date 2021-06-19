package dev.zihasz.zware.event;

public abstract class Event extends net.minecraftforge.fml.common.eventhandler.Event {

	private final EventState state;

	public Event(EventState state) {
		this.state = state;
	}

	public EventState getState() {
		return state;
	}

	public void cancel() {
		setCanceled(true);
	}

	@Override
	public boolean isCancelable() {
		return true;
	}
}
