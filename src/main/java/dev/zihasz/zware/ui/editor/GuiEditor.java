package dev.zihasz.zware.ui.editor;

import dev.zihasz.zware.features.hud.HudElement;
import dev.zihasz.zware.manager.HudManager;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiEditor extends GuiScreen {

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		HudManager.getElements().forEach(HudElement::draw);
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(null);
			onGuiClosed();
		}
	}

}
