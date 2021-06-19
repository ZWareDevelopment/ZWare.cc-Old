package dev.zihasz.zware.features.module.player;

import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.features.setting.SubSetting;
import dev.zihasz.zware.utils.misc.Timer;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FakeLag extends Module {

	private final Setting<Boolean> outgoing = new Setting<>("Outgoing", "Delay outgoing packets. (from client)", true);
	private final SubSetting<Float> outgoingDelay = new SubSetting<>(outgoing, "OutgoingDelay", "The amount to delay outgoing packets in ms.", 20f, 0f, 30000f);
	private final Setting<Boolean> incoming = new Setting<>("Incoming", "Delay incoming packets. (from server)", true);
	private final SubSetting<Float> incomingDelay = new SubSetting<>(incoming, "IncomingDelay", "The amount to delay incoming packets in ms.", 20f, 0f, 30000f);
	private final Timer incomingTimer = new Timer();
	private final Timer outgoingTimer = new Timer();
	private final Queue<Packet<?>> incomingQueue = new ConcurrentLinkedQueue<>();
	private final Queue<Packet<?>> outgoingQueue = new ConcurrentLinkedQueue<>();
	public FakeLag() {
		super("FakeLag", "Makes you look like your lagging", Category.PLAYER);
	}

	@Override
	public void onEnable() {
		incomingTimer.reset();
		outgoingTimer.reset();
	}

	@Override
	public void onUpdate() {
		if (incoming.getValue() && incomingTimer.passedMS((long) ((float) incomingDelay.getValue()))) {
			mc.player.connection.sendPacket(Objects.requireNonNull(incomingQueue.poll()));
			incomingTimer.reset();
		}
		if (outgoing.getValue() && outgoingTimer.passedMS((long) ((float) outgoingDelay.getValue()))) {
			mc.player.connection.sendPacket(Objects.requireNonNull(outgoingQueue.poll()));
			outgoingTimer.reset();
		}
	}

	@SubscribeEvent
	public void onPacketEvent(PacketEvent.Send event) {
		if (outgoing.getValue()) {
			event.cancel();
			outgoingQueue.add(event.getPacket());
		}
	}

	@SubscribeEvent
	public void onPacketEvent(PacketEvent.Read event) {
		if (incoming.getValue()) {
			event.cancel();
			incomingQueue.add(event.getPacket());
		}
	}

}
