package dev.zihasz.zware.ui.clickgui.component.button.setting.bind;

import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.ui.clickgui.component.Component;
import dev.zihasz.zware.ui.clickgui.component.button.Button;
import dev.zihasz.zware.utils.render.ColorScheme;

public class ModuleBindComponent extends Button {

	public int code;
	public char character;

	public ModuleBindComponent(Module module, Component parent, int x, int y, int width, int height, ColorScheme colorScheme) {
		super(parent, x, y, width, height, colorScheme);
		this.code = module.getBind();
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
