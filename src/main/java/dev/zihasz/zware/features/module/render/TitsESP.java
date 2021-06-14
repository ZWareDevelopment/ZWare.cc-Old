package dev.zihasz.zware.features.module.render;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.utils.client.Message;

import java.awt.*;
import java.net.URI;

public class TitsESP extends Module {

	public TitsESP() {
		super("TitsESP", "your mama (hnaha horny fuck)", Category.RENDER);
	}

	@Override
	public void onEnable() {
		try {

			Message.sendErrorMessage("Too horny");
			Desktop.getDesktop().browse(new URI("https://pornhub.com"));

		} catch (Exception ignored) {}
		disable();
	}
}
