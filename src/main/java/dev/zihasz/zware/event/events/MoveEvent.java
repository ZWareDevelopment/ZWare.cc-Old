package dev.zihasz.zware.event.events;

import dev.zihasz.zware.event.Event;
import dev.zihasz.zware.event.EventState;
import net.minecraft.entity.MoverType;

public class MoveEvent extends Event {

	public MoverType type;
	public double x;
	public double y;
	public double z;

	public MoveEvent(EventState state, MoverType type, double x, double y, double z) {
		super(state);
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
