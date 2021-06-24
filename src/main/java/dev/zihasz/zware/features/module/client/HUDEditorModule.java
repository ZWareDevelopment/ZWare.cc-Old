package dev.zihasz.zware.features.module.client;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.ui.editor.GuiEditor;
import org.lwjgl.input.Keyboard;

public class HUDEditorModule extends Module {

	public HUDEditorModule() {
		super("Editor", "Allows you to edit the HUD.", Category.CLIENT, Keyboard.KEY_GRAVE);
	}

	@Override
	public void onEnable() {
		if (mc.currentScreen == null)
			mc.displayGuiScreen(new GuiEditor());
		disable();
	}
}
