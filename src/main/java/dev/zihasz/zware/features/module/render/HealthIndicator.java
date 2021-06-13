package dev.zihasz.zware.features.module.render;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.ui.colors.Dracula;
import dev.zihasz.zware.utils.render.Rainbow;
import dev.zihasz.zware.utils.render.Renderer2D;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class HealthIndicator extends Module {

	private Setting<Color> mainColor = new Setting<>("Color", "Color of the circle", Dracula.ansiBrightRed);
	private Setting<Boolean> mainRainbow = new Setting<>("Rainbow", "Should the circle rainbow", true);
	private Setting<Float> scale = new Setting<>("Scale", "Scale of the circle", 1f, 0f, 10f);
	private Setting<Float> width = new Setting<>("Width", "Width of the circle", 2f, 0f, 10f);

	private Setting<Boolean> doubleSetting = new Setting<>("Double", "Render 2 circles (for health and absorption)", true);
	private Setting<Float> distance = new Setting<>("Distance", "Distance between circles", 2f, 0f, 10f, v -> doubleSetting.getValue());
	private Setting<Color> healthColor = new Setting<>("HealthColor", "Color of the health", Dracula.ansiBrightRed, v -> doubleSetting.getValue());
	private Setting<Boolean> healthRainbow = new Setting<>("HealthRainbow", "Should the health rainbow", false, v -> doubleSetting.getValue());
	private Setting<Color> absorpColor = new Setting<>("AbsorptionColor", "Color of the absorption", Dracula.ansiBrightYellow, v -> doubleSetting.getValue());
	private Setting<Boolean> absorpRainbow = new Setting<>("AbsorptionRainbow", "Should the absorption rainbow", false, v -> doubleSetting.getValue());

	private Setting<Boolean> pulseSetting = new Setting<>("Pulse", "Should the circle pulse", true);
	private Setting<Pulse> pulse = new Setting<>("DropDown", "DropDown of the pulse", Pulse.Sin, v -> pulseSetting.getValue());
	private Setting<Float> speed = new Setting<>("Speed", "Speed of the pulse", 10f, 0f, 100f, v -> pulseSetting.getValue());

	public HealthIndicator() {
		super("Health", "Shows how much health you have", Category.RENDER);
	}

	Rainbow rainbow = new Rainbow();

	@Override
	public void onRender2D() {
		ScaledResolution sr = new ScaledResolution(mc);

		Color main = mainRainbow.getValue() ? rainbow.getColor() : mainColor.getValue();
		Color absorption = absorpRainbow.getValue() ? rainbow.getColor() : absorpColor.getValue();
		Color health = healthRainbow.getValue() ? rainbow.getColor() : healthColor.getValue();

		if (doubleSetting.getValue()) {
			if (mc.player.getAbsorptionAmount() > 0) {
				Renderer2D.drawCircle(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, radiusHealth(), width(), health);
				Renderer2D.drawCircle(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, radiusAbsorption(), width(), absorption);
			} else {
				Renderer2D.drawCircle(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, radiusHealth(), width(), health);
			}
		} else {
			Renderer2D.drawCircle(sr.getScaledWidth() / 2f, sr.getScaledHeight() / 2f, radiusAbsorption(), width(), main);
		}
	}

	private float width() {
		float w = width.getValue();
		if (pulseSetting.getValue()) {
			switch (pulse.getValue()) {
				case Simple:
					w = mc.player.ticksExisted % speed.getValue() * width.getValue();
					break;
				case Sin:
					w = (float) Math.sin(mc.player.ticksExisted);
					break;
				case Cos:
					w = (float) Math.cos(mc.player.ticksExisted);
					break;
				case Weird:
					w = (float) Math.pow(Math.sin(mc.player.ticksExisted), 10) * 10;
					break;
			}
		}
		return w == 0 ? 1 : w;
	}
	private float radiusHealth() {
		float r = mc.player.getHealth() * scale.getValue();
		return r == 0 ? 1 : r;
	}
	private float radiusAbsorption() {
		float r = (mc.player.getHealth() + mc.player.getAbsorptionAmount()) * scale.getValue();
		return r == 0 ? 1 : r;
	}

	private enum Pulse {
		Simple,
		Sin,
		Cos,
		Weird
	}

}
