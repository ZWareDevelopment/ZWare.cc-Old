package dev.zihasz.zware.features.module.combat;

import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.features.setting.SubSetting;
import dev.zihasz.zware.manager.RelationManager;
import dev.zihasz.zware.utils.entity.EntityUtils;
import dev.zihasz.zware.utils.math.MathUtils;
import dev.zihasz.zware.utils.misc.Timer;
import dev.zihasz.zware.utils.player.InventoryUtils;
import dev.zihasz.zware.utils.render.Renderer3D;
import dev.zihasz.zware.utils.world.Raytrace;
import dev.zihasz.zware.utils.world.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketDestroyEntities;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/*
	TODO: FacePlace, ArmorBreaker.
 */

public class CrystalAura extends Module {

	private final Setting<Logic> logic = new Setting<>("Logic", "not german", Logic.BreakPlace);
	private final Setting<Float> range = new Setting<>("Range", "Targeting range", 10f, 0f, 20f);

	private final Setting<Boolean> breakSetting = new Setting<>("Break", "Break crystals", true);
	private final SubSetting<BreakMode> breakMode = new SubSetting<>(breakSetting, "BreakMode", "Options on what crystals to break", BreakMode.Smart);
	private final SubSetting<Float> breakDelay = new SubSetting<>(breakSetting, "BreakDelay", "Delay to break crystals.", 10f, 0f, 200f);
	private final SubSetting<Integer> minBreakDamage = new SubSetting<>(breakSetting, "MinBreakDamage", "Minimum damage to break a crystal", 10, 0, 36);
	private final SubSetting<Integer> maxSelfBreakDamage = new SubSetting<>(breakSetting, "MaxSelfBreakDamage", "Max damage done to self when breaking a crystal", 10, 0, 36);
	private final SubSetting<Float> breakRange = new SubSetting<>(breakSetting, "BreakRange", "Range to break crystals.", 5f, 0f, 6f);
	private final SubSetting<Float> breakWallsRange = new SubSetting<>(breakSetting, "WallsBreakRange", "Range to break crystals thru walls.", 3.5f, 0f, 6f);
	private final SubSetting<Boolean> packetBreak = new SubSetting<>(breakSetting, "PacketBreak", "", true);
	private final SubSetting<Boolean> breakPurge = new SubSetting<>(breakSetting, "BreakPurge", "Purges crystals on break", false);
	private final SubSetting<Boolean> breakSwing = new SubSetting<>(breakSetting, "BreakSwing", "Swing arm when breaking", true);
	private final SubSetting<EnumHand> breakHand = new SubSetting<>(breakSetting, "BreakHand", "Which hand to use when breaking crystals", EnumHand.OFF_HAND);
	private final SubSetting<Boolean> antiWeakness = new SubSetting<>(breakSetting, "AntiWeakness", "Break crystals with a sword if you have weakness", true);
	private final SubSetting<Boolean> silentAntiWeakness = new SubSetting<>(breakSetting, "SilentAntiWeakness", "Silently switch to a sword", true);
	private final SubSetting<Boolean> noBreakSuicide = new SubSetting<>(breakSetting, "No break suicide", "Dont commit suicide when breaking", true);
	private final SubSetting<Boolean> fullySimBreak = new SubSetting<>(breakSetting, "FillySimulateBreak", "Simulate all break crystals, if off, we will break the first possible crystal, but will have better performance.", true);
	private final SubSetting<Boolean> antiStickyBreak = new SubSetting<>(breakSetting, "ABS", "AntiStickyBreak, Limits hit attempts", true);
	private final SubSetting<Integer> absAttempts = new SubSetting<>(breakSetting, "ABSAttempts", "Max ABS attempts", 5, 1, 20);
	private final SubSetting<Boolean> breakRotate = new SubSetting<>(breakSetting, "BreakRotate", "Rotate when breaking", false);

