package dev.zihasz.zware.utils.world;

import dev.zihasz.zware.utils.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class Raytrace implements Util {

	public static RayTraceResult raytrace(EntityPlayer player, Entity entity) {
		return mc.world.rayTraceBlocks(player.getPositionVector(), entity.getPositionVector());
	}

	public static RayTraceResult raytrace(EntityPlayer player, BlockPos block) {
		Vec3d blockPosition = new Vec3d(block.x, block.y, block.z);
		return mc.world.rayTraceBlocks(player.getPositionVector(), blockPosition);
	}

}
