package dev.zihasz.zware.features.setting;

import java.util.function.Predicate;

public class Setting<T> {

	private final String name, description;
	private final T min, max;
	private final Predicate<T> visibility;
	private T value;

	public Setting(String name, String description, T value, T min, T max, Predicate<T> visibility) {
		this.name = name;
		this.description = description;
		this.value = value;
		this.min = min;
		this.max = max;
		this.visibility = visibility;
	}

	public Setting(String name, String description, T value, T min, T max) {
		this(name, description, value, min, max, null);
	}

	public Setting(String name, String description, T value, Predicate<T> visibility) {
		this(name, description, value, null, null, visibility);
	}

	public Setting(String name, String description, T value) {
		this(name, description, value, null);
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}

	public T getMin() {
		return min;
	}

	public T getMax() {
		return max;
	}

	public Predicate<T> getVisibility() {
		return visibility;
	}

	public void setEnumValue(String value) {
		for (Enum<?> e : ((Enum<?>) this.value).getClass().getEnumConstants()) {
			if (e.name().equalsIgnoreCase(value)) {
				setValue((T) e);
				break;
			}
		}
	}
}
