package dev.zihasz.zware.ui.rewrite.component.buttons.settings;

import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.ui.rewrite.GuiClick;
import dev.zihasz.zware.ui.rewrite.component.buttons.ModuleComponent;
import dev.zihasz.zware.utils.render.TextRenderer;

public class BooleanComponent extends SettingComponent<Boolean> {
	public BooleanComponent(ModuleComponent parent, Setting<Boolean> setting, int offset) {
		super(parent, setting, offset);
	}

	@Override
	public void render(int x, int y) {
		drawBackground(x, y, setting.getValue());
		TextRenderer.drawString(setting.getName(), parent.getParent().getX() + 3, getQuad().getY() + (getQuad().height() - TextRenderer.getFontHeight()) / 2f, GuiClick.defaultScheme.font);
	}

	@Override
	public void update(int x, int y) {
		if (hovered(x, y))
			GuiClick.hovered = this;
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
		if (hovered(x, y)) {
			if (button == 0) {
				setting.setValue(!setting.getValue());
			}
		}
	}

	@Override
	public void mouseReleased(int x, int y, int button) {

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

	}
}
