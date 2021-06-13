package dev.zihasz.zware.event.events;

import dev.zihasz.zware.event.Event;
import dev.zihasz.zware.event.EventState;

public class UpdateWalkingPlayerEvent extends Event {

	public UpdateWalkingPlayerEvent(EventState state) {
		super(state);
	}

}