	private final Setting<Boolean> placeSetting = new Setting<>("Place", "Place crystals", true);
	private final SubSetting<PlaceMode> placeMode = new SubSetting<>(placeSetting, "PlaceMode", "Options on when to place", PlaceMode.Smart);
	private final SubSetting<Float> placeDelay = new SubSetting<>(placeSetting, "PlaceDelay", "Delay to place crystals.", 0f, 0f, 200f);
	private final SubSetting<Integer> minPlaceDamage = new SubSetting<>(placeSetting, "MinPlaceDamage", "Minimum damage to place a crystal", 10, 0, 36);
	private final SubSetting<Integer> maxSelfPlaceDamage = new SubSetting<>(placeSetting, "MaxSelfPlaceDamage", "Max damage done to self when place a crystal", 10, 0, 36);
	private final SubSetting<Float> placeRange = new SubSetting<>(placeSetting, "PlaceRange", "Range to place crystals.", 5f, 0f, 6f);
	private final SubSetting<Float> placeWallsRange = new SubSetting<>(placeSetting, "WallsPlaceRange", "Range to place crystals thru walls.", 3.5f, 0f, 6f);
	private final SubSetting<Boolean> packetPlace = new SubSetting<>(placeSetting, "PacketPlace", "", true);
	private final SubSetting<Boolean> lockPBR = new SubSetting<>(placeSetting, "LockPBR", "Lock place-break range. Useful on some servers.", true);
	private final SubSetting<Boolean> lockPBD = new SubSetting<>(placeSetting, "LockPBD", "Lock place-break delay. Useful on some servers.", true);
	private final SubSetting<Boolean> placeSwing = new SubSetting<>(placeSetting, "PlaceSwing", "Swing arm when placing", true);
	private final SubSetting<Boolean> fullySimPlace = new SubSetting<>(placeSetting, "FillySimulatePlace", "Simulate all place positions, if off, we will place the first possible location, but will have better performance.", true);
	private final SubSetting<Boolean> facePlace = new SubSetting<>(placeSetting, "FacePlace", "Place faces", true);
	private final SubSetting<Integer> facePlaceHealth = new SubSetting<>(placeSetting, "FacePlaceHealth", "Health to place faces at", 8, 0, 36);
	private final SubSetting<Boolean> armorBreaker = new SubSetting<>(placeSetting, "ArmorBreaker", "Breaks armor", true);
	private final SubSetting<Integer> armorBreakerPercent = new SubSetting<>(placeSetting, "ArmorBreaker%", "% to break armors at", 25, 0, 100);
	private final SubSetting<Boolean> noPlaceSuicide = new SubSetting<>(placeSetting, "NoPlaceSuicide", "Dont commit suicide when placing", true);
	private final SubSetting<Boolean> thirteen = new SubSetting<>(placeSetting, "1.13+", "Use on servers like ec.me.", false);
	private final SubSetting<Boolean> extraCheck = new SubSetting<>(placeSetting, "ExtraEntityCheck", "An extra check for entities when placing blocks", true);
	private final SubSetting<Boolean> placeRotate = new SubSetting<>(placeSetting, "PlaceRotate", "Rotate when placing", false);

	private final Setting<Boolean> switchSetting = new Setting<>("Switch", "Switch to crystals in hand", false);
	private final SubSetting<Boolean> silentSwitch = new SubSetting<>(switchSetting, "Silent", "Silently switches to crystals", true);
	private final SubSetting<Integer> switchHealth = new SubSetting<>(switchSetting, "Health", "If you are below this health it wont switch", 10, 0, 20);

	private final Setting<Boolean> syncSetting = new Setting<>("Sync", "Sync crystals client side, helps with speed.", true);
	private final SubSetting<Sync> syncMode = new SubSetting<>(syncSetting, "SyncMode", "How to sync crystals", Sync.Sound);
	private final SubSetting<Boolean> placebo = new SubSetting<>(syncSetting, "Placebo", "sand pills momento", false);
	private final SubSetting<Float> destroyRange = new SubSetting<>(syncSetting, "Range", "Range to destroy crystals at when explosion sound packet", 6f, 0f, 6f);

	private final Setting<Boolean> renderSetting = new Setting<>("Render", "Render current place position(s).", true);
	private final SubSetting<RenderMode> renderMode = new SubSetting<>(renderSetting, "DropDown", "The way to render the current place position", RenderMode.FULL);
	private final SubSetting<RenderShape> renderShape = new SubSetting<>(renderSetting, "Shape", "The shape to render the current place block", RenderShape.BLOCK);
	private final SubSetting<Float> renderWidth = new SubSetting<>(renderSetting, "OutlineWidth", "The width of the outline if mode is set to outline or full.  ", .5f, 0f, 5f);
	private final SubSetting<Float> renderHeight = new SubSetting<>(renderSetting, "SlabHeight", "The height of the slab if shape is set to slab.", .2f, 0f, 1f);
	private final SubSetting<Color> renderColor = new SubSetting<>(renderSetting, "Color", "Color of render.", new Color(255, 255, 255, 255));
	private final SubSetting<Boolean> renderDamage = new SubSetting<>(renderSetting, "RenderDamage", "Render damage done to the target by the current place crystal.", true);
	private final SubSetting<Boolean> renderTargetSetting = new SubSetting<>(renderSetting, "RenderTarget", "Render current target", true);
	private final SubSetting<Boolean> renderCryStats = new SubSetting<>(renderSetting, "RenderCrystalStats", "Render the stats of all crystals with vanilla nametags", true);

