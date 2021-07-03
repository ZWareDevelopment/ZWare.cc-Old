package dev.zihasz.zware.features.module.misc;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;

public class PingBypass extends Module {

	public PingBypass() {
		super("PingBypass", ":flushed:", Category.CLIENT);
	}

	private final Setting<Region> region = new Setting<>("Region", "What region is the server in that you want to play?", Region.US);
	private final Setting<Boolean> detect = new Setting<>("Detect", "Automagically detects what region is the server in.", false);

	private enum Region {
		EU,
		UK,
		US,
		SA,
		AS,
	}

}
