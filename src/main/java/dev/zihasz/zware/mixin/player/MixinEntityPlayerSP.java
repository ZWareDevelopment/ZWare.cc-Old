package dev.zihasz.zware.mixin.player;

import com.mojang.authlib.GameProfile;
import dev.zihasz.zware.event.EventState;
import dev.zihasz.zware.event.events.PlayerEvent;
import dev.zihasz.zware.event.events.PushEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.MoverType;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public class MixinEntityPlayerSP extends AbstractClientPlayer {

	public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
		super(worldIn, playerProfile);
	}

	@Inject(method = "onUpdateWalkingPlayer()V", at = @At("HEAD"), cancellable = true)
	public void onUpdateWalkingPlayer(CallbackInfo info) {
		PlayerEvent.Update.Walking event = new PlayerEvent.Update.Walking(EventState.PRE);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled())
			info.cancel();
	}

	@Redirect(method = "move(Lnet/minecraft/entity/MoverType;DDD)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
	public void move(AbstractClientPlayer player, MoverType type, double x, double y, double z) {
		PlayerEvent.Move event = new PlayerEvent.Move(EventState.PRE, type, x, y, z);
		MinecraftForge.EVENT_BUS.post(event);

		if (!event.isCanceled())
			super.move(type, x, y, z);
		else
			super.move(type, event.x, event.y, event.z);
	}

	@Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
	public void pushOutOfBlocks(double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
		PushEvent.Block event = new PushEvent.Block(EventState.PRE);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled())
			cir.cancel();
	}

}
