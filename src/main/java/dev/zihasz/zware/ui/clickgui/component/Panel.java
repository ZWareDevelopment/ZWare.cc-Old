package dev.zihasz.zware.ui.clickgui.component;

import dev.zihasz.zware.features.module.Category;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Panel implements Component {

	public Category category;
	public Rectangle button;
	public Color foreground, background, font;

	private List<?> buttons = new ArrayList<>();

	public Panel(Category category, Rectangle button, Color foreground, Color background, Color font) {
		this.category = category;
		this.button = button;
		this.foreground = foreground;
		this.background = background;
		this.font = font;
	}

	@Override
	public void draw() {

	}

	@Override
	public void click(int mX, int mY, int button) {

	}

	@Override
	public void press(int button, char keyChar) {

	}

}
