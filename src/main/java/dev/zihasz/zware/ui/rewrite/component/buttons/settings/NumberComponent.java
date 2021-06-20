package dev.zihasz.zware.ui.rewrite.component.buttons.settings;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.ui.rewrite.ClickGUI;
import dev.zihasz.zware.ui.rewrite.component.panel.Frame;
import dev.zihasz.zware.ui.rewrite.component.buttons.ModuleComponent;
import dev.zihasz.zware.utils.math.MathUtils;
import dev.zihasz.zware.utils.render.Quad;
import dev.zihasz.zware.utils.render.Renderer2D;
import dev.zihasz.zware.utils.render.TextRenderer;

public class NumberComponent extends SettingComponent<Number> {
	private boolean dragging = false;
	private double dragWidth;

	public NumberComponent(ModuleComponent parent, Setting<Number> setting, int offset) {
		super(parent, setting, offset);
		dragWidth = Frame.FRAME_WIDTH * (setting.getValue().doubleValue() / (setting.getMax().doubleValue() - setting.getMin().doubleValue()));
	}

	@Override
	public void render(int x, int y) {
		drawBackground(x, y, false);
		final Quad q = getQuad();
		Renderer2D.drawRect(q.getX(), q.getY(), (float) (q.getX() + dragWidth + 1), q.getBottom(), hovered(x, y) ? ClickGUI.defaultScheme.background.brighter() : ClickGUI.defaultScheme.background.brighter().brighter());
		TextRenderer.drawString(setting.getName() + ": " + ChatFormatting.WHITE + setting.getValue(), q.getX() + 3, q.getY() + (q.height() - TextRenderer.getFontHeight()) / 2, ClickGUI.defaultScheme.font);
	}

	@Override
	public void update(int x, int y) {
		if (dragging) {
			double min = setting.getMin().doubleValue();
			double max = setting.getMax().doubleValue();

			dragWidth = MathUtils.clamp(0, Frame.FRAME_WIDTH - 1, (x - 1) - getQuad().getX());

			if (dragWidth == 0) {
				setting.setValue(setting.getMin().doubleValue());
			} else {
				Number newValue = MathUtils.roundToPlace(((dragWidth / (Frame.FRAME_WIDTH - 1)) * (max - min) + min), setting.getValue() instanceof Integer ? 0 : 2);
				setting.setValue(newValue);
			}
		} else if (hovered(x, y))
			ClickGUI.hovered = this;
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
		if (hovered(x, y)) {
			if (button == 0) {
				dragging = true;
			}
		}
	}

	@Override
	public void mouseReleased(int x, int y, int button) {
		dragging = false;
	}

	@Override
	public void keyTyped(char typedChar, int button) {

	}

	@Override
	public int height() {
		return SETTING_HEIGHT;
	}

	@Override
	public void guiClosed() {
		dragging = false;
	}
}
