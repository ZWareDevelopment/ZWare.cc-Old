package dev.zihasz.zware.features.module.player;

import dev.xdark.ssbus.Listener;
import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall extends Module {

	public NoFall() {
		super("NoFall", "Take no fall damage like a god.", Category.PLAYER);
	}

	@Listener
	public void onPackerSend(PacketEvent.Read event) {
		if (event.getPacket() instanceof CPacketPlayer && mc.player.fallDistance >= 3.0) {
			CPacketPlayer packet = (CPacketPlayer) event.getPacket();
			packet.onGround = true;
		}
	}

}

