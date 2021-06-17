package dev.zihasz.zware.mixin.player;

import dev.zihasz.zware.ZWare;
import dev.zihasz.zware.event.EventState;
import dev.zihasz.zware.event.events.BlockEvent;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerControllerMP.class)
public class MixinPlayerControllerMP {

	@Inject(method = { "clickBlock" }, at = { @At("HEAD") }, cancellable = true)
	public void onPreClickBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> info) {
		BlockEvent.Click event = new BlockEvent.Click(posBlock, directionFacing, EventState.PRE);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled()) info.cancel();
	}

	@Inject(method = { "clickBlock" }, at = { @At("TAIL") }, cancellable = true)
	public void onPostClickBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> info) {
		BlockEvent.Click event = new BlockEvent.Click(posBlock, directionFacing, EventState.POST);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled()) info.cancel();
	}

	@Inject(method = { "onPlayerDamageBlock" }, at = { @At("HEAD") }, cancellable = true)
	public void onPreDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> info) {
		BlockEvent.Damage event = new BlockEvent.Damage(posBlock, directionFacing, EventState.PRE);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled()) info.cancel();
	}

	@Inject(method = { "onPlayerDamageBlock" }, at = { @At("TAIL") }, cancellable = true)
	public void onPostDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> info) {
		BlockEvent.Damage event = new BlockEvent.Damage(posBlock, directionFacing, EventState.POST);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled()) info.cancel();
	}

	@Inject(method = { "onPlayerDestroyBlock" }, at = { @At("HEAD") }, cancellable = true)
	public void onPreDestroyBlock(BlockPos posBlock, CallbackInfoReturnable<Boolean> info) {
		BlockEvent.Break event = new BlockEvent.Break(posBlock, EventState.PRE);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled()) info.cancel();
	}

	@Inject(method = { "onPlayerDestroyBlock" }, at = { @At("TAIL") }, cancellable = true)
	public void onPostDestroyBlock(BlockPos posBlock,CallbackInfoReturnable<Boolean> info) {
		BlockEvent.Break event = new BlockEvent.Break(posBlock, EventState.POST);
		MinecraftForge.EVENT_BUS.post(event);

		if (event.isCanceled()) info.cancel();
	}

}
