package dev.zihasz.zware.utils.world;

import dev.zihasz.zware.utils.Util;
import dev.zihasz.zware.utils.player.PlayerUtils;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class BlockUtils implements Util {

	public static void placeBlock(BlockPos pos, EnumHand hand, boolean rotate, boolean packet) {
		boolean sneaking = false;
		EnumFacing side = WorldUtils.getSide(pos);
		if (side == null) {
			return;
		}

		BlockPos neighbour = pos.offset(side);
		EnumFacing opposite = side.getOpposite();

		Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));

		if (rotate) {
			PlayerUtils.faceVector(hitVec, true);
		}

		rightClickBlock(neighbour, hitVec, hand, opposite, packet);
		mc.player.swingArm(EnumHand.MAIN_HAND);
	}

	public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
		if (packet) {
			float f = (float) (vec.x - (double) pos.getX());
			float f1 = (float) (vec.y - (double) pos.getY());
			float f2 = (float) (vec.z - (double) pos.getZ());
			mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
		} else {
			mc.playerController.processRightClickBlock(mc.player, mc.world, pos, direction, vec, hand);
		}
		mc.player.swingArm(EnumHand.MAIN_HAND);
		mc.rightClickDelayTimer = 4; //?
	}

}
