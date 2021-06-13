package dev.zihasz.zware.features.module.combat;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;

public class Offhand extends Module {

	public Offhand() {
		super("Offhand", "ptsd", Category.COMBAT);
	}

	private final Setting<Float> health = new Setting<>("Health", "The health to put a totem in your offhand.", 10f, 0f, 36f);
	private final Setting<Float> delay = new Setting<>("Delay", "Delay between switches", 0f, 0f, 500f);
	private final Setting<Normal> item = new Setting<>("Item", "Item to put in your offhand when above the specified health.", Normal.Crystal);

	private final Setting<Boolean> right = new Setting<>("Right", "Switch offhand when right clicking", true);
	private final Setting<Boolean> rightSword = new Setting<>("SwordOnly", "Switch offhand only when you're holding a sword.", false);
	private final Setting<Right> rightItem = new Setting<>("RightItem", "Item to put in your offhand when your holding right click and above health", Right.Gapple);

	private final Setting<Boolean> shift = new Setting<>("Shift", "Place a different item in your offhand when shifting.", true);
	private final Setting<Boolean> shiftSword = new Setting<>("SwordOnly", "Switch offhand only when you're holding a sword.", false);
	private final Setting<Right> shiftItem = new Setting<>("ShiftItem", "Item to put in your offhand when you're shifting and above health", Right.Potion);

	private final Setting<Boolean> shiftRight = new Setting<>("ShiftRight", "Place a different item in your offhand when shifting and holding right click.", true);
	private final Setting<Boolean> shiftRightSword = new Setting<>("SwordOnly", "Switch offhand only when you're holding a sword.", false);
	private final Setting<Right> shiftRightItem = new Setting<>("ShiftRightItem", "Item to put in your offhand when you're shifting, holding right click and above health", Right.Bow);


	private enum Normal {
		Crystal,
		Gapple,
		Bed,
		Experience,
	}

	private enum Right {
		Gapple,
		Potion,
		Bow,
		Experience,
	}
}
