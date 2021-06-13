package dev.zihasz.zware.utils.misc;

import dev.zihasz.zware.utils.Util;

public class Timer implements Util {

	private long time = -1L;

	public boolean passedNS(long ns) { return System.nanoTime() - this.time >= ns; }
	public boolean passedMS(long ms) { return this.passedNS(this.convertMSToNS(ms)); }
	public boolean passedS(double s) { return this.passedMS((long) s * 1000L); }

	public long convertMSToNS(long time) { return time * 1000000L; }
	public long convertNSToMS(long time) { return time / 1000000L; }

	public void reset() {
		this.time = System.nanoTime();
	}

	public long getTime() { return time; }

	/**
	 * Set the timers current time.
	 * @param time The time in *nano* seconds.
	 */
	public void setTime(long time) { this.time = time; }

}
