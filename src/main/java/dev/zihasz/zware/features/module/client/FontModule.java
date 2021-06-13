package dev.zihasz.zware.features.module.client;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;

public class FontModule extends Module {

	public FontModule() {
		super("Font", "Settings about the clients font", Category.CLIENT);
	}

	public static Setting<Boolean> custom = new Setting<>("Custom Font", "Use a custom font instead of Minecraft's one", false);
	public static Setting<Integer> style = new Setting<>("Style", "The style of the font (0 = Plain, 1 = Bold, 2 = Italic)", 0, 0, 3, v -> custom.getValue());
	public static Setting<Integer> size = new Setting<>("Size", "The size of the font", 12, 1, 72, v -> custom.getValue());
	public static Setting<Boolean> antiAlias = new Setting<>("AntiAlias", "Use anti aliasing on the fonts", true);

}
