package dev.zihasz.zware.utils.networking;

import dev.zihasz.zware.utils.Util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostNameToIP implements Util {

	public static InetAddress resolve(String hostname) {
		InetAddress address;
		try {
			address = InetAddress.getByName(hostname);
		} catch (UnknownHostException e) {
			throw new IllegalStateException("Error resolving " + hostname + ", " + e.getMessage());
		}
		return(address);
	}

}
