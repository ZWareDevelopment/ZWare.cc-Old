package dev.zihasz.zware.features.module.movement;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", "Automatically sprints.", Category.MOVEMENT);
	}

	@Override
	public void onDisable() {
		mc.player.setSprinting(mc.gameSettings.keyBindSprint.isKeyDown());
	}

	@Override
	public void onUpdate() {
		mc.player.setSprinting(mc.player.getFoodStats().getFoodLevel() > 6);
	}
}
