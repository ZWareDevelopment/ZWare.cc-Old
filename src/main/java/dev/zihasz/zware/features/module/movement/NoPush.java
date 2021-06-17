package dev.zihasz.zware.features.module.movement;

import dev.zihasz.zware.event.events.EntityCollisionEvent;
import dev.zihasz.zware.event.events.WaterPushEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoPush extends Module {

	public NoPush() {
		super("NoPush", "Makes you unpushable.", Category.MOVEMENT);
	}

	@SubscribeEvent
	public void onWaterPush(WaterPushEvent event) {
		event.cancel();
	}

	@SubscribeEvent
	public void onEntityCollision(EntityCollisionEvent event) {
		event.cancel();
	}

}
