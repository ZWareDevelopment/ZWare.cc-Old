package dev.zihasz.zware.features.module.movement;

import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Fly extends Module {

	private final Setting<Float> speedSetting = new Setting<>("", "", 10f, 0f, 20f);

	public Fly() {
		super("Fly", "Better than exhibition.", Category.MOVEMENT);
	}

	@Override
	public void onUpdate() {
		if (nullCheck()) return;
		mc.player.capabilities.setFlySpeed(speedSetting.getValue() == 0 ? mc.player.capabilities.getFlySpeed() : speedSetting.getValue());
	}

	@Override
	public void onEnable() {
		mc.player.capabilities.isFlying = true;
	}

	@Override
	public void onDisable() {
		mc.player.capabilities.isFlying = false;
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		if (event.getPacket() instanceof CPacketPlayer) {
			((CPacketPlayer) event.getPacket()).onGround = true;
		}
	}

}
