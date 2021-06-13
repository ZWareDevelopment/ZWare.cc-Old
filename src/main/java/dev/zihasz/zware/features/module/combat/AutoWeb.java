package dev.zihasz.zware.features.module.combat;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.features.setting.SubSetting;
import dev.zihasz.zware.utils.misc.Timer;
import dev.zihasz.zware.utils.player.InventoryUtils;
import dev.zihasz.zware.utils.render.Renderer3D;
import dev.zihasz.zware.utils.world.BlockUtils;
import net.minecraft.block.BlockWeb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.awt.*;

public class AutoWeb extends Module {

	public AutoWeb() {
		super("AutoWeb", "WebGang owns all", Category.COMBAT);
	}

	private final Setting<Integer> delay = new Setting<>("Delay", "Delay between places", 50, 0, 250);
	private final Setting<Double> range = new Setting<>("Range", "The range to place webs at", 5.0d, 0d, 6.0d);
	private final Setting<Boolean> rotate = new Setting<>("Rotate", "Rotate to the target", true);
	private final Setting<Boolean> upper = new Setting<>("Upper", "Place webs at the upper part of the target body", true);

	private final Setting<Boolean> render = new Setting<>("Render", "Render the current place position.", true);
	private final SubSetting<Color> color = new SubSetting<>(render, "Color", "The color of the render.", new Color(255, 255, 255, 127));
	private final SubSetting<Boolean> fill = new SubSetting<>(render, "Fill", "Render the box of the block.", true);
	private final SubSetting<Boolean> outline = new SubSetting<>(render, "Outline", "Render the outline of the block.", true);
	private final SubSetting<Float> width = new SubSetting<>(render, "LineWidth", "The width of the outline.", 1f, 0f, 5f);
	private final SubSetting<Float> alpha = new SubSetting<>(render, "LineAlpha", "The alpha of the outline.", 255f, 0f, 255f);

	private EntityPlayer target = null;
	private BlockPos renderBlock = null;

	private final Timer timer = new Timer();

	@Override
	public void onEnable() {
		this.target = null;
		this.renderBlock = null;
		this.timer.reset();
	}

	@Override
	public void onUpdate() {

	}

	@Override
	public void onRender3D(RenderWorldLastEvent event) {
		if (!render.getValue() || renderBlock == null) return;

		AxisAlignedBB bb = new AxisAlignedBB(renderBlock);

		Color fillColor = color.getValue();
		Color outlineColor = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), alpha.getValue());

		if (fill.getValue()) Renderer3D.drawBBFill(bb, fillColor);
		if (outline.getValue()) Renderer3D.drawBBOutline(bb, width.getValue(), outlineColor);
	}

	@Override
	public void onDisable() {

	}

	private void doAutoWeb() {

	}

	private void placeWeb(BlockPos position) {
		this.renderBlock = position;

		int oldSlot = mc.player.inventory.currentItem;
		int webSlot = InventoryUtils.findHotbarBlock(BlockWeb.class);

		InventoryUtils.switchToSlot(webSlot, true, true);
		BlockUtils.placeBlock(position, EnumHand.MAIN_HAND, rotate.getValue(), true);
		InventoryUtils.switchToSlot(oldSlot, true, true);
	}

}