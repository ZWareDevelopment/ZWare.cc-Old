package dev.zihasz.zware.features.module.player;

import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiAim extends Module {

	public AntiAim() {
		super("AntiAim", "It's like we are in CS:GO!", Category.PLAYER);
	}

	private final Setting<Float> yaw = new Setting<>("Yaw", "Yaw of you when anti aiming", 0f, -180f, 180f);
	private final Setting<Float> pitch = new Setting<>("Pitch", "Pitch of you when anti aiming", 0f, -180f, 180f);

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		Packet<?> raw = event.getPacket();
		if (raw instanceof CPacketPlayer) {
			CPacketPlayer packet = (CPacketPlayer) raw;
			packet.rotating = true;
			packet.yaw = yaw.getValue();
			packet.pitch = pitch.getValue();
		}
	}

}
