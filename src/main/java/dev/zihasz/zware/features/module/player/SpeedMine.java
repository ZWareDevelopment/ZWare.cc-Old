package dev.zihasz.zware.features.module.player;

import dev.xdark.ssbus.Listener;
import dev.zihasz.zware.event.EventState;
import dev.zihasz.zware.event.events.BlockEvent;
import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.features.setting.SubSetting;
import dev.zihasz.zware.utils.render.Renderer3D;
import dev.zihasz.zware.utils.types.Pair;
import dev.zihasz.zware.utils.world.WorldUtils;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import java.awt.*;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SpeedMine extends Module {

	private final Setting<Mode> mode = new Setting<>("Mode", "mode bruh", Mode.PACKET);
	private final Setting<Float> startDamage = new Setting<>("DamageStart", "damage to start block at", 0.0f, 0f, 10f);
	private final Setting<Float> endDamage = new Setting<>("DamageEnd", "damage to end block at", 0.7f, 0f, 10f);
	private final Setting<Float> range = new Setting<>("Range", "Cancells block breaks if ur out of this range of cur block", 5f, 0f, 7f);
	private final Setting<Boolean> cancelAbort = new Setting<>("NoAbort", "Cancel abort packets, might be buggy added this for funis", false);
	private final Setting<Haste> haste = new Setting<>("Haste", "Option to add haste", Haste.TWO);
	private final Setting<Boolean> renderSetting = new Setting<>("Render", "Render current target block", true);
	private final SubSetting<Color> renderColor = new SubSetting<>(renderSetting, "Color", "Current target render color", new Color(255, 255, 255));
	private final SubSetting<Float> renderWidth = new SubSetting<>(renderSetting, "OutlineWidth", "Outline width, set to 0 for no outline", 1f, 0f, 5f);
	private final SubSetting<Boolean> renderDamage = new SubSetting<>(renderSetting, "Damage", "Render the block damage", true);
	private final SubSetting<Boolean> renderQueue = new SubSetting<>(renderSetting, "Queue", "Render the queued blocks", true);

	private BlockPos currentBlock;
	private EnumFacing currentFace;
	private Queue<Pair<BlockPos, EnumFacing>> breakQueue = new ConcurrentLinkedQueue<>();

	public SpeedMine() {
		super("SpeedMine", "Mine blocks at the speed of sound. Enjoy breaking the sound barrier! :)", Category.PLAYER);
	}

	@Override
	public void onEnable() {
		MinecraftForge.EVENT_BUS.register(this);
		breakQueue = new ConcurrentLinkedQueue<>();
		if (haste.getValue() != Haste.NONE)
			mc.player.addPotionEffect(new PotionEffect(MobEffects.HASTE, 255000, haste.getValue() == Haste.ONE ? 1 : 2));
	}

	@Override
	public void onDisable() {
		MinecraftForge.EVENT_BUS.unregister(this);
	}

	@Override
	public void onUpdate() {
		if (fullNullCheck()) return;

		if (currentBlock != null && mc.player.getDistanceSq(currentBlock) > range.getValue()) {
			currentBlock = null;
			currentFace = null;
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentBlock, currentFace));
		}

		if (mode.getValue() == Mode.BREAKER && WorldUtils.canBreak(currentBlock) && !mc.world.isAirBlock(currentBlock)) {
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentBlock, currentFace));
		}
	}

	@Override
	public void onRender3D(RenderWorldLastEvent event) {
		if (!renderSetting.getValue()) return;
		Renderer3D.drawBBFill(new AxisAlignedBB(currentBlock), renderColor.getValue());
		Renderer3D.drawBBOutline(new AxisAlignedBB(currentBlock), renderWidth.getValue(), renderColor.getValue());
		if (renderDamage.getValue() && mode.getValue() != Mode.DAMAGE)
			Renderer3D.drawTextFromBlock(mc.playerController.currentBlock, String.valueOf(mc.playerController.curBlockDamageMP), new Color(255, 255, 255, 255).getRGB(), 1f);
		if (renderQueue.getValue() && !breakQueue.isEmpty()) {
			for (Pair<BlockPos, EnumFacing> block : breakQueue) {
				Color color = new Color(renderColor.getValue().getRed(), renderColor.getValue().getGreen(), renderColor.getValue().getBlue(), renderColor.getValue().getAlpha() / 2);
				Renderer3D.drawBBFill(new AxisAlignedBB(currentBlock), color);
				Renderer3D.drawBBOutline(new AxisAlignedBB(currentBlock), renderWidth.getValue(), color);
			}
		}
	}

	@Listener
	public void onPacketSend(PacketEvent.Send event) {
		if (!event.getState().equals(EventState.PRE)) return;
		if (event.getPacket() instanceof CPacketPlayerDigging) {
			CPacketPlayerDigging packet = (CPacketPlayerDigging) event.getPacket();
			if (packet.getAction() == CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK && cancelAbort.getValue())
				event.cancel();
		}
	}

	@Listener
	public void onBlockClick(BlockEvent.Click event) {
		if (currentBlock == null) {
			currentBlock = event.getBlockPos();
			currentFace = event.getFace();
		} else
			breakQueue.add(new Pair<>(event.getBlockPos(), event.getFace()));


		if (mode.getValue() == Mode.DAMAGE) {
			mc.playerController.curBlockDamageMP = startDamage.getValue();
		} else {
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, currentBlock, currentFace));
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, currentBlock, currentFace));

			if (mode.getValue() == Mode.INSTANT) {
				mc.world.setBlockToAir(currentBlock);
			}
		}
	}

	@Listener
	public void onBlockDamage(BlockEvent.Damage event) {
		if (mode.getValue() == Mode.DAMAGE) {
			if (mc.playerController.curBlockDamageMP >= endDamage.getValue()) {
				mc.playerController.curBlockDamageMP = 1f;
			}
		}
	}

	@Listener
	public void onBlockBreak(BlockEvent.Break event) {
		if (event.getBlockPos() == currentBlock) {
			currentBlock = null;
			currentFace = null;

			Pair<BlockPos, EnumFacing> pair = breakQueue.poll();
			currentBlock = Objects.requireNonNull(pair).getKey();
			currentFace = Objects.requireNonNull(pair).getVal();
		}
	}

	private enum Mode {
		DAMAGE,
		PACKET,
		INSTANT,
		BREAKER,
	}

	private enum Haste {
		NONE,
		ONE,
		TWO
	}

}