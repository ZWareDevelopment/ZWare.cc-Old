package dev.zihasz.zware.features.module.render;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.utils.render.Quad;
import dev.zihasz.zware.utils.render.skeet.SkeetUtils;

public class Skeet extends Module {

	public Skeet() {
		super("Skeet", "Skeetless in 2021 smh", Category.RENDER);
	}

	@Override
	public void onRender2D() {
		SkeetUtils.renderSkeetBox(new Quad(5, 5, 50, 50));
	}

}
