package dev.zihasz.zware.features.module.player;

import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.utils.misc.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PingSpoof extends Module {

	public PingSpoof() {
		super("PingSpoof", "Make your ping look higher. (thx phobos)", Category.PLAYER);
	}

	private final Setting<Integer> delay = new Setting<>("Delay", "Delay in millis.", 20, 0, 1000);
	private final Queue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
	private final Timer timer = new Timer();
	private boolean receive = true;

	@Override
	public void onEnable() {
		this.clearQueue();
	}

	@Override
	public void onUpdate() {
		this.clearQueue();
	}

	@Override
	public void onDisable() {
		this.clearQueue();
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		if (this.receive && mc.player != null && !mc.isSingleplayer() && mc.player.isEntityAlive() && event.getPacket() instanceof CPacketKeepAlive) {
			this.packets.add(event.getPacket());
			event.cancel();
		}
	}

	public void clearQueue() {
		if (mc.player != null && !mc.isSingleplayer() && mc.player.isEntityAlive() && this.timer.passedMS(this.delay.getValue())) {
			int limit = (int) getIncremental(Math.random() * 10.0, 1.0);
			this.receive = false;
			for (int i = 0; i < limit; i++) {
				Packet<?> packet = this.packets.poll();
				if (packet != null) {
					mc.player.connection.sendPacket(packet);
				}
			}
			this.timer.reset();
			this.receive = true;
		}
	}

	public static double getIncremental(double val, double inc) {
		double one = 1.0 / inc;
		return (double)Math.round(val * one) / one;
	}

}
