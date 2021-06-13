package dev.zihasz.zware.utils.misc;

import java.awt.*;

public class ColorUtils {

	public static Color changeAlpha(Color color, float alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

}
