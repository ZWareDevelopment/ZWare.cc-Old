package dev.zihasz.zware;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;

public class Discord {

	public static final String APP_ID = "834329758686773258";
	public static final String STEAM_ID = "";
	public static DiscordRPC rpc = DiscordRPC.INSTANCE;
	public static DiscordRichPresence presence = new DiscordRichPresence();
	private static Thread presenceThread;

	public static void start() {
		DiscordEventHandlers handlers = new DiscordEventHandlers();
		handlers.ready = user -> ZWare.LOGGER.info("RPC Connected! User: " + user.username + "#" + user.discriminator);
		handlers.disconnected = (errorCode, message) -> ZWare.LOGGER.warn("RPC Disconnected! Code: " + errorCode + " Message: " + message);
		handlers.errored = (errorCode, message) -> ZWare.LOGGER.warn("RPC Error! Code: " + errorCode + " Message: " + message);
		handlers.joinRequest = request -> ZWare.LOGGER.info(request.username + "#" + request.discriminator + " requested to play with you!");
		handlers.joinGame = ZWare.LOGGER::info;
		handlers.spectateGame = ZWare.LOGGER::info;
		rpc.Discord_Initialize(APP_ID, handlers, true, STEAM_ID);
		presence.startTimestamp = System.currentTimeMillis() / 1000L;
		presence.details = Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu ? "In the main menu." : "Playing " + (Minecraft.getMinecraft().currentServerData != null ? "on " + Minecraft.getMinecraft().currentServerData.serverIP + "." : " singleplayer.");
		presence.state = ZWare.MOD_NAME + " " + ZWare.MOD_VERSION_PREFIX + ZWare.MOD_VERSION;
		presence.largeImageKey = "phobos";
		presence.largeImageText = "Phobos 1.9.0";
		rpc.Discord_UpdatePresence(presence);
		presenceThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				rpc.Discord_RunCallbacks();
				presence.details = Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu ? "In the main menu." : "Playing " + (Minecraft.getMinecraft().currentServerData != null ? "on " + Minecraft.getMinecraft().currentServerData.serverIP + "." : " singleplayer.");
				presence.state = ZWare.MOD_NAME + " " + ZWare.MOD_VERSION_PREFIX + ZWare.MOD_VERSION;
				rpc.Discord_UpdatePresence(presence);
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException ignored) {
				}
			}
		}, "PresenceThread");
		presenceThread.start();
	}

	public static void stop() {
		if (presenceThread != null && !presenceThread.isInterrupted())
			presenceThread.interrupt();
		rpc.Discord_Shutdown();
	}

	public static void updatePresence(String details, String state) {
		presence.details = details;
		presence.state = state;
		rpc.Discord_UpdatePresence(presence);
	}

}
