package dev.zihasz.zware.utils.networking;

import dev.zihasz.zware.utils.Util;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public class PacketUtils implements Util {

	public static CPacketCustomPayload generatePayload(String channel, String message) {
		return new CPacketCustomPayload(channel, new PacketBuffer(Unpooled.buffer().writeBytes(message.getBytes())));
	}

}
