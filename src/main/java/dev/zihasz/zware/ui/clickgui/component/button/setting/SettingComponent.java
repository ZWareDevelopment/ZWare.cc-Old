package dev.zihasz.zware.ui.clickgui.component.button.setting;

import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.ui.clickgui.component.button.Button;
import dev.zihasz.zware.ui.clickgui.component.button.ModuleComponent;
import dev.zihasz.zware.utils.render.ColorScheme;
import org.apache.commons.lang3.StringUtils;


public abstract class SettingComponent<T> extends Button {

	protected Setting<T> setting;

	public SettingComponent(ModuleComponent parent, Setting<T> setting, int x, int y, int width, int height, ColorScheme colorScheme) {
		super(parent,x, y, width, height, colorScheme);
		this.setting = setting;
	}

	public Setting<T> getSetting() { return setting; }
	public String getSettingName() { return StringUtils.capitalize(setting.getName().toLowerCase()); }

}
