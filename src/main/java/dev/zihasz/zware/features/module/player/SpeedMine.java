package dev.zihasz.zware.features.module.player;

import dev.zihasz.zware.event.events.BlockEvent;
import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.features.setting.SubSetting;
import dev.zihasz.zware.utils.entity.EntityUtils;
import dev.zihasz.zware.utils.misc.ColorUtils;
import dev.zihasz.zware.utils.render.Renderer3D;
import dev.zihasz.zware.utils.types.Pair;
import dev.zihasz.zware.utils.world.WorldUtils;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SpeedMine extends Module {

	private final Setting<Mode> modeSetting = new Setting<>("Mode", "The speed mine mode.", Mode.PACKET);
	private final Setting<Float> range = new Setting<>("Range", "Range to cancel breaking the block at.", 5f, 0f, 7f);
	private final Setting<Float> startDamage = new Setting<>("StartDamage", "The damage to start breaking the block at.", 0f, 0f, 1f, v -> modeSetting.getValue() == Mode.DAMAGE);
	private final Setting<Float> stopDamage = new Setting<>("StopDamage", "The damage to stop breaking the block at.", .7f, 0f, 1f, v -> modeSetting.getValue() == Mode.DAMAGE);
	private final Setting<Boolean> queue = new Setting<>("Queue", "Block break queue. (like Meteor)", true, v -> modeSetting.getValue() == Mode.PACKET);
	private final Setting<Integer> haste = new Setting<>("Haste", "Applies haste effect to make mining faster.", 1, 0, 2);

	private final Setting<Boolean> render = new Setting<>("Render", "Render the currently mined block", true);
	private final SubSetting<Boolean> outline = new SubSetting<>(render, "Outline", "Outline the render block.", true, v -> render.getValue());
	private final SubSetting<Color> outlineColor = new SubSetting<>(render, "Color", "The color of the outline.", new Color(255, 255, 255, 255), v -> outline.getValue());
	private final SubSetting<Float> outlineWidth = new SubSetting<>(render, "Width", "The width of the outline.", 1f, 0f, 5f, v -> outline.getValue());
	private final SubSetting<Boolean> fill = new SubSetting<>(render, "Fill", "Fill in the render block.", true, v -> render.getValue());
	private final SubSetting<Color> fillColor = new SubSetting<>(render, "Color", "The color of the fill.", new Color(255, 255, 255, 80), v -> fill.getValue());
	private final SubSetting<Boolean> renderQueue = new SubSetting<>(render, "RenderQueue", "Render the breaking queue's blocks.", true);
	private final SubSetting<Boolean> renderDamage = new SubSetting<>(render, "RenderDamage", "Render the block damage.", true, v -> modeSetting.getValue() == Mode.DAMAGE);

	private Pair<BlockPos, EnumFacing> block;
	private Queue<Pair<BlockPos, EnumFacing>> blockQueue = new ConcurrentLinkedQueue<>();


	public SpeedMine() {
		super("SpeedMine", "Mine blocks at the speed of sound. Enjoy breaking (haha pun :D) the sound barrier! :)", Category.PLAYER);
	}

	@Override
	public void onDisable() {
		block = null;
		blockQueue.clear();
	}

	@Override
	public void onUpdate() {
		if (nullCheck()) return;
		if (block == null) return;

		if (haste.getValue() != 0) {
			mc.player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 0, haste.getValue() - 1));
		}

		if (EntityUtils.getDistance(block.getKey()) > range.getValue()) {
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, block.getKey(), block.getVal()));
			block = blockQueue.isEmpty() ? null : blockQueue.poll();
		}

		if (modeSetting.getValue() == Mode.BREAKER) {
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, block.getKey(), block.getVal()));
		}

	}

	@Override
	public void onRender3D(RenderWorldLastEvent event) {
		if (nullCheck()) return;
		if (block == null) return;

		if (render.getValue()) {
			if (modeSetting.getValue() != Mode.DAMAGE) {
				if (fill.getValue()) Renderer3D.drawBBFill(new AxisAlignedBB(block.getKey()), fillColor.getValue());
				if (outline.getValue())
					Renderer3D.drawBBOutline(new AxisAlignedBB(block.getKey()), outlineWidth.getValue(), outlineColor.getValue());
			} else if (renderDamage.getValue()) {
				Renderer3D.drawTextFromBlock(block.getKey(), new DecimalFormat("#.#").format(mc.playerController.curBlockDamageMP), 0xffffffff, 1f);
			}

			if (renderQueue.getValue()) {
				for (Pair<BlockPos, EnumFacing> pair : blockQueue) {
					if (modeSetting.getValue() != Mode.DAMAGE) {
						if (fill.getValue()) Renderer3D.drawBBFill(new AxisAlignedBB(pair.getKey()), ColorUtils.changeAlpha(fillColor.getValue(), 100));
						if (outline.getValue())
							Renderer3D.drawBBOutline(new AxisAlignedBB(pair.getKey()), outlineWidth.getValue(), ColorUtils.changeAlpha(outlineColor.getValue(), 100));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onBlockClick(BlockEvent.Click event) {
		if (!WorldUtils.canBreak(event.getBlockPos())) return;

		if (block == null)
			block = new Pair<>(event.getBlockPos(), event.getFace());
		else if (queue.getValue() && modeSetting.getValue() == Mode.PACKET)
			blockQueue.add(new Pair<>(event.getBlockPos(), event.getFace()));

		if (modeSetting.getValue() == Mode.DAMAGE)
			mc.playerController.curBlockDamageMP = startDamage.getValue();
		else {
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, block.getKey(), block.getVal()));
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, block.getKey(), block.getVal()));
			if (modeSetting.getValue() == Mode.INSTANT)
				mc.world.setBlockToAir(block.getKey());
		}
	}

	@SubscribeEvent
	public void onBlockDamage(BlockEvent.Damage event) {
		if (modeSetting.getValue() == Mode.DAMAGE && mc.playerController.curBlockDamageMP >= stopDamage.getValue()) {
			mc.playerController.curBlockDamageMP = 1f;
		}
	}

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.Break event) {
		if (event.getBlockPos() == block.getKey())
			block = blockQueue.isEmpty() ? null : blockQueue.poll();
	}

	private enum Mode {
		DAMAGE,
		PACKET,
		INSTANT,
		BREAKER,
	}

}