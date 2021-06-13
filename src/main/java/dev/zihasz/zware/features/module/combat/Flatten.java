package dev.zihasz.zware.features.module.combat;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.manager.RelationManager;
import dev.zihasz.zware.utils.player.InventoryUtils;
import dev.zihasz.zware.utils.world.BlockUtils;
import net.minecraft.block.BlockObsidian;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Flatten extends Module {

	private final Setting<Integer> blocksPerTick = new Setting<>("BlocksPerTick", "", 8, 1, 30);
	private final Setting<Boolean> rotate = new Setting<>("Rotate", "", false);
	private final Setting<Boolean> packet = new Setting<>("PacketPlace", "", false);
	private final Setting<Boolean> autoDisable = new Setting<>("AutoDisable", "", true);

	private final Vec3d[] offsetsDefault = new Vec3d[]{
			new Vec3d(0.0, 0.0, -1.0),
			new Vec3d(0.0, 0.0, 1.0),
			new Vec3d(1.0, 0.0, 0.0),
			new Vec3d(-1.0, 0.0, 0.0),
	};

	private int offsetStep = 0;
	private int oldSlot = -1;

	public Flatten() {
		super("Flatten", "Flatter than 19xp's 14yr old girlfriend", Category.COMBAT);
	}

	@Override
	public void onEnable() {
		oldSlot = mc.player.inventory.currentItem;
	}

	@Override
	public void onDisable() {
		oldSlot = -1;
	}

	@Override
	public void onUpdate() {
		EntityPlayer closest_target = findClosestTarget();

		int obbySlot = InventoryUtils.findHotbarBlock(BlockObsidian.class);

		if (closest_target == null) {
			this.disable();
			return;
		}

		final List<Vec3d> place_targets = new ArrayList<>();
		Collections.addAll(place_targets, offsetsDefault);

		int blocks_placed = 0;
		while (blocks_placed < blocksPerTick.getValue()) {
			if (offsetStep >= place_targets.size()) {
				offsetStep = 0;
				break;
			}
			final BlockPos offset_pos = new BlockPos(place_targets.get(offsetStep));
			final BlockPos target_pos = new BlockPos(closest_target.getPositionVector()).down().add(offset_pos.getX(), offset_pos.getY(), offset_pos.getZ());
			boolean should_try_place = mc.world.getBlockState(target_pos).getMaterial().isReplaceable();

			for (final Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(target_pos))) {
				if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
					should_try_place = false;
					break;
				}
			}

			if (should_try_place) {
				place(target_pos, obbySlot, oldSlot);
				++blocks_placed;
			}
			offsetStep++;
		}

		if (autoDisable.getValue()) {
			disable();
		}
	}

	private void place(BlockPos pos, int slot, int oldSlot) {
		mc.player.inventory.currentItem = slot;
		mc.playerController.updateController();
		BlockUtils.placeBlock(pos, EnumHand.MAIN_HAND, this.rotate.getValue(), this.packet.getValue());
		mc.player.inventory.currentItem = oldSlot;
		mc.playerController.updateController();
	}

	private EntityPlayer findClosestTarget() {
		if (mc.world.playerEntities.isEmpty())
			return null;
		EntityPlayer closestTarget = null;
		for (final EntityPlayer target : mc.world.playerEntities) {
			if (target == mc.player || !target.isEntityAlive())
				continue;
			if (RelationManager.isFriend(target.getGameProfile().getId()))
				continue;
			if (target.getHealth() <= 0.0f)
				continue;
			if (mc.player.getDistance(target) > 5)
				continue;
			if (closestTarget != null)
				if (mc.player.getDistance(target) > mc.player.getDistance(closestTarget))
					continue;
			closestTarget = target;
		}
		return closestTarget;
	}

}