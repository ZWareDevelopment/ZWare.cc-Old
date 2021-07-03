package dev.zihasz.zware.event.events;

import dev.zihasz.zware.event.Event;
import dev.zihasz.zware.event.EventState;

public abstract class PushEvent extends Event {

	public PushEvent(EventState state) {
		super(state);
	}

	public static class Entity extends PushEvent {
		public Entity(EventState state) {
			super(state);
		}
	}

	public static class Liquid extends PushEvent {
		public Liquid(EventState state) {
			super(state);
		}
	}

	public static class Block extends PushEvent {
		public Block(EventState state) {
			super(state);
		}
	}

}
