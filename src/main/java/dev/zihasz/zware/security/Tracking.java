package dev.zihasz.zware.security;

import dev.zihasz.zware.security.auth.HWID;
import dev.zihasz.zware.utils.Util;
import dev.zihasz.zware.utils.networking.RequestUtils;

import java.io.IOException;

public class Tracking implements Util {

	private static String trackingUrl = "https://localhost:1337/tracking";

	public static void onStarted() {
		try {
			log(Action.START_CLIENT);
		} catch (IOException exception) {
			// Exit.exit(true);
		}
	}
	public static void onStopped() {
		try {
			log(Action.STOP_CLIENT);
		} catch (IOException exception) {
			// Exit.exit(true);
		}
	}

	private static void log(Action action) throws IOException {
		String mc_username = mc.session.getUsername();
		String mc_uuid_str = mc.session.getPlayerID();

		String pc_username = System.getProperty("user.name");
		String hwid = HWID.getHWID();

		String act = action.toString().toLowerCase();
		String json = String.format(
				"{ " +
						"\"action\": \"%s\", " +
						"\"mc_username\": \"%s\", " +
						"\"mc_uuid_str\": \"%s\", " +
						"\"pc_username\": \"%s\", " +
						"\"hwid\": \"%s\" " +
				"}",
				act,
				mc_username,
				mc_uuid_str,
				pc_username,
				hwid
		);

		RequestUtils.postJson(trackingUrl, json);
	}

	private enum Action {
		START_CLIENT,
		STOP_CLIENT
	}

}
