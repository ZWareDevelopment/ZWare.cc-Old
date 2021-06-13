package dev.zihasz.zware.mixin.network;

import dev.zihasz.zware.features.module.misc.FakeLunar;
import dev.zihasz.zware.features.module.misc.FakeVanilla;
import dev.zihasz.zware.manager.ModuleManager;
import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientBrandRetriever.class)
public class MixinClientBrandRetriever {

	@Inject(method = "", at = @At("RETURN"), cancellable = true)
	public void getClientModName(CallbackInfoReturnable<String> cir) {
		if (ModuleManager.getModule(FakeVanilla.class).isEnabled() || ModuleManager.getModule(FakeLunar.class).isEnabled()) {
			cir.setReturnValue("vanilla");
			System.out.println("Spoofed ClientBrandRetriever");
		}
	}

}
