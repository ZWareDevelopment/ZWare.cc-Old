package dev.zihasz.zware.features.hud;

import dev.zihasz.zware.features.Feature;
import net.minecraftforge.common.MinecraftForge;

public abstract class HudElement extends Feature {

	private boolean enabled;
	public int x, y;

	public HudElement(String name, String description, int defaultX, int defaultY) {
		super(name, description);
		this.x = defaultX;
		this.y = defaultY;
	}

	public void enable() {
		this.enabled = true;
		this.onEnable();
		MinecraftForge.EVENT_BUS.register(this);
	}
	public void disable() {
		this.enabled = false;
		this.onDisable();
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	public void toggle() {
		if (enabled) disable();
		else enable();
	}

	public void draw() {}

	public void onEnable() {}
	public void onDisable() {}

	public boolean isEnabled() { return enabled; }
	public void setEnabled(boolean enabled) {
		if (enabled) disable();
		else enable();
	}

}
