package dev.zihasz.zware.manager;

public class ClientManager {

	private boolean experimental;
	private boolean debug;

	public ClientManager() {
		String e = System.getProperty("dev.zihasz.zware.experimental");
		String d = System.getProperty("dev.zihasz.zware.debug");

		experimental = e != null && e.equalsIgnoreCase("true");
		debug = d != null && d.equalsIgnoreCase("true");
	}

	public boolean isExperimental() {
		return experimental;
	}
	public boolean isDebug() {
		return debug;
	}
}
