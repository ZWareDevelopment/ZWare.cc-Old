package dev.zihasz.zware.mixin.network;

import dev.zihasz.zware.features.module.misc.FakeLunar;
import dev.zihasz.zware.manager.ModuleManager;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.network.play.server.SPacketJoinGame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class MixinNetHandlerPlayClient {

	@Inject(method = "handleJoinGame", at = @At("HEAD"), cancellable = true)
	public void handleJoinGame(SPacketJoinGame packetJoinGame, CallbackInfo info) {
		if (ModuleManager.getModule(FakeLunar.class).isEnabled()) {
			Minecraft.getMinecraft().player.connection.sendPacket(new CPacketCustomPayload(
					"Lunar-Client",
					new PacketBuffer(Unpooled.buffer()).writeByteArray(Minecraft.getMinecraft().session.getPlayerID().getBytes())
			));
			System.out.println("Sent CPacketCustomPayload");
		}
	}

}
