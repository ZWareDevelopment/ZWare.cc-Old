package dev.zihasz.zware.event.events;

import dev.zihasz.zware.event.Event;
import dev.zihasz.zware.event.EventState;
import dev.zihasz.zware.features.module.Module;

public abstract class ModuleEvent extends Event {

	private Module module;

	public ModuleEvent(EventState era, Module module) {
		super(era);
		this.module = module;
	}

	public Module getModule() {
		return module;
	}

	public static class Enable extends ModuleEvent {
		public Enable(EventState era, Module module) {
			super(era, module);
		}
	}

	public static class Disable extends ModuleEvent {
		public Disable(EventState era, Module module) {
			super(era, module);
		}
	}

}
