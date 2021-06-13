package dev.zihasz.zware.ui.clickgui;

import dev.zihasz.zware.ui.colors.Dracula;
import dev.zihasz.zware.utils.render.TextRenderer;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public class GuiClick extends GuiScreen {

	public GuiClick() {

	}

	@Override
	public void initGui() {

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		TextRenderer.drawStringWithShadow("Made by zihasz", 2, 2, Dracula.ansiBrightWhite);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
	}
}
