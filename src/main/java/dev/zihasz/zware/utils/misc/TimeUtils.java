package dev.zihasz.zware.utils.misc;

import dev.zihasz.zware.utils.Util;

public class TimeUtils implements Util {

	public static int ticksToMs(int ticks) {
		return ticks * 500;
	}
	public static int msToTicks(int millis) {
		return millis / 500;
	}

	public static long FloatToLong(Float value) {
		return (long)((float) value);
	}

}
