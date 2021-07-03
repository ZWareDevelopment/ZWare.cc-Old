package dev.zihasz.zware.features.module.misc;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import net.minecraft.world.GameType;

public class ForceSurvival extends Module {

	public ForceSurvival() {
		super("ForceSurvival", "Forces survival gamemode", Category.MISC);
	}

	@Override
	public void onUpdate() {
		mc.playerController.setGameType(GameType.SURVIVAL);
		mc.player.noClip = false;
	}

}
