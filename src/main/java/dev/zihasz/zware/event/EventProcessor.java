package dev.zihasz.zware.event;

import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.manager.ModuleManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class EventProcessor {

	public EventProcessor() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::onUpdate);
	}

	@SubscribeEvent
	public void onRender(TickEvent.RenderTickEvent event) {
		ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::onRender2D);
	}

	@SubscribeEvent
	public void onWorldRender(RenderWorldLastEvent event) {
		ModuleManager.getModules().stream().filter(Module::isEnabled).forEach(module -> module.onRender3D(event));
	}

	@SubscribeEvent
	public void onKey(InputEvent.KeyInputEvent event) {
		if (Keyboard.getEventKeyState()) {
			ModuleManager.getModules().stream()
					.filter(module -> module.getBind() == Keyboard.getEventKey())
					.forEach(Module::toggle);
		}
	}

}