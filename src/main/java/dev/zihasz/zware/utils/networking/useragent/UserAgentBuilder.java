package dev.zihasz.zware.utils.networking.useragent;

import dev.zihasz.zware.ZWare;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;

public class UserAgentBuilder {

	public UserAgentBuilder() {}

	public UserAgent build() {
		UserAgent userAgent = new UserAgent();

		userAgent.setPlatform(String.format("%s %s %s",
				System.getProperty("os.name"),
				System.getProperty("os.version"),
				System.getProperty("os.arch")));
		userAgent.setHardware(String.format(
				"[ \"%s\", \"%s\" ]",
				this.getHardwareInfo()[0],
				this.getHardwareInfo()[1]
		));
		userAgent.setAppName(ZWare.MOD_NAME);
		userAgent.setAppVersion(ZWare.MOD_VERSION);

		return userAgent;
	}

	public String[] getHardwareInfo() {
		HardwareAbstractionLayer hardware = new SystemInfo().getHardware();
		StringBuilder gpu = new StringBuilder();
		for (GraphicsCard card : hardware.getGraphicsCards())
			gpu.append(card.getName()).append(" ");
		return new String[] {
				hardware.getProcessor().getProcessorIdentifier().getName(),
				gpu.toString()
		};
	}

}
