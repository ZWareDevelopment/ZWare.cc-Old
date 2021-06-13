package dev.zihasz.zware.utils.networking.useragent;

public class UserAgent {

	public static String format = "Mozilla/5.0 (%s; %s) %s/%s";

	private String platform;
	private String hardware;
	private String appName;
	private String appVersion;

	public UserAgent(String platform, String hardware, String appName, String appVersion) {
		this.platform = platform;
		this.hardware = hardware;
		this.appName = appName;
		this.appVersion = appVersion;
	}

	public UserAgent() {
		this("", "", "", "");
	}

	public String getPlatform() {
		return platform;
	}
	public String getHardware() {
		return hardware;
	}
	public String getAppName() {
		return appName;
	}
	public String getAppVersion() {
		return appVersion;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public void setHardware(String hardware) {
		this.hardware = hardware;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	@Override
	public String toString() {
		return String.format("Mozilla/5.0 (%s; %s) %s/%s", platform, hardware, appName, appVersion);
	}
}
