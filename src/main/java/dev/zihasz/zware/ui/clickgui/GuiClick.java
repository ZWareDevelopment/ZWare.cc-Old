package dev.zihasz.zware.ui.clickgui;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.ui.clickgui.component.panel.Frame;
import dev.zihasz.zware.ui.clickgui.component.panel.Panel;
import dev.zihasz.zware.utils.render.ColorScheme;
import dev.zihasz.zware.utils.render.TextRenderer;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GuiClick extends GuiScreen {

	private final ColorScheme defaultScheme = new ColorScheme(new Color(0xff98ff98), new Color(0xff3f3f3f), new Color(0xfff3f3f3));
	private List<Panel> panels = new ArrayList<>();

	public GuiClick() {
		int offX = 0;
		for (Category category : Arrays.stream(Category.values()).sorted(Comparator.comparing(Category::toString)).collect(Collectors.toList())) {
			panels.add(new Frame(category, 40 + offX, 40, 100, 20, defaultScheme));
			offX += 110;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		TextRenderer.drawString("Made by Zihasz", 2, 2, new Color(0xfff3f3f3), true);
		panels.forEach(panel -> panel.draw(mouseX, mouseY));
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		panels.forEach(panel -> panel.click(mouseX, mouseY, mouseButton));
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		panels.forEach(panel -> panel.press(keyCode, typedChar));
	}
}
