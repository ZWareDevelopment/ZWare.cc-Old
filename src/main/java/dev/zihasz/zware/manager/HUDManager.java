package dev.zihasz.zware.manager;

import dev.zihasz.zware.features.hud.HudElement;
import dev.zihasz.zware.features.setting.Setting;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HudManager {

	private static final List<HudElement> elements = new ArrayList<>();

	public HudManager() {
		Set<Class> moduleClasses = findClasses(HudElement.class.getPackage().getName(), HudElement.class);
		moduleClasses.forEach(moduleClass -> {
			try {
				this.addElement((HudElement) moduleClass.newInstance());
			} catch (Exception ignored) {}
		});
	}

	private void addElement(HudElement element) {
		try {
			for (Field field : element.getClass().getDeclaredFields()) {
				if (Setting.class.isAssignableFrom(field.getType())) {
					if (!field.isAccessible()) {
						field.setAccessible(true);
					}
					final Setting<?> val = (Setting<?>) field.get(element);
					element.addSetting(val);
				}
			}
		} catch (Exception ignored) {
		}
		elements.add(element);
	}

	public static Set findClasses(String pack, Class subType) {
		Reflections reflections = new Reflections(pack);
		return reflections.getSubTypesOf(subType);
	}
	
	public static HudElement getElement(Class<? extends HudElement> element) {
		return elements.stream().filter(element1 -> element1.getClass() == element).findFirst().orElse(null);
	}
	public static HudElement getElement(String element) {
		return elements.stream().filter(element1 -> element1.getName().equalsIgnoreCase(element)).findFirst().orElse(null);
	}
	
	public static List<HudElement> getElements() {
		return elements;
	}

}
