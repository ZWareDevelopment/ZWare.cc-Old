package dev.zihasz.zware.ui.clickgui.component.button;

import dev.zihasz.zware.ui.clickgui.component.Component;
import dev.zihasz.zware.utils.render.ColorScheme;

public abstract class Button implements Component {

	public int x, y, width, height;
	public ColorScheme colorScheme;
	public int buttonOffset = 0;
	public Component parent;

	public Button(Component parent, int x, int y, int width, int height, ColorScheme colorScheme) {
		this.parent = parent;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.colorScheme = colorScheme;
	}

	public abstract void draw(int x, int y);
	public abstract void click(int x, int y, int button);
	public abstract void press(int code, char character);

	protected boolean hovered(int x, int y) { return (this.x <= x && x <= this.x + this.width) && (this.y <= y && y <= this.y + this.height); }

}
