package dev.zihasz.zware.features.module.misc;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;

import java.awt.*;
import java.net.URI;

public class AutoPorn extends Module {

	Setting<AutoPornMode> mode = new Setting<>("Mode", "Whyt porn do you like?", AutoPornMode.Lesbian);

	public AutoPorn() {
		super("AutoPorn", "Opens porn in enw browser", Category.MISC);
	}

	@Override
	public void onEnable() {
		try {
			Desktop.getDesktop().browse(URI.create(String.format("https://www.pornhub.com/video/search?search=%s", mode.getValue().toString().toLowerCase())));
		} catch (Exception e) {}
		disable();
	}

	public enum AutoPornMode {
		Straight,
		Lesbian,
		Step,
		MILF,
		Hentai,
		Feet,
		BDSM,
		Teas,
		Creampie,
		Squirt,
		Gangbang,
		Teen,
		Cumshot,
	}

}
