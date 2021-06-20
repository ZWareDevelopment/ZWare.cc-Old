package dev.zihasz.zware.ui.rewrite.component.buttons.settings;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.ui.rewrite.GuiClick;
import dev.zihasz.zware.ui.rewrite.component.buttons.ModuleComponent;
import dev.zihasz.zware.utils.render.TextRenderer;

public class ModeComponent extends SettingComponent<Enum<?>> {

	public ModeComponent(ModuleComponent parent, Setting<Enum<?>> setting, int offset) {
		super(parent, setting, offset);
	}

	@Override
	public void render(int x, int y) {
		drawBackground(x, y, false);
		TextRenderer.drawString(setting.getName() + ": " + ChatFormatting.WHITE + setting.getValue(), getQuad().getX() + 3, getQuad().getY() + (getQuad().height() - TextRenderer.getFontHeight()) / 2, GuiClick.defaultScheme.font);
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
				setting.setEnumValue(getNextEnumValue(setting.getValue(), false));
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

	private String getNextEnumValue(Enum<?> currentEnum, boolean reverse) {
		int i = 0;

		for (; i < currentEnum.getClass().getEnumConstants().length; i++) {
			final Enum<?> e = currentEnum.getClass().getEnumConstants()[i];
			if (e.name().equalsIgnoreCase(currentEnum.name())) {
				break;
			}
		}

		return currentEnum.getClass()
				.getEnumConstants()[(reverse ? (i != 0 ? i - 1 : currentEnum.getClass().getEnumConstants().length - 1)
				: i + 1) % currentEnum.getClass().getEnumConstants().length].toString();
	}
}
