package dev.zihasz.zware.features.module.misc;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.features.setting.SubSetting;
import net.minecraft.util.text.TextFormatting;

public class Notifications extends Module {

	public static Setting<Mode> mode = new Setting<>("Mode", "A way to show notifications", Mode.Chat);

	public static Setting<Boolean> module = new Setting<>("Modules", "Show notifications for module toggles.", true);
	public static SubSetting<TextFormatting> moduleEnableFormat = new SubSetting<>(module, "Enable Color", "Chat color for enabled modules", TextFormatting.GREEN);
	public static SubSetting<TextFormatting> moduleDisableFormat = new SubSetting<>(module, "Disable Color", "Chat color for disabled modules", TextFormatting.RED);

	public Notifications() {
		super("Notifications", "Show different notifications", Category.CLIENT);
	}

	public static enum Mode {
		Chat,
		HUD
	}

}
