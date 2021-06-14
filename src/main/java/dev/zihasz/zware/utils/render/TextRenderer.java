package dev.zihasz.zware.utils.render;

import dev.zihasz.zware.features.module.client.FontModule;
import dev.zihasz.zware.utils.Util;
import dev.zihasz.zware.utils.render.font.CFontRenderer;

import java.awt.*;

public class TextRenderer implements Renderer, Util {

	public static Font rubik = new Font("assets/zware/fonts/Rubik-Regular.ttf", FontModule.style.getValue(), FontModule.size.getValue());
	public static CFontRenderer renderer = new CFontRenderer(rubik, FontModule.antiAlias.getValue(), true);

	public static void drawString(String text, float x, float y, Color color, boolean shadow) {
		if (FontModule.custom.getValue()) {
			renderer.drawString(text, x, y, color.getRGB(), shadow);
		} else {
			mc.fontRenderer.drawString(text, x, y, color.getRGB(), shadow);
		}
	}
	public static void drawString(String text, float x, float y, Color color) {
		drawString(text, x, y, color, false);
	}
	public static void drawStringWithShadow(String text, float x, float y, Color color) {
		drawString(text, x, y, color, true);
	}

	public static void drawCenteredString(String text, int x, int y, int width, int height, Color color, boolean shadow) {
		TextRenderer.drawString(text, x + (width / 2) - (TextRenderer.getStringWidth(text) / 2), y + (height / 2) - (TextRenderer.getFontHeight() / 2), color, shadow);
	}

	public static void drawStringRight(String text, float right, float y, Color color, boolean shadow) {
		if (FontModule.custom.getValue()) {
			renderer.drawString(text, right - renderer.getStringWidth(text), y, color.getRGB(), shadow);
		} else {
			mc.fontRenderer.drawString(text, right - mc.fontRenderer.getStringWidth(text), y, color.getRGB(), shadow);
		}
	}
	public static void drawStringRight(String text, float right, float y, Color color) {
		drawStringRight(text, right, y, color, false);
	}
	public static void drawStringRightWithShadow(String text, float right, float y, Color color) {
		drawStringRight(text, right, y, color, true);
	}

	public static float getStringWidth(String text) {
		if (FontModule.custom.getValue()) {
			return renderer.getStringWidth(text);
		} else {
			return mc.fontRenderer.getStringWidth(text);
		}
	}
	public static float getStringHeight(String text) {
		if (FontModule.custom.getValue()) {
			return renderer.getStringHeight(text);
		} else {
			return mc.fontRenderer.FONT_HEIGHT;
		}
	}

	public static float getFontHeight() {
		if (FontModule.custom.getValue()) {
			return renderer.getHeight();
		} else {
			return mc.fontRenderer.FONT_HEIGHT;
		}
	}

}
