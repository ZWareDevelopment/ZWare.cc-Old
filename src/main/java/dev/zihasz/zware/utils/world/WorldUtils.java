package dev.zihasz.zware.utils.world;

import dev.zihasz.zware.utils.Util;
import dev.zihasz.zware.utils.player.PlayerUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorldUtils implements Util {

	public static List<BlockPos> getSphere(BlockPos position, float radius, int height, boolean hollow, boolean sphere, int extraY) {
		ArrayList<BlockPos> circle = new ArrayList<>();
		int circleX = position.getX();
		int circleY = position.getY();
		int circleZ = position.getZ();
		int x = circleX - (int) radius;
		while ((float) x <= (float) circleX + radius) {
			int z = circleZ - (int) radius;
			while ((float) z <= (float) circleZ + radius) {
				int y = sphere ? circleY - (int) radius : circleY;
				while (true) {
					float f = y;
					float f2 = sphere ? (float) circleY + radius : (float) (circleY + height);
					if (!(f < f2)) break;
					double dist = (circleX - x) * (circleX - x) + (circleZ - z) * (circleZ - z) + (sphere ? (circleY - y) * (circleY - y) : 0);
					if (!(!(dist < (double) (radius * radius)) || hollow && dist < (double) ((radius - 1.0f) * (radius - 1.0f)))) {
						BlockPos blockPos = new BlockPos(x, y + extraY, z);
						circle.add(blockPos);
					}
					++y;
				}
				++z;
			}
			++x;
		}
		return circle;
	}

	public static List<EntityEnderCrystal> getAllCrystals() {
		return mc.world.loadedEntityList.stream()
				.filter(e -> e instanceof EntityEnderCrystal)
				.map(e -> (EntityEnderCrystal) e)
				.collect(Collectors.toList());
	}
	public static List<BlockPos> getPlacePositions(float range, boolean specialCheck, boolean fifteen) {
		NonNullList<BlockPos> positions = NonNullList.create();
		List<BlockPos> sphere = getSphere(PlayerUtils.getPlayerPos(), range, (int) range, false, true, 0);
		positions.addAll(sphere.stream().filter(pos -> canPlaceCrystal(pos, specialCheck, fifteen)).collect(Collectors.toList()));
		return positions;
	}

	public static boolean canPlaceCrystal(BlockPos position, boolean specialCheck, boolean thirteen) {
		BlockPos up = position.add(0, 1, 0);
		BlockPos up2 = position.add(0, 2, 0);
		try {
			if (!thirteen) {
				if (mc.world.getBlockState(position).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(position).getBlock() != Blocks.OBSIDIAN) {
					return false;
				}
				if (mc.world.getBlockState(up).getBlock() != Blocks.AIR || mc.world.getBlockState(up2).getBlock() != Blocks.AIR) {
					return false;
				}
				if (!specialCheck) {
					return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(up)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(up2)).isEmpty();
				}
				for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(up))) {
					if (entity instanceof EntityEnderCrystal) continue;
					return false;
				}
				for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(up2))) {
					if (entity instanceof EntityEnderCrystal) continue;
					return false;
				}
			} else {
				if (mc.world.getBlockState(position).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(position).getBlock() != Blocks.OBSIDIAN) {
					return false;
				}
				if (mc.world.getBlockState(up).getBlock() != Blocks.AIR) {
					return false;
				}
				if (!specialCheck) {
					return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(up)).isEmpty();
				}
				for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(up))) {
					if (entity instanceof EntityEnderCrystal) continue;
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean intersectsWithEntity(BlockPos pos) {
		for (Entity entity : mc.world.loadedEntityList) {
			if (entity.equals(mc.player)) continue;
			if (entity instanceof EntityItem) continue;
			if (new AxisAlignedBB(pos).intersects(entity.getEntityBoundingBox())) return true;
		}
		return false;
	}

	public static List<EnumFacing> getSides(BlockPos pos) {
		List<EnumFacing> facings = new ArrayList<>();
		for (EnumFacing side : EnumFacing.values()) {
			BlockPos neighbour = pos.offset(side);
			if (mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
				IBlockState blockState = mc.world.getBlockState(neighbour);
				if (!blockState.getMaterial().isReplaceable()) {
					facings.add(side);
				}
			}
		}
		return facings;
	}
	public static EnumFacing getSide(BlockPos pos) { return getSides(pos).stream().findFirst().orElse(null); }

	public static boolean canBreak(BlockPos pos) { return mc.world.getBlockState(pos).getBlockHardness(mc.world, pos) != -1; }

}
