package dev.zihasz.zware.ui.rewrite;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.ui.base.GuiBase;
import dev.zihasz.zware.ui.colors.Dracula;
import dev.zihasz.zware.ui.rewrite.component.Component;
import dev.zihasz.zware.ui.rewrite.component.panel.Frame;
import dev.zihasz.zware.utils.render.ColorScheme;
import dev.zihasz.zware.utils.render.TextRenderer;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class GuiClick extends GuiBase {
	public static final ColorScheme defaultScheme = new ColorScheme(new Color(0xff98ff98), new Color(0xff3f3f3f), new Color(0xfff3f3f3));
	public static final int X_OFF_DEFAULT = 20;

	public static Component hovered = null;
	private final ArrayList<dev.zihasz.zware.ui.rewrite.component.panel.Frame> frames;

	public GuiClick() {
		this.frames = new ArrayList<>();

		int x = X_OFF_DEFAULT;
		for (Category c : Category.values()) {
			frames.add(new dev.zihasz.zware.ui.rewrite.component.panel.Frame(x, 20, c));
			x += dev.zihasz.zware.ui.rewrite.component.panel.Frame.FRAME_WIDTH + X_OFF_DEFAULT;
		}
	}

	@Override
	public void initGui() {

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		hovered = null;
		frames.forEach(e -> e.update(mouseX, mouseY));
		frames.forEach(e -> e.render(mouseX, mouseY));

		if (hovered != null) {
			TextRenderer.drawString(hovered.getDescription(), mouseX + 3, mouseY, Dracula.purple);
		}
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		frames.forEach(e -> e.mouseClicked(mouseX, mouseY, mouseButton));
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(null);
			onGuiClosed();
		}
		frames.forEach(e -> e.keyTyped(typedChar, keyCode));
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		frames.forEach(e -> e.mouseReleased(mouseX, mouseY, state));
	}

	@Override
	public void onGuiClosed() {
		frames.forEach(dev.zihasz.zware.ui.rewrite.component.panel.Frame::guiClosed);
	}

	public ArrayList<Frame> getFrames() {
		return frames;
	}
}
