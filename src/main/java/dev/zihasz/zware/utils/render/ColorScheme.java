package dev.zihasz.zware.utils.render;

import java.awt.*;

public class ColorScheme {

	public Color foreground, background, font;

	public ColorScheme(Color foreground, Color background, Color font) {
		this.foreground = foreground;
		this.background = background;
		this.font = font;
	}

	public ColorScheme() {
		this(new Color(0xff98ff98), new Color(0xff3f3f3f), new Color(0xfff3f3f3));
	}

	public Color getForeground() { return foreground; }
	public Color getBackground() { return background; }
	public Color getFont() { return font; }

	public void setForeground(Color foreground) { this.foreground = foreground; }
	public void setBackground(Color background) { this.background = background; }
	public void setFont(Color font) { this.font = font; }
}
