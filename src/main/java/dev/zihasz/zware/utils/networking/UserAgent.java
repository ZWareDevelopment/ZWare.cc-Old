package dev.zihasz.zware.utils.networking;

import dev.zihasz.zware.ZWare;

public class UserAgent {

	public static String getUserAgent() {
		return String.format("%s/%s (%s %s; %s) %s",
				ZWare.MOD_NAME,
				ZWare.MOD_VERSION_PREFIX + ZWare.MOD_VERSION,
				System.getProperty("os.name"),
				System.getProperty("os.version"),
				System.getProperty("os.arch"),
				System.getProperty("user.name"));
	}

}
