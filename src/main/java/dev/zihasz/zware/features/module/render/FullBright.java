package dev.zihasz.zware.features.module.render;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;

public class FullBright extends Module {

	public FullBright() {
		super("FullBright", "Brightens your game.", Category.RENDER);
	}

	private final Setting<Mode> mode = new Setting<>("DropDown", "Fuck gamma.", Mode.POTION);

	private float oldGamma = 0;

	@Override
	public void onEnable() {
		oldGamma = mc.gameSettings.gammaSetting;
		if (mode.getValue() == Mode.GAMMA) mc.gameSettings.gammaSetting = 100f;
		else if (mode.getValue() == Mode.POTION) mc.player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 999));
	}

	@Override
	public void onDisable() {
		if (mode.getValue() == Mode.GAMMA) mc.gameSettings.gammaSetting = oldGamma;
		else if (mode.getValue() == Mode.POTION) mc.player.removePotionEffect(MobEffects.NIGHT_VISION);
	}

	private enum Mode {
		POTION,
		GAMMA
	}
}
