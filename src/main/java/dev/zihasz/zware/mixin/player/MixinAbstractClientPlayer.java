package dev.zihasz.zware.mixin.player;

import com.mojang.authlib.GameProfile;
import dev.zihasz.zware.ZWare;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends EntityPlayer {

	public MixinAbstractClientPlayer(World worldIn, GameProfile gameProfileIn) {
		super(worldIn, gameProfileIn);
	}

	/*
	@Shadow @Nullable
	protected abstract NetworkPlayerInfo getPlayerInfo();

	@Inject(method = "getLocationCape()Lnet/minecraft/util/ResourceLocation;", at = @At(value = "RETURN"), cancellable = true)
	public void getCape(CallbackInfoReturnable<ResourceLocation> cir) {
		UUID uuid = Objects.requireNonNull(getPlayerInfo()).getGameProfile().getId();

		if (ZWare.capeManager.hasCape(uuid)) {
			cir.setReturnValue(ZWare.capeManager.getCapeForUUID(uuid));
		}
	}
    */

}