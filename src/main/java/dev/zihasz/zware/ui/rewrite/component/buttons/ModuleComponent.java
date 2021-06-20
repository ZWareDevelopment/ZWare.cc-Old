package dev.zihasz.zware.ui.rewrite.component.buttons;

import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.ui.rewrite.GuiClick;
import dev.zihasz.zware.ui.rewrite.component.Component;
import dev.zihasz.zware.ui.rewrite.component.panel.Frame;
import dev.zihasz.zware.ui.rewrite.component.buttons.settings.BooleanComponent;
import dev.zihasz.zware.ui.rewrite.component.buttons.settings.ModeComponent;
import dev.zihasz.zware.ui.rewrite.component.buttons.settings.NumberComponent;
import dev.zihasz.zware.ui.rewrite.component.buttons.settings.SettingComponent;
import dev.zihasz.zware.utils.render.Quad;
import dev.zihasz.zware.utils.render.Renderer2D;
import dev.zihasz.zware.utils.render.TextRenderer;
import net.minecraft.util.ChatAllowedCharacters;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class ModuleComponent implements Component {
	public static final int MODULE_HEIGHT = 20;
	private final Module mod;
	private Frame parent;
	private ArrayList<SettingComponent<?>> settings;
	private int settingOff;
	private int offset;
	private boolean open = false;

	private String search = "";

	public ModuleComponent(Frame parent, Module mod, int offset) {
		this.parent = parent;
		this.mod = mod;
		this.offset = offset;

		this.settings = new ArrayList<>();
		this.settingOff = 0;

		for (Setting<?> s : mod.getSettings()) {
			if (s.getValue() instanceof Boolean) {
				settings.add(new BooleanComponent(this, (Setting<Boolean>) s, settingOff));
			} else if (s.getValue() instanceof Enum) {
				settings.add(new ModeComponent(this, (Setting<Enum<?>>) s, settingOff));
			} else if (s.getValue() instanceof Number) {
				settings.add(new NumberComponent(this, (Setting<Number>) s, settingOff));
			}
			settingOff += SettingComponent.SETTING_HEIGHT;
		}
	}

	@Override
	public void render(int x, int y) {
		final Quad q = new Quad(parent.getX(), parent.getY() + Frame.FRAME_HEIGHT + offset, parent.getX() + Frame.FRAME_WIDTH, parent.getY() + Frame.FRAME_HEIGHT + MODULE_HEIGHT + offset);
		Renderer2D.drawRect(
				q.getX(),
				q.getY(),
				q.getRight(),
				q.getBottom(),
				mod.isEnabled() ?
						(hovered(x, y) ? GuiClick.defaultScheme.foreground.brighter() : GuiClick.defaultScheme.foreground) :
						(hovered(x, y) ? GuiClick.defaultScheme.background.brighter() : GuiClick.defaultScheme.background)
		);
		TextRenderer.drawCenteredString(
				getModuleName(),
				(int) q.getX(),
				(int) q.getY(),
				(int) q.width(),
				(int) q.height(),
				search.isEmpty() ? GuiClick.defaultScheme.font : getModuleName().toLowerCase().contains(search.toLowerCase()) ? GuiClick.defaultScheme.font : GuiClick.defaultScheme.font.darker(),
				true);

		if (open) {
			settings.forEach(e -> e.render(x, y));
			Renderer2D.drawRect(q.getX(), q.getBottom(), q.getX() + 1, q.getY() + height(), GuiClick.defaultScheme.foreground);
		}
	}

	@Override
	public void update(int x, int y) {
		if (hovered(x, y))
			GuiClick.hovered = this;

		if (open)
			settings.forEach(e -> e.update(x, y));
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
		if (hovered(x, y)) {
			switch (button) {
				case 0:
					mod.toggle();
					break;
				case 1:
					open = !open;
					break;
			}
		}
		if (open)
			settings.forEach(e -> e.mouseClicked(x, y, button));
	}

	@Override
	public void mouseReleased(int x, int y, int button) {
		if (open)
			settings.forEach(e -> e.mouseReleased(x, y, button));
	}

	@Override
	public void keyTyped(char typedChar, int button) {
		if (button != Keyboard.KEY_NONE) {
			if (button == Keyboard.KEY_DELETE) search = "";
			else if (button == Keyboard.KEY_BACK && !search.equals("")) search = search.substring(0, search.length() - 1);
			else if (button == Keyboard.KEY_BACK) return;
			else if (button == Keyboard.KEY_RETURN) search = "";
			else if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) search += typedChar;
		}

		if (open)
			settings.forEach(e -> e.keyTyped(typedChar, button));
	}

	@Override
	public int height() {
		if (!open)
			return MODULE_HEIGHT;
		else {
			return MODULE_HEIGHT + settings.size() * SettingComponent.SETTING_HEIGHT;
		}
	}

	@Override
	public String getDescription() {
		return mod.getDescription();
	}

	@Override
	public void guiClosed() {
		settings.forEach(Component::guiClosed);
	}

	private boolean hovered(int x, int y) {
		return new Quad(
				parent.getX(),
				parent.getY() + Frame.FRAME_HEIGHT + offset,
				parent.getX() + Frame.FRAME_WIDTH,
				parent.getY() + Frame.FRAME_HEIGHT + MODULE_HEIGHT + offset - 1).isWithinIncluding(x, y);
	}

	public Frame getParent() {
		return parent;
	}
	public void setParent(Frame parent) {
		this.parent = parent;
	}
	public Module getMod() {
		return mod;
	}
	public ArrayList<SettingComponent<?>> getSettings() {
		return settings;
	}
	public void setSettings(ArrayList<SettingComponent<?>> settings) {
		this.settings = settings;
	}
	public int getSettingOff() {
		return settingOff;
	}
	public void setSettingOff(int settingOff) {
		this.settingOff = settingOff;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public String getModuleName() {
		return StringUtils.capitalize(mod.getName().toLowerCase());
	}

}