	private EntityLivingBase target = null;
	private List<BlockPos> placedCrystals;
	private List<Integer> ignoredCrystals;

	private BlockPos renderPos = null;
	private EntityEnderCrystal renderCrystal = null;
	private EntityLivingBase renderTarget = null;

	private boolean doRotate = false;
	private float yaw;
	private float pitch;

	private Timer breakTimer = new Timer();
	private Timer placeTimer = new Timer();

	public CrystalAura() {
		super("CrystalAura", "Makes crystals go boom boom.", Category.COMBAT);
		placedCrystals = new ArrayList<>();
		ignoredCrystals = new ArrayList<>();
	}

	@Override
	public void onUpdate() {
		if (fullNullCheck() || target == null || renderPos == null || renderCrystal == null || renderTarget == null)
			return;

		if (renderCryStats.getValue())
			doCrystalStats();
	}

	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (fullNullCheck() || target == null || renderPos == null || renderCrystal == null || renderTarget == null)
			return;

		if (lockPBR.getValue()) placeRange.setValue(breakRange.getValue());
		if (lockPBD.getValue()) placeDelay.setValue(breakDelay.getValue());

		doAutoCrystal();
	}

	@Override
	public void onRender3D(RenderWorldLastEvent event) {
		if (!renderSetting.getValue()) return;
		if (fullNullCheck() || target == null || renderPos == null || renderCrystal == null || renderTarget == null)
			return;
		switch (renderShape.getValue()) {
			case BLOCK:
				renderBlock();
				break;

			case SLAB:
				renderSlab();
				break;

			case FLAT:
				renderFlat();
				break;
		}
		if (renderDamage.getValue()) {
			DecimalFormat format = new DecimalFormat("###.###");
			String damage = format.format(EntityUtils.calculateDamage(renderPos, target));
			Renderer3D.drawTextFromBlock(renderPos, damage, 0xffffffff, 1.0f);
		}
	}

	@SubscribeEvent
	public void onPacketRead(PacketEvent.Read event) {
		Packet<?> rawPacket = event.getPacket();
		// Sound sync
		if (syncMode.getValue() == Sync.Sound && rawPacket instanceof SPacketSoundEffect) {
			SPacketSoundEffect packet = (SPacketSoundEffect) rawPacket;
			if (packet.category == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
				mc.world.loadedEntityList.stream()
						.filter(entity -> entity instanceof EntityEnderCrystal)
						.filter(entity -> entity.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= destroyRange.getValue())
						.forEach(Entity::setDead);
			}
		}

		// Ignored crystals
		if (rawPacket instanceof SPacketDestroyEntities) {
			SPacketDestroyEntities packet = (SPacketDestroyEntities) rawPacket;
			for (int id : packet.getEntityIDs()) {
				if (ignoredCrystals.contains(id)) {
					ignoredCrystals.remove(id);
					Objects.requireNonNull(mc.world.getEntityByID(id)).setDead();
				}
			}
		}
	}

	@SubscribeEvent
	public void onPacketSend(PacketEvent.Send event) {
		Packet<?> rawPacket = event.getPacket();
		// Instant sync
		if (rawPacket instanceof CPacketUseEntity) {
			CPacketUseEntity packet = (CPacketUseEntity) rawPacket;
			Entity entity = packet.getEntityFromWorld(mc.world);
			if (packet.getAction() == CPacketUseEntity.Action.ATTACK && entity instanceof EntityEnderCrystal) {
				if (syncMode.getValue() == Sync.Instant) entity.setDead();
			}
		}
		// Rotate
		if (rawPacket instanceof CPacketPlayer) {
			CPacketPlayer packet = (CPacketPlayer) rawPacket;
			if (packet.rotating && doRotate) {
				packet.yaw = this.yaw;
				packet.pitch = this.pitch;
				doRotate = false;
			}
		}
	}

	public void renderBlock() {
		AxisAlignedBB box = new AxisAlignedBB(renderPos);
		switch (renderMode.getValue()) {
			case FULL:
				Renderer3D.drawBBFill(box, renderColor.getValue());
				Renderer3D.drawBBOutline(box, renderWidth.getValue(), renderColor.getValue());
				break;
			case OUTLINE:
				Renderer3D.drawBBOutline(box, renderWidth.getValue(), renderColor.getValue());
				break;
			case FILL:
				Renderer3D.drawBBFill(box, renderColor.getValue());
				break;
		}
	}
	public void renderSlab() {
		AxisAlignedBB box = new AxisAlignedBB(renderPos).contract(0f, renderHeight.getValue(), 0f);
		switch (renderMode.getValue()) {
			case FULL:
				Renderer3D.drawBBFill(box, renderColor.getValue());
				Renderer3D.drawBBOutline(box, renderWidth.getValue(), renderColor.getValue());
				break;
			case OUTLINE:
				Renderer3D.drawBBOutline(box, renderWidth.getValue(), renderColor.getValue());
				break;
			case FILL:
				Renderer3D.drawBBFill(box, renderColor.getValue());
				break;
		}
	}
	public void renderFlat() {
		AxisAlignedBB box = new AxisAlignedBB(renderPos).setMaxY(0.1f);
		switch (renderMode.getValue()) {
			case FULL:
				Renderer3D.drawBBFill(box, renderColor.getValue());
				Renderer3D.drawBBOutline(box, renderWidth.getValue(), renderColor.getValue());
				break;
			case OUTLINE:
				Renderer3D.drawBBOutline(box, renderWidth.getValue(), renderColor.getValue());
				break;
			case FILL:
				Renderer3D.drawBBFill(box, renderColor.getValue());
				break;
		}
	}

	public void doCrystalStats() {
		mc.world.loadedEntityList.stream()
				.filter(entity -> entity instanceof EntityEnderCrystal)
				.map(entity -> (EntityEnderCrystal) entity)
				.forEach(crystal -> {
					crystal.setCustomNameTag("DIST=" + EntityUtils.getDistance(crystal) + " DMG=" + new DecimalFormat("###.###").format(EntityUtils.calculateDamage(crystal, target)));
					crystal.setAlwaysRenderNameTag(true);
				});
	}

	public void doAutoCrystal() {
		target = getTarget();
		if (logic.getValue() == Logic.BreakPlace) {
			doBreak();
			doPlace();
		}

		if (logic.getValue() == Logic.PlaceBreak) {
			doPlace();
			doBreak();
		}
	}
	public void doPlace() {
		if (!placeSetting.getValue()) return;
		if (!placeTimer.passedMS((long) (placeDelay.getValue() * 50))) return;
		if (getPlaceBlock() == null) return;

		placeTimer.reset();

		BlockPos position = getPlaceBlock();

		double selfDamage = EntityUtils.calculateDamage(position, mc.player);
		double targetDamage = EntityUtils.calculateDamage(position, target);

		if (selfDamage > maxSelfPlaceDamage.getValue()) return;

		switch (placeMode.getValue()) {
			case All:
				renderPos = position;
				placeCrystal(position);
				placedCrystals.add(position);
				break;

			case Lethal:
				if (targetDamage >= target.getHealth() + target.getAbsorptionAmount()) {
					renderPos = position;
					placeCrystal(position);
					placedCrystals.add(position);
				}
				break;

			case Smart:
				if (targetDamage >= target.getHealth() + target.getAbsorptionAmount()) {
					renderPos = position;
					placeCrystal(position);
					placedCrystals.add(position);
				} else if (targetDamage > selfDamage) {
					renderPos = position;
					placeCrystal(position);
					placedCrystals.add(position);
				}
				break;
		}
	}
	public void doBreak() {
		if (!breakSetting.getValue()) return;
		if (!breakTimer.passedMS((long) (breakDelay.getValue() * 50))) return;
		if (getBreakCrystal() == null) return;

		breakTimer.reset();

		EntityEnderCrystal crystal = getBreakCrystal();
		BlockPos crystalBase = crystal.getPosition().down();

		double selfDamage = EntityUtils.calculateDamage(crystal, mc.player);
		double targetDamage = EntityUtils.calculateDamage(crystal, target);

		if (selfDamage > maxSelfBreakDamage.getValue()) return;

		switch (breakMode.getValue()) {
			case All:
				renderCrystal = crystal;
				breakCrystal(crystal);
				placedCrystals.remove(crystalBase);
				break;

			case Own:
				if (placedCrystals.contains(crystalBase)) {
					renderCrystal = crystal;
					breakCrystal(crystal);
					placedCrystals.remove(crystalBase);
				}
				break;

			case Smart:
				if (targetDamage >= target.getHealth() + target.getAbsorptionAmount()) {
					renderCrystal = crystal;
					breakCrystal(crystal);
					placedCrystals.remove(crystalBase);
				} else if (targetDamage > selfDamage) {
					renderCrystal = crystal;
					breakCrystal(crystal);
					placedCrystals.remove(crystalBase);
				} else if (selfDamage <= mc.player.getAbsorptionAmount()) {
					renderCrystal = crystal;
					breakCrystal(crystal);
					placedCrystals.remove(crystalBase);
				}
				break;
		}

	}

	private EntityLivingBase getTarget() {
		return this.renderTarget = mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityPlayer).map(entity -> (EntityPlayer) entity).filter(player -> !player.isDead).filter(player -> player != mc.player).filter(player -> EntityUtils.getDistance(player) < range.getValue()).filter(player -> !RelationManager.isFriend(player.getGameProfile().getId())).filter(player -> RelationManager.isEnemy(player.getGameProfile().getId())).min(Comparator.comparingDouble(EntityUtils::getDistance)).orElse(mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityPlayer).map(entity -> (EntityPlayer) entity).filter(player -> !player.isDead).filter(player -> player != mc.player).filter(player -> EntityUtils.getDistance(player) < range.getValue()).filter(player -> !RelationManager.isFriend(player.getGameProfile().getId())).min(Comparator.comparingDouble(EntityUtils::getDistance)).orElse(null));
	}
	private BlockPos getPlaceBlock() {
		BlockPos bestPosition = null;
		double mostDamage = 0;

		for (BlockPos pos : WorldUtils.getPlacePositions(placeRange.getValue(), extraCheck.getValue(), thirteen.getValue())) {
			double damage = EntityUtils.calculateDamage(pos.up(), target);

			if (!isPlaceValid(pos)) continue;
			if (damage < minPlaceDamage.getValue()) continue;
			if (!fullySimPlace.getValue()) return pos;
			if (mostDamage < damage) {
				mostDamage = damage;
				bestPosition = pos;
			}

		}

		return bestPosition;
	}
	private EntityEnderCrystal getBreakCrystal() {
		EntityEnderCrystal bestCrystal = null;
		double mostDamage = 0;

		for (EntityEnderCrystal crystal : WorldUtils.getAllCrystals()) {
			double damage = EntityUtils.calculateDamage(crystal, target);

			if (!isBreakValid(crystal)) continue;
			if (damage < minBreakDamage.getValue()) continue;

			if (!fullySimBreak.getValue()) return crystal;
			if (mostDamage < damage) {
				mostDamage = damage;
				bestCrystal = crystal;
			}

		}
		return bestCrystal;
	}

	private boolean isBreakValid(Entity crystal) {
		if (!(crystal instanceof EntityEnderCrystal)) return false;
		if (crystal.isDead) return false;
		if (EntityUtils.getDistance(crystal) > breakRange.getValue()) return false;
		if (Raytrace.raytrace(mc.player, crystal) == null && EntityUtils.getDistance(crystal) > breakWallsRange.getValue())
			return false;
		return !noBreakSuicide.getValue() || !(EntityUtils.calculateDamage((EntityEnderCrystal) crystal, mc.player) > mc.player.getHealth() + mc.player.getAbsorptionAmount());
	}
	private boolean isPlaceValid(BlockPos position) {
		if (!WorldUtils.canPlaceCrystal(position, extraCheck.getValue(), thirteen.getValue())) return false;
		if (!(mc.world.getBlockState(position).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(position).getBlock() == Blocks.OBSIDIAN))
			return false;
		if (EntityUtils.getDistance(position) > placeRange.getValue()) return false;
		if (Raytrace.raytrace(mc.player, position) == null && EntityUtils.getDistance(position) > placeWallsRange.getValue())
			return false;
		return !noPlaceSuicide.getValue() || !(EntityUtils.calculateDamage(position, mc.player) > mc.player.getHealth() + mc.player.getAbsorptionAmount());
	}

	private void breakCrystal(EntityEnderCrystal crystal) {
		// If we dont break return
		if (!breakSetting.getValue()) return;

		// Rotate
		if (breakRotate.getValue()) {
			RayTraceResult result = Raytrace.raytrace(mc.player, crystal);
			Vec2f rotation = MathUtils.vectorToPY(result.hitVec);
			rotate(rotation.x, rotation.y);
		}

		int swordSlot = InventoryUtils.findFirst(Items.DIAMOND_SWORD);
		int oldSlot = mc.player.inventory.currentItem;

		// AntiWeakness
		if (antiWeakness.getValue() && mc.player.getActivePotionMap().containsKey(MobEffects.WEAKNESS))
			InventoryUtils.switchToSlot(swordSlot, silentAntiWeakness.getValue(), true);

		// Break - ABS and ignoring crystals
		if (antiStickyBreak.getValue()) {
			for (int i = 0; i < absAttempts.getValue(); i++) {
				if (packetBreak.getValue()) mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
				else mc.playerController.attackEntity(mc.player, crystal);
			}
			ignoredCrystals.add(crystal.entityId);
		} else {
			if (packetBreak.getValue()) mc.player.connection.sendPacket(new CPacketUseEntity(crystal));
			else mc.playerController.attackEntity(mc.player, crystal);
		}

		// AntiWeakness
		if (antiWeakness.getValue() && mc.player.getActivePotionMap().containsKey(MobEffects.WEAKNESS))
			InventoryUtils.switchToSlot(oldSlot, silentAntiWeakness.getValue(), true);

		// Swing
		if (breakSwing.getValue()) swingArm(breakHand.getValue(), packetBreak.getValue());

		// "Sync"
		if (placebo.getValue()) crystal.setDead();

		// Purge
		if (breakPurge.getValue())
			mc.player.connection.handleDestroyEntities(new SPacketDestroyEntities(crystal.entityId));
	}
	private void placeCrystal(BlockPos pos) {
		// If we dont place return
		if (!placeSetting.getValue()) return;

		// Raytrace to the block
		RayTraceResult result = Raytrace.raytrace(mc.player, pos);

		// Rotate to the block
		if (placeRotate.getValue()) {
			Vec2f rotation = MathUtils.vectorToPY(result.hitVec);
			rotate(rotation.x, rotation.y);
		}

		int crystalSlot = InventoryUtils.findFirst(Items.END_CRYSTAL);
		int oldSlot = mc.player.inventory.currentItem;

		// Get the crystal hand
		EnumHand hand = mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? EnumHand.MAIN_HAND : mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL ? EnumHand.OFF_HAND : null;

		// Switch
		if (hand == null && switchSetting.getValue() && mc.player.getHealth() > switchHealth.getValue()) {
			InventoryUtils.switchToSlot(crystalSlot, silentSwitch.getValue(), true);
			hand = EnumHand.MAIN_HAND;
		}

		// Place
		if (packetPlace.getValue())
			mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, result.sideHit, hand, (float) result.hitVec.x, (float) result.hitVec.y, (float) result.hitVec.z));
		else mc.playerController.processRightClickBlock(mc.player, mc.world, pos, result.sideHit, result.hitVec, hand);

		// Switch
		if (switchSetting.getValue())
			InventoryUtils.switchToSlot(oldSlot, silentSwitch.getValue(), true);

		// Swing
		if (placeSwing.getValue()) swingArm(hand, packetPlace.getValue());
	}
	private void swingArm(EnumHand hand, boolean packet) {
		if (packet) mc.player.connection.sendPacket(new CPacketAnimation(hand));
		else mc.player.swingArm(hand);
	}
	private void rotate(float yaw, float pitch) {
		doRotate = true;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	private void reset() {
		breakTimer.reset();
		placeTimer.reset();
		placedCrystals = new ArrayList<>();
		ignoredCrystals = new ArrayList<>();
		target = null;
		renderPos = null;
		renderTarget = null;
		renderCrystal = null;
	}

	private enum Logic {
		BreakPlace,
		PlaceBreak
	}
	private enum BreakMode {
		All,
		Own,
		Smart
	}
	private enum PlaceMode {
		All,
		Lethal,
		Smart
	}
	private enum Sync {
		Instant,
		Sound
	}
	private enum RenderMode {
		OUTLINE,
		FILL,
		FULL,
	}
	private enum RenderShape {
		BLOCK,
		SLAB,
		FLAT,
	}

}
