package dev.zihasz.zware.features.module.combat;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.manager.ModuleManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import org.jetbrains.annotations.Nullable;

public class Offhand extends Module {

	private final Setting<Float> health = new Setting<>("Health", "The health to put a totem in your offhand.", 10f, 0f, 36f);

	private final Setting<Boolean> crystal = new Setting<>("Crystal Check", "Checks for mainhand crystal", true);

	private final Setting<Boolean> caCheck = new Setting<>("CA Check", "CrystalAura check", true);
	private final Setting<Boolean> forceCa = new Setting<>("Force CA", "Force crystals in offhand if CA is targeting", true);

	private final Setting<OffhandItem> itemN = new Setting<>("ItemN", "Offhand item normally", OffhandItem.Crystal);
	private final Setting<OffhandItem> itemR = new Setting<>("ItemR", "Offhand item when holding right click", OffhandItem.Gapple);
	private final Setting<OffhandItem> itemS = new Setting<>("ItemS", "Offhand item when sneaking", OffhandItem.Potion);
	private final Setting<OffhandItem> itemB = new Setting<>("ItemB", "Offhand item when sneaking and holding right clicking", OffhandItem.Bow);

	private Item item;

	public Offhand() {
		super("Offhand", "ptsd", Category.COMBAT);
	}

	@Override
	public void onUpdate() {
		if (nullCheck()) return;

		if (mc.player.isSneaking() && mc.gameSettings.keyBindUseItem.isKeyDown()) {
			item = getItem(itemB.getValue());
		} else if (mc.player.isSneaking()) {
			item = getItem(itemS.getValue());
		} else if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
			item = getItem(itemR.getValue());
		} else {
			item = getItem(itemN.getValue());
		}

		if ((mc.player.getHealth() + mc.player.getAbsorptionAmount() <= health.getValue() || !isMainHandTotem()) ||
				(item == Items.END_CRYSTAL && isMainHandCrystal() && crystal.getValue()) ||
				(item == Items.END_CRYSTAL && !ModuleManager.getModule(CrystalAura.class).isEnabled() && caCheck.getValue()) || item == null)
			item = Items.TOTEM_OF_UNDYING;

		if (ModuleManager.getModule(CrystalAura.class).isEnabled() && item != Items.END_CRYSTAL && forceCa.getValue())
			item = Items.END_CRYSTAL;

	}

	@Nullable
	private Item getItem(OffhandItem offhandItem) {
		switch (offhandItem) {
			case None:
				return null;
			case Totem:
				return Items.TOTEM_OF_UNDYING;
			case Crystal:
				return Items.END_CRYSTAL;
			case Gapple:
				return Items.GOLDEN_APPLE;
			case Bed:
				return Items.BED;
			case Experience:
				return Items.EXPERIENCE_BOTTLE;
			case Potion:
				return Items.POTIONITEM;
			case Bow:
				return Items.BOW;
		}
		return null;
	}

	@Override
	public String getInfo() {
		if (Items.TOTEM_OF_UNDYING.equals(item)) {
			return "Totem";
		} else if (Items.END_CRYSTAL.equals(item)) {
			return "Crystal";
		} else if (Items.GOLDEN_APPLE.equals(item)) {
			return "Gapple";
		} else if (Items.BED.equals(item)) {
			return "Bed";
		} else if (Items.EXPERIENCE_BOTTLE.equals(item)) {
			return "XP";
		} else if (Items.POTIONITEM.equals(item)) {
			return "Potion";
		} else if (Items.BOW.equals(item)) {
			return "Bow";
		} else {
			return "";
		}
	}

	private boolean isMainHandTotem() {
		return mc.player.getHeldItemMainhand().getItem() == Items.TOTEM_OF_UNDYING;
	}

	private boolean isMainHandCrystal() {
		return mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL;
	}

	private enum OffhandItem {
		None,
		Totem,
		Crystal,
		Gapple,
		Bed,
		Experience,
		Potion,
		Bow,
	}

}
