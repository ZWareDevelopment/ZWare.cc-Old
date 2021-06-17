package dev.zihasz.zware.mixin.render;

import dev.zihasz.zware.ZWare;
import dev.zihasz.zware.event.EventState;
import dev.zihasz.zware.event.events.TransformEvent;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

	@Inject(method = "transformFirstPerson", at = @At("HEAD"), cancellable = true)
	public void transformFirstPerson(EnumHandSide hand, float p_187453_2_, CallbackInfo ci) {
		TransformEvent.FirstPerson event = new TransformEvent.FirstPerson(EventState.PRE, hand);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled())
			ci.cancel();
	}

	@Inject(method = "transformSideFirstPerson", at = @At("HEAD"), cancellable = true)
	public void transformSideFirstPerson(EnumHandSide hand, float p_185749_2_, CallbackInfo ci) {
		TransformEvent.FirstPerson.Side event = new TransformEvent.FirstPerson.Side(EventState.PRE, hand);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled())
			ci.cancel();
	}

	@Inject(method = "transformEatFirstPerson", at = @At("HEAD"), cancellable = true)
	public void transformEatFirstPerson(float p_187454_1_, EnumHandSide hand, ItemStack stack, CallbackInfo ci) {
		TransformEvent.FirstPerson.Eat event = new TransformEvent.FirstPerson.Eat(EventState.PRE, hand, stack);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled())
			ci.cancel();
	}

}
