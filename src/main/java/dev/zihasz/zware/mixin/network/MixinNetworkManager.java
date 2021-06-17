package dev.zihasz.zware.mixin.network;

import dev.zihasz.zware.event.EventState;
import dev.zihasz.zware.event.events.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkManager.class)
public class MixinNetworkManager {

	@Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void sendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
		PacketEvent event = new PacketEvent.Send(packet, EventState.PRE);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled()) callbackInfo.cancel();
	}

	@Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
	private void channelRead0(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
		PacketEvent event = new PacketEvent.Read(packet, EventState.PRE);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled()) callbackInfo.cancel();
	}

}