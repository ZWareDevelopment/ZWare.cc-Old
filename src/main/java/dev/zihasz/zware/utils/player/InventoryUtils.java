package dev.zihasz.zware.utils.player;

import dev.zihasz.zware.utils.Util;
import net.minecraft.block.Block;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtils implements Util {

	public static int find(Item item) {
		int b = -1;
		for (int a = 0; a < 9; a++) {
			if (mc.player.inventory.getStackInSlot(a).getItem() == item) {
				b = a;
			}
		}
		return b;
	}
	public static int find(Class<? extends Item> clazz) {
		int b = -1;
		for (int a = 0; a < 9; a++) {
			if (mc.player.inventory.getStackInSlot(a).getItem().getClass().equals(clazz)) {
				b = a;
			}
		}
		return b;
	}
	public static int findFirst(Item item) {
		int b = -1;
		for (int a = 0; a < 9; a++) {
			if (mc.player.inventory.getStackInSlot(a).getItem() == item) {
				b = a;
				break;
			}
		}
		return b;
	}
	public static int findFirst(Class<? extends Item> clazz) {
		int b = -1;
		for (int a = 0; a < 9; a++) {
			if (mc.player.inventory.getStackInSlot(a).getItem().getClass().equals(clazz)) {
				b = a;
				break;
			}
		}
		return b;
	}

	public static int findHotbarBlock(Block block) {
		for (int i = 0; i < 9; i++) {
			Item item = mc.player.inventory.getStackInSlot(i).getItem();
			if (item instanceof ItemBlock && ((ItemBlock) item).getBlock().equals(block))
				return i;
		}

		return -1;
	}
	public static int findHotbarBlock(Class clazz) {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = mc.player.inventory.getStackInSlot(i);
			if (stack == ItemStack.EMPTY) {
				continue;
			}

			if (clazz.isInstance(stack.getItem())) {
				return i;
			}

			if (stack.getItem() instanceof ItemBlock) {
				Block block = ((ItemBlock) stack.getItem()).getBlock();
				if (clazz.isInstance(block)) {
					return i;
				}
			}
		}
		return -1;
	}

	public static int amountInHotbar(Item item) {
		return amountInHotbar(item, true);
	}
	public static int amountInHotbar(Item item, boolean offhand) {
		int quantity = 0;

		for (int i = 44; i > 35; i--) {
			ItemStack stackInSlot = mc.player.inventoryContainer.getSlot(i).getStack();
			if (stackInSlot.getItem() == item) quantity += stackInSlot.getCount();
		}

		if (mc.player.getHeldItemOffhand().getItem() == item && offhand)
			quantity += mc.player.getHeldItemOffhand().getCount();

		return quantity;
	}
	public static int amountBlockInHotbar(Block block) {
		return amountInHotbar(new ItemStack(block).getItem());
	}
	public static int amountBlockInHotbar(Block block, boolean offhand) {
		return amountInHotbar(new ItemStack(block).getItem(), offhand);
	}

	public static void switchToSlot(int slot, boolean packet, boolean updateController) {
		mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
		if (!packet) mc.player.inventory.currentItem = slot;
		if (updateController) mc.playerController.updateController();
	}
	public static void switchOffhand(int slot, int step) {
		if (slot == -1) return;
		if (step == 0) {
			mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
			mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
			mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
		}
		if (step == 1) {
			mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
		}
		if (step == 2) {
			mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
			mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
		}

		mc.playerController.updateController();
	}
	public static int findInventory(Item item) {
		int slot = -1;
		for (int i = 0; i < mc.player.inventory.mainInventory.size(); i++) {
			if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
				slot = i;
			}
		}
		return slot;
	}

}