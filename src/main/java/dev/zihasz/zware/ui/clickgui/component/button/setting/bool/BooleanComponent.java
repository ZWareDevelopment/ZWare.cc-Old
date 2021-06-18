package dev.zihasz.zware.ui.clickgui.component.button.setting.bool;

import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.ui.clickgui.component.button.ModuleComponent;
import dev.zihasz.zware.ui.clickgui.component.button.setting.SettingComponent;
import dev.zihasz.zware.utils.render.ColorScheme;
import dev.zihasz.zware.utils.render.Renderer2D;
import dev.zihasz.zware.utils.render.TextRenderer;

public class BooleanComponent extends SettingComponent<Boolean> {

	public BooleanComponent(ModuleComponent parent, Setting<Boolean> setting, int x, int y, int width, int height, ColorScheme colorScheme) {
		super(parent, setting, x, y, width, height, colorScheme);
	}

	@Override
	public void draw(int x, int y) {
		Renderer2D.drawRect(this.x, this.y, this.width, this.height, setting.getValue() ? colorScheme.foreground.darker() : colorScheme.background.darker());
		TextRenderer.drawCenteredString(this.getSettingName(), this.x, this.y, this.width, this.height, colorScheme.font, true);
	}

	@Override
	public void click(int x, int y, int button) {
		if (hovered(x, y)) {
			if (button == 0 || button == 1)
				setting.setValue(!setting.getValue());
		}
	}

	@Override
	public void press(int code, char character) {

	}
}
