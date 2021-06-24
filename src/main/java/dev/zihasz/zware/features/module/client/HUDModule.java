package dev.zihasz.zware.features.module.client;

import dev.zihasz.zware.features.hud.HudElement;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.manager.HudManager;

public class HUDModule extends Module {

	public HUDModule() {
		super("HUD", "Makes the hud draw", Category.CLIENT);
	}

	@Override
	public void onRender2D() {
		HudManager.getElements().stream()
				.filter(HudElement::isEnabled)
				.forEach(HudElement::draw);
	}
}
