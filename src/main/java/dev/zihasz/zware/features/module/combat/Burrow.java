package dev.zihasz.zware.features.module.combat;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.utils.player.InventoryUtils;
import dev.zihasz.zware.utils.world.BlockUtils;
import dev.zihasz.zware.utils.world.WorldUtils;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class Burrow extends Module {

	private final Setting<Float> offset = new Setting<>("Offset", "Offset", 7.0F, -20.0F, 20.0F);
	private final Setting<Boolean> sneak = new Setting<>("Sneak", "Sneak", true);
	private final Setting<Boolean> rotate = new Setting<>("Rotate", "Rotate", true);
	private final Setting<Boolean> packet = new Setting<>("Packet", "Packet", true);

	private BlockPos originalPos;
	private int oldSlot = -1;

	public Burrow() {
		super("Burrow", "ew delete minecraft rn", Category.COMBAT);
	}

	@Override
	public void onEnable() {
		super.onEnable();
		originalPos = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
		if (mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock().equals(Blocks.OBSIDIAN) ||
				WorldUtils.intersectsWithEntity(this.originalPos)) {
			toggle();
			return;
		}
		oldSlot = mc.player.inventory.currentItem;
	}

	@Override
	public void onUpdate() {
		if (InventoryUtils.findHotbarBlock(BlockObsidian.class) == -1) {
			mc.player.sendMessage(new TextComponentString("No obby found"));
			toggle();
			return;
		}

		InventoryUtils.switchToSlot(InventoryUtils.findHotbarBlock(BlockObsidian.class), true, true);
		fakeJump();
		BlockUtils.placeBlock(originalPos, EnumHand.MAIN_HAND, rotate.getValue(), packet.getValue());
		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + offset.getValue(), mc.player.posZ, false));
		if (sneak.getValue()) {
			mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
			mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		}
		InventoryUtils.switchToSlot(oldSlot, true, true);
		toggle();
	}

	private void fakeJump() {
		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.41999998688698D, mc.player.posZ, true));
		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.7531999805211997D, mc.player.posZ, true));
		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.00133597911214D, mc.player.posZ, true));
		mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.16610926093821D, mc.player.posZ, true));
	}

}
