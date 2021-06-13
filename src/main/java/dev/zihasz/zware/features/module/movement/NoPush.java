package dev.zihasz.zware.features.module.movement;

import dev.xdark.ssbus.Listener;
import dev.zihasz.zware.event.events.EntityCollisionEvent;
import dev.zihasz.zware.event.events.WaterPushEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;

public class NoPush extends Module {

	public NoPush() {
		super("NoPush", "Makes you unpushable.", Category.MOVEMENT);
	}

	@Listener
	public void onWaterPush(WaterPushEvent event) {
		event.cancel();
	}

	@Listener
	public void onEntityCollision(EntityCollisionEvent event) {
		event.cancel();
	}

}
