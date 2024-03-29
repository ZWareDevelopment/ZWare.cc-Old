package dev.zihasz.zware.mixin;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

public class MixinLoader implements IFMLLoadingPlugin {

	private static boolean obfuscated = false;

	public MixinLoader() {
		MixinBootstrap.init();
		Mixins.addConfiguration("mixins.zware.json");
		MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
	}

	@Override
	public String[] getASMTransformerClass() { return new String[0]; }

	@Override
	public String getModContainerClass() { return null; }

	@Nullable
	@Override
	public String getSetupClass() { return null; }

	@Override
	public void injectData(Map<String, Object> data) { obfuscated = (boolean) data.get("runtimeDeobfuscationEnabled"); }

	@Override
	public String getAccessTransformerClass() { return null; }

}
