package dev.zihasz.zware.features.module;

import dev.zihasz.zware.ZWare;
import dev.zihasz.zware.event.EventState;
import dev.zihasz.zware.event.events.ModuleEvent;
import dev.zihasz.zware.features.Feature;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

public abstract class Module extends Feature {

	private Category category;

	private int bind;
	private boolean enabled;
	private boolean visible;

	/**
	 * The main constructor of the {@link Module} class.
	 * @param name the name of the module.
	 * @param description the description of the modules
	 * @param category the {@link Category} for the module.
	 * @param bind the bind of the module
	 * @param visible is the module visible
	 */
	public Module(String name, String description, Category category, int bind, boolean visible) {
		super(name, description);
		this.category = category;
		this.bind = bind;
		this.visible = visible;
	}
	public Module(String name, String description, Category category, int bind) { this(name, description, category, bind, true); }
	public Module(String name, String description, Category category)           { this(name, description, category, Keyboard.KEY_NONE); }
	public Module(String name, Category category)                               { this(name, "", category); }

	public void enable() {
		if (nullCheck()) return;
		this.enabled = true;
		onEnable();
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.post(new ModuleEvent.Enable(EventState.POST, this));
	}
	public void disable() {
		if (nullCheck()) return;
		this.enabled = false;
		onDisable();
		MinecraftForge.EVENT_BUS.post(new ModuleEvent.Enable(EventState.POST, this));
		MinecraftForge.EVENT_BUS.unregister(this);
	}
	public void toggle() {
		if (enabled) disable();
		else enable();
	}

	public String getInfo() { return ""; }

	public void onUpdate() {}
	public void onRender2D() {}
	public void onRender3D(RenderWorldLastEvent event) {}

	public void onEnable() {}
	public void onDisable() {}

	public Category getCategory() { return category; }
	public int getBind() { return bind; }
	public boolean isEnabled() { return enabled; }
	public boolean isVisible() { return visible; }

	public void setCategory(Category category) { this.category = category; }
	public void setBind(int bind) { this.bind = bind; }

	public void setEnabled(boolean enabled) {
		if (enabled) {
			disable();
		} else {
			enable();
		}
	}
	public void setVisible(boolean visible) { this.visible = visible; }

}
