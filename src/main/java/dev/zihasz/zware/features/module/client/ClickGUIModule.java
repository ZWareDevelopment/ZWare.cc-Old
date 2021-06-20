package dev.zihasz.zware.features.module.client;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.ui.rewrite.GuiClick;
import org.lwjgl.input.Keyboard;

public class ClickGUIModule extends Module {
	public ClickGUIModule() {
		super("GuiClick", "Enables the GuiClick.", Category.CLIENT, Keyboard.KEY_RCONTROL);
	}

	public static Setting<Integer> animationSpeed = new Setting<>("", "", 0, 0, 0);
	public static Setting<Integer> scrollingSpeed = new Setting<>("", "", 0, 0, 0);

	@Override
	public void onEnable() {

		mc.displayGuiScreen(new GuiClick());
		this.disable();

	}

}
