package dev.zihasz.zware.features.module.misc;

import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.utils.misc.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ItemDelay extends Module {

	private final Setting<Float> delay = new Setting<>("Delay", "", 150f, 0f, 50000f);
	private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
	private final Timer timer = new Timer();

	public ItemDelay() {
		super("ItemDelay", "doesnt work just dopesnt let u use tings basically", Category.MISC);
	}

	@Override
	public void onUpdate() {
		if (timer.passedMS(delay.getValue().longValue()) && !packets.isEmpty()) {
			mc.player.connection.sendPacket(packets.poll());
			timer.reset();
		}
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		Packet<?> raw = event.getPacket();
		if (raw instanceof CPacketPlayerTryUseItem) {
			packets.add(raw);
			event.cancel();
		}
	}

	@Override
	public void onDisable() {
		for (Packet<?> packet : packets) {
			mc.player.connection.sendPacket(packet);
			timer.reset();
			packets.clear();
		}
	}
}
