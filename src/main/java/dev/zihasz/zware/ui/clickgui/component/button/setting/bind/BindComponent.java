package dev.zihasz.zware.ui.clickgui.component.button.setting.bind;

import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.ui.clickgui.component.button.ModuleComponent;
import dev.zihasz.zware.ui.clickgui.component.button.setting.SettingComponent;
import dev.zihasz.zware.utils.render.ColorScheme;
import dev.zihasz.zware.utils.types.Bind;

public class BindComponent extends SettingComponent<Bind> {

	public BindComponent(ModuleComponent parent, Setting<Bind> setting, int x, int y, int width, int height, ColorScheme colorScheme) {
		super(parent, setting, x, y, width, height, colorScheme);
	}

	@Override
	public void draw(int x, int y) {

	}

	@Override
	public void click(int x, int y, int button) {

	}

	@Override
	public void press(int code, char character) {

	}
}
