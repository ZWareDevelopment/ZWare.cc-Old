package dev.zihasz.zware.ui.rewrite.component.buttons.settings;

import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.ui.rewrite.GuiClick;
import dev.zihasz.zware.ui.rewrite.component.panel.Frame;
import dev.zihasz.zware.ui.rewrite.component.Component;
import dev.zihasz.zware.ui.rewrite.component.buttons.ModuleComponent;
import dev.zihasz.zware.utils.render.Quad;
import dev.zihasz.zware.utils.render.Renderer2D;

public abstract class SettingComponent<T> implements Component {
	public static int SETTING_HEIGHT = 20;

	protected int offset;
	protected Setting<T> setting;
	protected ModuleComponent parent;

	public SettingComponent(ModuleComponent parent, Setting<T> setting, int offset) {
		this.setting = setting;
		this.parent = parent;
		this.offset = offset;
	}

	protected void drawBackground(int x, int y, boolean bright) {
		final Quad q = getQuad();
		Renderer2D.drawRect(
				q.getX(),
				q.getY(),
				q.getRight(),
				q.getBottom(),
				bright ? (hovered(x, y) ? GuiClick.defaultScheme.background.brighter() : GuiClick.defaultScheme.background).brighter() : hovered(x, y) ? GuiClick.defaultScheme.background.brighter() : GuiClick.defaultScheme.background
		);
	}

	protected boolean hovered(int x, int y) {
		Quad quad = getQuad();
		quad.setBottom(getQuad().getBottom() - 1);
		return quad.isWithinIncluding(x, y);
	}

	protected Quad getQuad() {
		return new Quad(
				parent.getParent().getX(),
				parent.getParent().getY() + Frame.FRAME_HEIGHT + parent.getOffset() + ModuleComponent.MODULE_HEIGHT + offset,
				parent.getParent().getX() + Frame.FRAME_WIDTH,
				parent.getParent().getY() + Frame.FRAME_HEIGHT + parent.getOffset() + ModuleComponent.MODULE_HEIGHT + offset + SETTING_HEIGHT
		);
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public Setting<T> getSetting() {
		return setting;
	}

	public void setSetting(Setting<T> setting) {
		this.setting = setting;
	}

	public ModuleComponent getParent() {
		return parent;
	}

	public void setParent(ModuleComponent parent) {
		this.parent = parent;
	}

	@Override
	public String getDescription() {
		return setting.getDescription();
	}
}
