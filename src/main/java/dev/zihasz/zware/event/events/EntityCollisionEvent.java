package dev.zihasz.zware.event.events;

import dev.zihasz.zware.event.Event;
import dev.zihasz.zware.event.EventState;

public class EntityCollisionEvent extends Event {

	public EntityCollisionEvent(EventState era, int stage) {
		super(era);
	}

	public EntityCollisionEvent(EventState era) {
		super(era);
	}

}
