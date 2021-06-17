package dev.zihasz.zware.features.module.misc;

import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.utils.client.Message;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PacketLogger extends Module {

	public PacketLogger() {
		super("PacketLogger", "Logs outgoing packets", Category.MISC);
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		Packet<?> raw = event.getPacket();
		if (raw instanceof CPacketPlayer) {
			CPacketPlayer packet = (CPacketPlayer) raw; 
			Message.sendLogMessage("PacketPlayer: ");
			Message.sendLogMessage(String.format("  X: %s, Y: %s, Z: %s", packet.x, packet.y, packet.z));
			Message.sendLogMessage(String.format("  Yaw: %s, Pitch: %s", packet.yaw, packet.pitch) );
			Message.sendLogMessage(String.format("  OnGround: %s", packet.onGround));
		}
		if (raw instanceof CPacketPlayerAbilities) {
			CPacketPlayerAbilities packet = (CPacketPlayerAbilities) raw; 
			Message.sendLogMessage("PacketPlayerAbilities: ");
			Message.sendLogMessage(String.format("  AllowFly: %s, Flying: %s", packet.isAllowFlying(), packet.isFlying()));
			Message.sendLogMessage(String.format("  Invuln: %s, Creative: %s", packet.isInvulnerable(), packet.isCreativeMode()));

		}
		if (raw instanceof CPacketConfirmTransaction) {
			CPacketConfirmTransaction packet = (CPacketConfirmTransaction) raw; 
			Message.sendLogMessage("PacketConfirmTransaction: ");
			Message.sendLogMessage(String.format("  WID: %s, UID: %s", packet.getWindowId(), packet.getUid()));
		}
		if (raw instanceof CPacketConfirmTeleport) {
			CPacketConfirmTeleport packet = (CPacketConfirmTeleport) raw;
			Message.sendLogMessage("PacketConfirmTeleport: ");
			Message.sendLogMessage(String.format("  TID: %s", packet.getTeleportId()));
		}
	}

}
