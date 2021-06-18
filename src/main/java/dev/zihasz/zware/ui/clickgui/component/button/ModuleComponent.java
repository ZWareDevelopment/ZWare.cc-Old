package dev.zihasz.zware.ui.clickgui.component.button;

import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.ui.clickgui.component.button.setting.SettingComponent;
import dev.zihasz.zware.ui.clickgui.component.button.setting.bool.BooleanComponent;
import dev.zihasz.zware.ui.clickgui.component.button.setting.color.ColorComponent;
import dev.zihasz.zware.ui.clickgui.component.button.setting.mode.ModeComponent;
import dev.zihasz.zware.ui.clickgui.component.button.setting.number.NumberComponent;
import dev.zihasz.zware.ui.clickgui.component.panel.Frame;
import dev.zihasz.zware.ui.colors.Dracula;
import dev.zihasz.zware.utils.render.ColorScheme;
import dev.zihasz.zware.utils.render.Renderer2D;
import dev.zihasz.zware.utils.render.TextRenderer;
import dev.zihasz.zware.utils.types.Bind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatAllowedCharacters;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleComponent extends Button {

	private final Module module;
	private final ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

	private boolean expanded = false;
	private String search = "";

	private final List<SettingComponent<?>> settings = new ArrayList<>();

	public ModuleComponent(Frame parent, int x, int y, int width, int height, ColorScheme colorScheme, Module module) {
		super(parent, x, y, width, height, colorScheme);
		this.module = module;

		int offsetY = height;
		for (Setting setting: module.getSettings()) {
			if (setting.getValue() instanceof Bind) {

			} else if (setting.getValue() instanceof Boolean) {
				this.settings.add(new BooleanComponent(this, (Setting<Boolean>) setting, this.x, this.y + offsetY, width, height, colorScheme));
			} else if (setting.getValue() instanceof Color) {
				this.settings.add(new ColorComponent(this, (Setting<Color>) setting, this.x, this.y + offsetY, width, height, colorScheme));
			} else if (setting.getValue() instanceof Enum) {
				this.settings.add(new ModeComponent(this, (Setting<Enum>) setting, this.x, this.y + offsetY, width, height, colorScheme));
			} else if (setting.getValue() instanceof Number) {
				this.settings.add(new NumberComponent(this, (Setting<Number>) setting, this.x, this.y + offsetY, this.width, this.height, colorScheme));
			} else if (setting.getValue() instanceof String) {

			}
			offsetY += height;
		}
	}

	@Override
	public void draw(int x, int y) {
		Renderer2D.drawRect(this.x, this.y, this.x + this.width, this.y + this.height,
				this.hovered(x, y) ?
						(module.isEnabled() ? this.colorScheme.foreground : this.colorScheme.background).brighter() :
						(module.isEnabled() ? this.colorScheme.foreground : this.colorScheme.background)
		);
		TextRenderer.drawCenteredString(this.getModuleName(), this.x, this.y, this.width, this.height, match(this.getModuleName(), search) ? this.colorScheme.font : this.colorScheme.font.darker(), true);

		if (hovered(x, y)) TextRenderer.drawString(module.getDescription(), x + 2, y - (TextRenderer.getFontHeight() + 2) , colorScheme.font.darker());
		if (!search.equals("")) TextRenderer.drawString(search, (sr.getScaledWidth() / 2f) - (TextRenderer.getStringWidth(search) / 2), 2, colorScheme.font);

		if (expanded) {
			((Frame) parent).buttonOffset = settings.size() * height;
		}
	}

	@Override
	public void click(int x, int y, int button) {
		search = "";
		if (hovered(x, y)) {
			switch (button) {
				case 0:
					module.toggle();
					break;
				case 1:
					expanded = !expanded;
					break;
			}
		}
	}

	@Override
	public void press(int code, char character) {
		if (code != Keyboard.KEY_NONE) {
			if (code == Keyboard.KEY_DELETE) search = "";
			else if (code == Keyboard.KEY_BACK && !search.equals("")) search = search.substring(0, search.length() - 1);
			else if (code == Keyboard.KEY_BACK) return;
			else if (code == Keyboard.KEY_RETURN) search = "";
			else if (ChatAllowedCharacters.isAllowedCharacter(character)) search += character;
		}
	}

	private boolean match(String input, String search) {
		return input.toLowerCase().contains(search.toLowerCase());
	}

	public Module getModule() { return module; }
	public String getModuleName() { return StringUtils.capitalize(module.getName().toLowerCase()); }
}
