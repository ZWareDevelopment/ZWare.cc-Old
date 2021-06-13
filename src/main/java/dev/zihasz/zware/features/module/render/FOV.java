package dev.zihasz.zware.features.module.render;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FOV extends Module {

	public FOV() {
		super("FOV", "Modify your FOV.", Category.RENDER);
	}

	private final Setting<Integer> fov = new Setting<>("FOV", "The normal FOV value for the camera.", 130, -360, 360);
	private final Setting<Integer> itemFov = new Setting<>("ItemFOV", "The FOV for items", 130, -360, 360);

	@Override
	public void onUpdate() {
		mc.gameSettings.fovSetting = fov.getValue();
	}

	@SubscribeEvent
	public void onFOVModifier(EntityViewRenderEvent.FOVModifier event) {
		event.setFOV(itemFov.getValue());
	}

}
