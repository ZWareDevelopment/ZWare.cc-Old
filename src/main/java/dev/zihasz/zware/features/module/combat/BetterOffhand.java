package dev.zihasz.zware.features.module.combat;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.utils.misc.TimeUtils;
import dev.zihasz.zware.utils.misc.Timer;
import dev.zihasz.zware.utils.player.InventoryUtils;
import dev.zihasz.zware.utils.player.PlayerUtils;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BetterOffhand extends Module {

	private final Setting<Float> health = new Setting<>("Health", "The health to put a totem in your offhand.", 10f, 0f, 36f);
	private final Setting<Float> delay = new Setting<>("Delay", "Delay between switches", 0f, 0f, 500f);
	private final Setting<Normal> item = new Setting<>("Item", "Item to put in your offhand when above the specified health.", Normal.Crystal);
	private final Setting<Boolean> right = new Setting<>("SwordGap", "Put a gap in your offhand when holding a sword and right click.", true);
	private Timer timer = new Timer();

	public BetterOffhand() {
		super("BetterOffhand", "not ptsd", Category.COMBAT);
	}

	@Override
	public void onUpdate() {
		if (timer.passedMS(delay.getValue().longValue())) {
			if (PlayerUtils.getHealth() > health.getValue()) {
				if (mc.player.getHeldItemMainhand().getItem() == modeToItem(item.getValue())) return;

				int slot = InventoryUtils.findInventory(modeToItem(item.getValue()));
				int gapple = InventoryUtils.findInventory(Items.GOLDEN_APPLE);

				if (right.getValue() && mc.gameSettings.keyBindUseItem.isKeyDown() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD) {
					InventoryUtils.switchOffhand(gapple);
				} else {
					InventoryUtils.switchOffhand(slot);
				}
			} else {
				if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) return;

				int totem = InventoryUtils.findInventory(Items.TOTEM_OF_UNDYING);
				InventoryUtils.switchOffhand(totem);
			}
			timer.reset();
		}
	}

	@Override
	public String getInfo() {
		int totems = mc.player.inventory.mainInventory.stream()
				.filter(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING)
				.mapToInt(ItemStack::getCount)
				.sum();
		return String.valueOf(totems);
	}

	private Item modeToItem(Normal mode) {
		switch (mode) {
			case Crystal:
				return Items.END_CRYSTAL;
			case Gapple:
				return Items.GOLDEN_APPLE;
			case Bed:
				return Items.BED;
			case Experience:
				return Items.EXPERIENCE_BOTTLE;
			default:
				return null;
		}
	}

	private enum Normal {
		Crystal,
		Gapple,
		Bed,
		Experience,
	}

}
