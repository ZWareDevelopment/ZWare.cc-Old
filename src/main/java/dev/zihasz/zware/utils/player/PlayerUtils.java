package dev.zihasz.zware.utils.player;

import dev.zihasz.zware.utils.Util;
import dev.zihasz.zware.utils.math.MathUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class PlayerUtils implements Util {

	public static BlockPos getPlayerPos() {
		return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
	}

	public static boolean canSeeBlock(BlockPos pos) {
		return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) == null;
	}

	public static Vec3d getEyesPos() { return new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ); }
	public static float[] getLegitRotations(Vec3d vec) {
		Vec3d eyesPos = getEyesPos();
		double diffX = vec.x - eyesPos.x;
		double diffY = vec.y - eyesPos.y;
		double diffZ = vec.z - eyesPos.z;
		double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

		float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
		float pitch = (float) -Math.toDegrees(Math.atan2(diffY, diffXZ));

		return new float[]{
				mc.player.rotationYaw + MathUtils.wrapDegrees(yaw - mc.player.rotationYaw),
				mc.player.rotationPitch + MathUtils.wrapDegrees(pitch - mc.player.rotationPitch)
		};
	}
	public static void faceVector(Vec3d vec, boolean normalizeAngle) {
		float[] rotations = PlayerUtils.getLegitRotations(vec);
		mc.player.connection.sendPacket(new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? MathUtils.normalizeAngle((int) rotations[1], 360) : rotations[1], mc.player.onGround));
	}

	public static float getHealth() {
		return mc.player.getHealth() + mc.player.getAbsorptionAmount();
	}

	private static int getPing() {
		if (mc.player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(mc.player.getName()) == null) {
			return -1;
		} else {
			return Objects.requireNonNull(mc.getConnection().getPlayerInfo(mc.player.getName())).getResponseTime();
		}
	}
	private static int getPing(EntityPlayer player) {
		if (player == null || mc.getConnection() == null || mc.getConnection().getPlayerInfo(player.getName()) == null) {
			return -1;
		} else {
			return Objects.requireNonNull(mc.getConnection().getPlayerInfo(player.getName())).getResponseTime();
		}
	}

	public static double getDiffX() {
		return mc.player.posX - mc.player.lastTickPosX;
	}
	public static double getDiffY() {
		return mc.player.posY - mc.player.lastTickPosY;
	}
	public static double getDiffZ() {
		return mc.player.posZ - mc.player.lastTickPosZ;
	}
	public static Vec3d getDiff() {
		return new Vec3d(getDiffX(), getDiffY(), getDiffZ());
	}

}
