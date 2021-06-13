package dev.zihasz.zware.features.module.combat;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.features.setting.SubSetting;
import dev.zihasz.zware.utils.player.InventoryUtils;
import dev.zihasz.zware.utils.render.Rainbow;
import dev.zihasz.zware.utils.render.Renderer3D;
import dev.zihasz.zware.utils.world.BlockUtils;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockWeb;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.awt.*;

public class SelfWeb extends Module {

	public SelfWeb() {
		super("SelfWeb", "Place a web at your feet", Category.COMBAT);
	}

	private final Setting<Boolean> up = new Setting<>("Double", "Place a web at your head too.", true);
	private final Setting<Boolean> toggle = new Setting<>("Toggle", "Toggle off after placing.", true);
	private final Setting<Boolean> smart = new Setting<>("Smart", "Smart range option", false);
	// private final Setting<Float> smartRange = new Setting<>("Smart Range", "The range for smart option", 2f, 0f, 7f);
	// private final Setting<Float> targetRange = new Setting<>("Target Range", "The range for targeting.", 10f, 0f, 20f);

	private final Setting<Boolean> render = new Setting<>("Render", "Render the current place position.", true);
	private final SubSetting<Color> color = new SubSetting<>(render, "Color", "The color of the render.", new Color(255, 255, 255, 127));
	private final SubSetting<Boolean> rainbowColor = new SubSetting<>(render, "Rainbow", "Should we use rainbow color.", false);
	private final SubSetting<Boolean> fill = new SubSetting<>(render, "Fill", "Render the box of the block.", true);
	private final SubSetting<Boolean> outline = new SubSetting<>(render, "Outline", "Render the outline of the block.", true);
	private final SubSetting<Float> width = new SubSetting<>(render, "LineWidth", "The width of the outline.", 1f, 0f, 5f);
	private final SubSetting<Float> alpha = new SubSetting<>(render, "LineAlpha", "The alpha of the outline.", 255f, 0f, 255f);

	private int oldSlot = -1;
	private BlockPos renderPos = null;
	private final Rainbow rainbow = new Rainbow();

	@Override
	public void onEnable() {
		if (mc.world.getBlockState(mc.player.getPosition()).getBlock() != Blocks.AIR) {
			toggle();
			return;
		}
		oldSlot = mc.player.inventory.currentItem;
	}

	@Override
	public void onUpdate() {
		BlockPos position = new BlockPos(Math.ceil(mc.player.posX), Math.ceil(mc.player.posY), Math.ceil(mc.player.posZ));
		BlockPos boost = position.add(0, 1, 0);

		if (InventoryUtils.findHotbarBlock(BlockWeb.class) == -1) {
			mc.player.sendMessage(new TextComponentString("No webs found!"));
			toggle();
			return;
		}

		InventoryUtils.switchToSlot(InventoryUtils.findHotbarBlock(BlockObsidian.class), true, true);

		renderPos = position;
		BlockUtils.placeBlock(position, EnumHand.MAIN_HAND, true, true);
		if (up.getValue()) {
			renderPos = boost;
			BlockUtils.placeBlock(boost, EnumHand.MAIN_HAND, true, true);
		}
		renderPos = null;

		InventoryUtils.switchToSlot(oldSlot, true, true);
		if (toggle.getValue()) toggle();
	}

	@Override
	public void onRender3D(RenderWorldLastEvent event) {
		if (renderPos == null) return;

		AxisAlignedBB bb = new AxisAlignedBB(renderPos);

		Color fillColor = rainbowColor.getValue() ? rainbow.getColor() : color.getValue();
		Color outlineColor = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), alpha.getValue());

		if (fill.getValue()) Renderer3D.drawBBFill(bb, fillColor);
		if (outline.getValue()) Renderer3D.drawBBOutline(bb, width.getValue(), outlineColor);
	}
}