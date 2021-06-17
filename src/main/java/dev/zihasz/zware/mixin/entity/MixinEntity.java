package dev.zihasz.zware.mixin.entity;

import dev.zihasz.zware.event.EventState;
import dev.zihasz.zware.event.events.EntityCollisionEvent;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class MixinEntity {

	@Inject(method = "applyEntityCollision", at = @At("HEAD"), cancellable = true)
	public void applyEntityCollision(Entity entityIn, CallbackInfo ci) {
		EntityCollisionEvent event = new EntityCollisionEvent(EventState.PRE);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled()) ci.cancel();
	}
}
