package dev.zihasz.zware.ui.clickgui.component.panel;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.manager.ModuleManager;
import dev.zihasz.zware.ui.clickgui.component.button.Button;
import dev.zihasz.zware.ui.clickgui.component.button.ModuleComponent;
import dev.zihasz.zware.utils.render.ColorScheme;
import dev.zihasz.zware.utils.render.Renderer2D;
import dev.zihasz.zware.utils.render.TextRenderer;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Frame extends Panel {

	private Category category;
	private List<Button> buttons = new ArrayList<>();
	private boolean expanded = true;

	public Frame(Category category, int x, int y, int width, int height, ColorScheme colorScheme) {
		super(x, y, width, height, colorScheme);
		this.category = category;

		int offsetY = height;
		for (Module module : ModuleManager.getModules(category).stream().sorted().collect(Collectors.toList())) {
			buttons.add(new ModuleComponent(x, y + offsetY, width, height, colorScheme, module));
			offsetY += height;
		}
	}

	@Override
	public void draw(int x, int y) {
		Renderer2D.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, this.colorScheme.foreground);
		TextRenderer.drawCenteredString(this.getCategoryName(), this.x, this.y, this.width, this.height, this.colorScheme.font, true);

		if (expanded) buttons.forEach(b -> b.draw(x, y));
	}

	@Override
	public void click(int x, int y, int button) {
		if (expanded)
			buttons.forEach(b -> b.click(x, y, button));

		if (hovered(x, y)) {
			switch (button) {
				case 0:
					// TODO: Handle dragging (deez nuts across your face)
					break;
				case 1:
					expanded = !expanded;
					break;
			}
		}
	}

	@Override
	public void press(int code, char character) {
		if (expanded) buttons.forEach(b -> b.press(code, character));
	}

	public Category getCategory() { return category; }
	public String getCategoryName() { return StringUtils.capitalize(category.toString().toLowerCase()); }

}
