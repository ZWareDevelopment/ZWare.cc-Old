package dev.zihasz.zware.mixin.player;

import dev.zihasz.zware.ZWare;
import dev.zihasz.zware.event.EventState;
import dev.zihasz.zware.event.events.WaterPushEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {

	public MixinEntityPlayer(World worldIn) {
		super(worldIn);
	}

	@Inject(method = "isPushedByWater", at = @At("HEAD"), cancellable = true)
	private void onPushedByWater(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
		WaterPushEvent event = new WaterPushEvent(EventState.PRE);
		ZWare.BUS.unsafeFireAndForget(event);
		if (event.isCanceled()) callbackInfoReturnable.setReturnValue(false);
	}

}
