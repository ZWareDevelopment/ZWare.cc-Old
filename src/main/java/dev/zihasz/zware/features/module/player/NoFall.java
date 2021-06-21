package dev.zihasz.zware.features.module.player;

import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NoFall extends Module {

	public NoFall() {
		super("NoFall", "Take no fall damage like a god.", Category.PLAYER);
	}

	private final Setting<Float> fallDistance = new Setting<>("Fall Distance", "Minimum fall distance to disable fall damage.", 3f, 0f, 100f);

	@SubscribeEvent
	public void onPackerSend(PacketEvent.Read event) {
		if (event.getPacket() instanceof CPacketPlayer && mc.player.fallDistance >= fallDistance.getValue()) {
			CPacketPlayer packet = (CPacketPlayer) event.getPacket();
			packet.onGround = true;
		}
	}

}

