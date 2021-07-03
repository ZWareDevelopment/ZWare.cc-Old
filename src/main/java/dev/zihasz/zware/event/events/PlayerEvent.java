package dev.zihasz.zware.event.events;

import dev.zihasz.zware.event.Event;
import dev.zihasz.zware.event.EventState;
import net.minecraft.entity.MoverType;

public abstract class PlayerEvent extends Event {

	public PlayerEvent(EventState state) {
		super(state);
	}

	public static class Move extends PlayerEvent {

		public MoverType type;
		public double x;
		public double y;
		public double z;

		public Move(EventState state, MoverType type, double x, double y, double z) {
			super(state);
			this.type = type;
			this.x = x;
			this.y = y;
			this.z = z;
		}
	}

	public abstract static class Update extends PlayerEvent {

		public Update(EventState state) {
			super(state);
		}

		public static class Walking extends Update {

			public Walking(EventState state) {
				super(state);
			}

		}

	}

}
