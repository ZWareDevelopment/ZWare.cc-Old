package dev.zihasz.zware.security.utils;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class Exit {

	public static void exit(boolean hard) {
		FMLCommonHandler.instance().exitJava(-1, hard);
	}
	public static void crash() {
		throw new NullPointerException(null); // :trollage:
	}
	public static void overflow(int integer) { // Cursed
		overflow(integer);
	}

}
