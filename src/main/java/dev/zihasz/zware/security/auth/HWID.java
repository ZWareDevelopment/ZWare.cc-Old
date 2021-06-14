package dev.zihasz.zware.security.auth;

import dev.zihasz.sec4j.encryption.AES;
import dev.zihasz.sec4j.hashing.SHA2;
import dev.zihasz.zware.ZWare;
import dev.zihasz.zware.utils.Util;
import org.jetbrains.annotations.Nullable;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

import java.net.NetworkInterface;
import java.util.Enumeration;

public class HWID implements Util {

	@Nullable
	public static String getHWID() {
		try {
			return AES.encrypt(hwid(), "", "hwid");
		} catch (Exception exception) {
			if (ZWare.clientManager.isDebug()) ZWare.LOGGER.error(exception.getMessage());
		}
		return null;
	}

	private static String nid() throws Exception {
		StringBuilder nid = new StringBuilder();

		Enumeration<NetworkInterface> networkInterfaces = null;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();

			while (networkInterfaces.hasMoreElements()) {
				if (networkInterfaces.nextElement().isVirtual()) continue;
				NetworkInterface ni = networkInterfaces.nextElement();
				nid.append(ni.getName()).append(ni.getDisplayName()).append(ni.getMTU()).append(new String(ni.getHardwareAddress()));
			}
		} catch (Exception ignored) {
			return "";
		}

		return SHA2.sha512(nid.toString());
	}
	private static String hwid() throws Exception {
		SystemInfo si = new SystemInfo();
		HardwareAbstractionLayer hardwareAbstractionLayer = si.getHardware();
		OperatingSystem operatingSystem = si.getOperatingSystem();
		CentralProcessor processor = hardwareAbstractionLayer.getProcessor();

		String arch = System.getProperty("os.arch");
		String os = operatingSystem.getFamily() + " " + operatingSystem.getManufacturer();
		String process = processor.getProcessorIdentifier().getName() + " " + processor.getProcessorIdentifier().getMicroarchitecture();

		return SHA2.sha512(arch + os + process);
	}

}
