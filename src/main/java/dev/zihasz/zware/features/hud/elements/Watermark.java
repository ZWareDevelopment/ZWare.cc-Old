package dev.zihasz.zware.features.hud.elements;

import dev.zihasz.zware.ZWare;
import dev.zihasz.zware.features.hud.HudElement;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.utils.render.Rainbow;

import java.awt.*;

public class Watermark extends HudElement {

	public Watermark() {
		super("Watermark", "Draws an element with the client name and optionally the version on screen.", 5, 5);
	}

	private Setting<Color> color = new Setting<>("Color", "The color to draw the watermark in.", new Color(0xff98ff98));
	private Setting<Boolean> rainbow = new Setting<>("Rainbot", "Draw the text in rainbow.", true);
	private Setting<Boolean> version = new Setting<>("Version", "Draw the version number after the client name", true);

	private final Rainbow rb = new Rainbow();

	@Override
	public void draw() {
		mc.fontRenderer.drawString(
				ZWare.MOD_NAME + (version.getValue() ? " " + ZWare.MOD_VERSION : ""),
				this.x,
				this.y,
				(rainbow.getValue() ? rb.getColor() : color.getValue()).getRGB()
		);
	}
}
