package dev.zihasz.zware.features.module.client;

import dev.zihasz.zware.Discord;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.utils.client.Message;

public class DiscordModule extends Module {

	public DiscordModule() {
		super("Discord", "Enables Discord rich presence", Category.CLIENT);
	}

	@Override
	public void onEnable() {
		Discord.start();
		Message.sendSuccessMessage("Discord presence successfully started!");
	}

	@Override
	public void onDisable() {
		Discord.stop();
		Message.sendSuccessMessage("Discord presence successfully stopped!");
	}
}
