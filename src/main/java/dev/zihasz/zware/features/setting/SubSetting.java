package dev.zihasz.zware.features.setting;

import java.util.function.Predicate;

public class SubSetting<T> extends Setting<T> {

	Setting<?> parent;

	public SubSetting(Setting<?> parent, String name, String description, T value, T min, T max, Predicate<T> visibility) {
		super(name, description, value, min, max, visibility);
		this.parent = parent;
	}
	public SubSetting(Setting<?> parent, String name, String description, T value, T min, T max) {
		this(parent, name, description, value, min, max, null);
	}
	public SubSetting(Setting<?> parent, String name, String description, T value, Predicate<T> visibility) {
		this(parent, name, description, value, null, null, visibility);
	}
	public SubSetting(Setting<?> parent, String name, String description, T value) {
		this(parent, name, description, value, null, null, null);
	}

	public Setting<?> getParent() {
		return parent;
	}

}
