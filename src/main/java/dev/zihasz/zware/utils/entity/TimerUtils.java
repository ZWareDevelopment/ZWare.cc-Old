package dev.zihasz.zware.utils.entity;

import dev.zihasz.zware.utils.Util;

public class TimerUtils implements Util {

	public static float getTimer() {
		return mc.timer.tickLength;
	}

	public static void setTimer(float speed) {
		mc.timer.tickLength = 50 / speed;
	}

	public static void resetTimer() {
		mc.timer.tickLength = 50;
	}

}
