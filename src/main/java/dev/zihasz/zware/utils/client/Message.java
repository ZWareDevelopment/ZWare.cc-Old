package dev.zihasz.zware.utils.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import dev.zihasz.zware.ZWare;
import dev.zihasz.zware.utils.Util;
import net.minecraft.util.text.TextComponentString;

public class Message implements Util {

	public static String watermark = ChatFormatting.GRAY + "[" + ChatFormatting.GOLD + ZWare.MOD_NAME + ChatFormatting.GRAY + "] " + ChatFormatting.RESET;

	public static void sendMessage(String message) {
		mc.player.sendMessage(new TextComponentString(message));
	}
	public static void sendClientMessage(String message) {
		Message.sendMessage(watermark + message);
	}
	public static void sendInfoMessage(String message) {
		Message.sendClientMessage(ChatFormatting.AQUA + message);
	}
	public static void sendSuccessMessage(String message) {
		Message.sendClientMessage(ChatFormatting.GREEN + message);
	}
	public static void sendWarningMessage(String message) {
		Message.sendClientMessage(ChatFormatting.YELLOW + message);
	}
	public static void sendErrorMessage(String message) {
		Message.sendClientMessage(ChatFormatting.RED + message);
	}

	public static void sendDeletableMessage(String message, int id) {
		mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(message), id);
	}
	public static void sendDeletableMessage(String message, String variable) {
		mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString(message), generateMessageId(variable));
	}
	public static int generateMessageId(String message) {
		int id = 0;
		for (char character : message.toCharArray()) {
			id += character;
			id *= 10;
		}
		return id;
	}

}
