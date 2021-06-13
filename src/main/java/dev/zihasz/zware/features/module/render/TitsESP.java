package dev.zihasz.zware.features.module.render;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.utils.client.Message;

public class TitsESP extends Module {

	public TitsESP() {
		super("TitsESP", "your mama (hnaha horny fuck)", Category.RENDER);
	}

	@Override
	public void onEnable() {
		Message.sendErrorMessage("Too horny");
		disable();
	}
}
