package dev.zihasz.zware.features.module.misc;

import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.manager.ModuleManager;
import dev.zihasz.zware.utils.networking.PacketUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class FakeLunar extends Module {

	public FakeLunar() {
		super("FakeLunar", "Fakes lunar client.", Category.MISC);
	}

	@Override
	public void onEnable() {
		ModuleManager.getModule(FakeVanilla.class).enable();
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		Packet<?> raw = event.getPacket();
		if(raw instanceof CPacketCustomPayload) {
			CPacketCustomPayload packet = (CPacketCustomPayload) raw;
			if(packet.getChannelName().equalsIgnoreCase("MC|Brand")) {
				mc.player.connection.sendPacket(PacketUtils.generatePayload("REGISTER", "Lunar-Client"));
			}
		}
	}

}
