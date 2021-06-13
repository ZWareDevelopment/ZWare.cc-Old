package dev.zihasz.zware.features.module.client;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.ui.clickgui.GuiClick;
import org.lwjgl.input.Keyboard;

public class ClickGUIModule extends Module {

	public ClickGUIModule() {
		super("ClickGUI", "Enables the ClickGUI.", Category.CLIENT, Keyboard.KEY_RCONTROL);
	}

	@Override
	public void onEnable() {
		if (!(mc.currentScreen instanceof GuiClick)) {
			mc.displayGuiScreen(new GuiClick());
		}
		disable();
	}

}
