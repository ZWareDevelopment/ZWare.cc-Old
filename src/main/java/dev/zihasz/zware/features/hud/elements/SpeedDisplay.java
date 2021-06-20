package dev.zihasz.zware.features.hud.elements;

import dev.zihasz.zware.features.hud.HudElement;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.utils.entity.EntityUtils;
import dev.zihasz.zware.utils.math.MathUtils;
import dev.zihasz.zware.utils.render.Rainbow;
import dev.zihasz.zware.utils.render.TextRenderer;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.Format;

public class SpeedDisplay extends HudElement {

	private final Rainbow rb = new Rainbow();
	private final Format f = new DecimalFormat("###.##");
	private Setting<Color> color = new Setting<>("Color", "The color to draw the watermark in.", new Color(0xff98ff98));
	private Setting<Boolean> rainbow = new Setting<>("Rainbot", "Draw the text in rainbow.", true);

	public SpeedDisplay() {
		super("Speed", "Displays speed and related info", 5, 15);
	}

	@Override
	public void draw() {
		TextRenderer.drawString("Speed: " + f.format(EntityUtils.calcSpeed(mc.player)), this.x, this.y, rainbow.getValue() ? rb.getColor() : color.getValue(), true);
		TextRenderer.drawString("Velocity: " + f.format(EntityUtils.getVelocity(mc.player)), this.x, this.y + TextRenderer.getFontHeight(), rainbow.getValue() ? rb.getColor() : color.getValue(), true);
		TextRenderer.drawString("LT. Diff.:" + f.format(MathUtils.doubleAndAdd(mc.player.posX - mc.player.lastTickPosX, mc.player.posY - mc.player.lastTickPosY, mc.player.posZ - mc.player.lastTickPosZ)), this.x, this.y + TextRenderer.getFontHeight() + TextRenderer.getFontHeight(), rainbow.getValue() ? rb.getColor() : color.getValue(), true);
	}
}
