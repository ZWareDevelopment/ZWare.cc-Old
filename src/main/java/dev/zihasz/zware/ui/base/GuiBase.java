package dev.zihasz.zware.ui.base;

import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

public abstract class GuiBase extends GuiScreen {
	public GuiBase() {

	}

	@Override
	public abstract void initGui();

	@Override
	public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);

	@Override
	public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;

	@Override
	public abstract void mouseReleased(int mouseX, int mouseY, int state);

	@Override
	public abstract void keyTyped(char typedChar, int keyCode) throws IOException;

	@Override
	public abstract void onGuiClosed();

	@Override
	public final boolean doesGuiPauseGame() { return false; }
}
