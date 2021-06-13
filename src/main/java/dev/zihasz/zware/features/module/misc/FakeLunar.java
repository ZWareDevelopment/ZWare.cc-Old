package dev.zihasz.zware.features.module.misc;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.manager.ModuleManager;

public class FakeLunar extends Module {

	public FakeLunar() {
		super("FakeLunar", "Fakes lunar client.", Category.MISC);
	}

	@Override
	public void onEnable() {
		ModuleManager.getModule(FakeVanilla.class).enable();
	}
}
