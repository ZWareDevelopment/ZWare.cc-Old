package dev.zihasz.zware.security;

import dev.zihasz.zware.security.auth.HWID;
import dev.zihasz.zware.utils.Util;
import dev.zihasz.zware.utils.networking.RequestUtils;
import dev.zihasz.zware.utils.networking.useragent.UserAgentBuilder;

import java.io.IOException;

public class Tracking implements Util {

	private static String trackingUrl = "https://api.zware.cc/tracking";
	private static UserAgentBuilder builder = new UserAgentBuilder();

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
		String pc_username = System.getProperty("user.name");
		String hwid = HWID.getHWID();

		String act = action.toString().toLowerCase();
		String json = String.format("{ \"action\": \"%s\", \"mc\": \"%s\", \"pc\": \"%s\", \"hwid\": \"%s\" }",
				mc_username,
				pc_username,
				hwid,
				act);

		RequestUtils.postJson(trackingUrl, json, builder.build().toString());
	}

	private enum Action {
		START_CLIENT,
		STOP_CLIENT
	}

}
