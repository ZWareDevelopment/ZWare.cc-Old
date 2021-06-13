package dev.zihasz.zware.event.events;

import dev.zihasz.zware.event.Event;
import dev.zihasz.zware.event.EventState;
import net.minecraft.network.Packet;

public abstract class PacketEvent extends Event {

	public Packet<?> packet;

	public PacketEvent(Packet<?> packet, EventState state) {
		super(state);
		this.packet = packet;
	}

	public Packet<?> getPacket() {
		return packet;
	}

	public static class Send extends PacketEvent {
		public Send(Packet<?> packet, EventState state) {
			super(packet, state);
		}
	}

	public static class Read extends PacketEvent {
		public Read(Packet<?> packet, EventState state) {
			super(packet, state);
		}
	}

}
