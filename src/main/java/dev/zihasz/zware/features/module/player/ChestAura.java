package dev.zihasz.zware.features.module.player;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
public class ChestAura extends Module {

	public ChestAura() {
		super("ChestAura", "Opens chests around you. duh.", Category.PLAYER);
	}

	private final Setting<Boolean> packet = new Setting<>("Packet", "\"You have been kicked for packet exploits. (NOTE: This is just a joke.)\"", true);
	private final Setting<Boolean> shulkerOnly = new Setting<>("ShulkerOnly", "only shulkers", true);
	private final Setting<Double> range = new Setting<>("Range", "max range", 4.5D, 0.0D, 5.0D);

	@Override
	public void onUpdate() {
		TileEntityShulkerBox box = (TileEntityShulkerBox) mc.world.loadedTileEntityList.stream()
				.filter(tileEntity -> !shulkerOnly.getValue() || tileEntity instanceof TileEntityShulkerBox)
				.filter(tileEntity -> mc.player.getDistance(tileEntity.getPos().x, tileEntity.getPos().y, tileEntity.getPos().z) < range.getValue())
				.sorted(Comparator.comparing(tileEntity ->
						mc.player.getDistance(
								tileEntity.getPos().x,
								tileEntity.getPos().y,
								tileEntity.getPos().z)))
				.findFirst()
				.orElse(null);
		if (box != null) {
			RayTraceResult rt = mc.world.rayTraceBlocks(
					new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ),
					new Vec3d(box.getPos().getX() + 0.5, box.getPos().getY() + 0.5, box.getPos().getZ() + 0.5)
			);
			if (!packet.getValue())
				mc.playerController.processRightClickBlock(
						mc.player,
						mc.world,
						box.getPos(),
						getEnumFacing(true, box.getPos()),
						rt.hitVec,
						EnumHand.MAIN_HAND);
			else
				mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(
						box.getPos(),
						getEnumFacing(true, box.getPos()),
						EnumHand.MAIN_HAND,
						(float) rt.hitVec.x,
						(float) rt.hitVec.y,
						(float) rt.hitVec.z));
		}
	}

	private EnumFacing getEnumFacing(boolean rayTrace, BlockPos placePosition) {
		RayTraceResult result = mc.world.rayTraceBlocks(
				new Vec3d(
						mc.player.posX,
						mc.player.posY + mc.player.getEyeHeight(),
						mc.player.posZ),
				new Vec3d(
						placePosition.getX() + 0.5,
						placePosition.getY() + 0.5,
						placePosition.getZ() + 0.5)
		);

		if (placePosition.getY() == 255)return EnumFacing.DOWN;
		if (rayTrace) return (result == null || result.sideHit == null) ? EnumFacing.UP : result.sideHit;

		return EnumFacing.UP;
	}

}
