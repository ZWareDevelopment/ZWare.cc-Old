package dev.zihasz.zware.utils.types;

import dev.zihasz.zware.utils.math.MathUtils;

public class Range {

	private final Number min, max;
	private Number lowValue, highValue;

	public Range(Number min, Number defaultLow, Number defaultHigh, Number max) {
		this.min = min;
		this.lowValue = defaultLow;
		this.highValue = defaultHigh;
		this.max = max;
	}
	public Range(Number min, Number defaultValue, Number max) {
		this(
				min,
				defaultValue.doubleValue() - defaultValue.doubleValue() / 2,
				defaultValue.doubleValue() + defaultValue.doubleValue() / 2,
				max
		);
	}
	public Range(Number min, Number max) {
		this(min, min, max, max);
	}

	public Number getAverage() {
		return (this.min.doubleValue() + this.max.doubleValue()) / 2;
	}
	public Number getRandom() {
		return MathUtils.randomIntBetween((int) lowValue, (int) highValue);
	}

	public Number getMin() {
		return min;
	}
	public Number getLowValue() {
		return lowValue;
	}
	public Number getHighValue() {
		return highValue;
	}
	public Number getMax() {
		return max;
	}

	public void setLowValue(Number lowValue) {
		this.lowValue = lowValue;
	}
	public void setHighValue(Number highValue) {
		this.highValue = highValue;
	}

}
