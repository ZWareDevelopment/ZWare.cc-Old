package dev.zihasz.zware.features.module.chat;

import dev.zihasz.zware.ZWare;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.manager.CommandManager;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatSuffix extends Module {

	public ChatSuffix() {
		super("ChatSuffix", "Appends a suffix after every chat message.", Category.CHAT);
	}

	private final Setting<Boolean> version = new Setting<>("Version", "Show client version too", true);

	@SubscribeEvent
	public void onChat(ClientChatEvent event) {
		if (event.getOriginalMessage().startsWith("/") || event.getOriginalMessage().startsWith(CommandManager.prefix)) return;
		event.setMessage(event.getOriginalMessage() + " | " + ZWare.MOD_NAME + (version.getValue() ? " " + ZWare.MOD_VERSION_PREFIX + ZWare.MOD_VERSION : ""));
	}

}
