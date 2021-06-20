package dev.zihasz.zware.utils.entity;

import dev.zihasz.zware.utils.Util;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

public class EntityUtils implements Util {

	public static float calculateDamage(double posX, double posY, double posZ, Entity target) {
		try {
			double factor = (1.0 - target.getDistance(posX, posY, posZ) / 12.0f) * target.world.getBlockDensity(new Vec3d(posX, posY, posZ), target.getEntityBoundingBox());
			float calculatedDamage = (float) (int) ((factor * factor + factor) / 2.0 * 7.0 * 12.0f + 1.0);
			double damage = 1.0;

			if (target instanceof EntityLivingBase)
				damage = getBlastReduction((EntityLivingBase) target, calculatedDamage * ((mc.world.getDifficulty().getId() == 0) ? 0.0f : ((mc.world.getDifficulty().getId() == 2) ? 1.0f : ((mc.world.getDifficulty().getId() == 1) ? 0.5f : 1.5f))), new Explosion(mc.world, null, posX, posY, posZ, 6.0f, false, true));

			return (float) damage;
		} catch (Exception ignored) {

		}

		return 0;
	}
	public static float calculateDamage(Vec3d pos, Entity target) {
		try {
			double factor = (1.0 - target.getDistance(pos.x, pos.y, pos.z) / 12.0f) * target.world.getBlockDensity(pos, target.getEntityBoundingBox());
			float calculatedDamage = (float) (int) ((factor * factor + factor) / 2.0 * 7.0 * 12.0f + 1.0);
			double damage = 1.0;

			if (target instanceof EntityLivingBase)
				damage = getBlastReduction((EntityLivingBase) target, calculatedDamage * ((mc.world.getDifficulty().getId() == 0) ? 0.0f : ((mc.world.getDifficulty().getId() == 2) ? 1.0f : ((mc.world.getDifficulty().getId() == 1) ? 0.5f : 1.5f))), new Explosion(mc.world, null, pos.x, pos.y, pos.z, 6.0f, false, true));

			return (float) damage;
		} catch (Exception ignored) {

		}

		return 0;
	}
	public static float calculateDamage(BlockPos pos, Entity target) {
		try {
			double factor = (1.0 - target.getDistance(pos.x, pos.y, pos.z) / 12.0f) * target.world.getBlockDensity(new Vec3d(pos.x, pos.y, pos.z), target.getEntityBoundingBox());
			float calculatedDamage = (float) (int) ((factor * factor + factor) / 2.0 * 7.0 * 12.0f + 1.0);
			double damage = 1.0;

			if (target instanceof EntityLivingBase)
				damage = getBlastReduction((EntityLivingBase) target, calculatedDamage * ((mc.world.getDifficulty().getId() == 0) ? 0.0f : ((mc.world.getDifficulty().getId() == 2) ? 1.0f : ((mc.world.getDifficulty().getId() == 1) ? 0.5f : 1.5f))), new Explosion(mc.world, null, pos.x, pos.y, pos.z, 6.0f, false, true));

			return (float) damage;
		} catch (Exception ignored) {

		}

		return 0;
	}
	public static float calculateDamage(EntityEnderCrystal crystal, Entity target) {
		try {
			BlockPos pos = crystal.getPosition();
			double factor = (1.0 - target.getDistance(pos.x, pos.y, pos.z) / 12.0f) * target.world.getBlockDensity(new Vec3d(pos.x, pos.y, pos.z), target.getEntityBoundingBox());
			float calculatedDamage = (float) (int) ((factor * factor + factor) / 2.0 * 7.0 * 12.0f + 1.0);
			double damage = 1.0;

			if (target instanceof EntityLivingBase)
				damage = getBlastReduction((EntityLivingBase) target, calculatedDamage * ((mc.world.getDifficulty().getId() == 0) ? 0.0f : ((mc.world.getDifficulty().getId() == 2) ? 1.0f : ((mc.world.getDifficulty().getId() == 1) ? 0.5f : 1.5f))), new Explosion(mc.world, crystal, pos.x, pos.y, pos.z, 6.0f, false, true));

			return (float) damage;
		} catch (Exception ignored) {

		}

		return 0;
	}

	public static float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
		if (entity instanceof EntityPlayer) {
			damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
			damage *= 1.0f - MathHelper.clamp((float) EnchantmentHelper.getEnchantmentModifierDamage(entity.getArmorInventoryList(), DamageSource.causeExplosionDamage(explosion)), 0.0f, 20.0f) / 25.0f;

			if (entity.isPotionActive(MobEffects.RESISTANCE))
				damage -= damage / 4.0f;

			return damage;
		}

		damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
		return damage;
	}

	public static double getDistance(Entity entity) {
		return mc.player.getPositionVector().add(0, mc.player.eyeHeight, 0).distanceTo(entity.getPositionVector().add(0, entity.height / 2d, 0));
	}

	public static double getDistance(BlockPos entity) {
		return mc.player.getPositionVector().add(0, mc.player.eyeHeight, 0).distanceTo(new Vec3d(entity.x, entity.y, entity.z));
	}

	public static BlockPos getPosNextTick(EntityPlayer player) {
		double diffX = player.posX - player.lastTickPosX;
		double diffY = player.posY - player.lastTickPosY;
		double diffZ = player.posZ - player.lastTickPosZ;
		double moveX = player.posX + diffX;
		double moveY = player.posY + diffY;
		double moveZ = player.posZ + diffZ;
		return new BlockPos(moveX, moveY, moveZ);
	}

	public static double getVelocity(EntityPlayer player) {
		return player.motionX * player.motionX + player.motionY * player.motionY + player.motionZ + player.motionZ;
	}

	public static boolean isBurrowed(Entity entity) {
		return !mc.world.getBlockState(entity.getPosition()).getBlock().isPassable(mc.world, entity.getPosition());
	}

	public static double calcSpeed(EntityPlayerSP player) {
		double tps = 1000.0 / mc.timer.tickLength;
		double xDiff = player.posX - player.prevPosX;
		double zDiff = player.posZ - player.prevPosZ;

		return Math.hypot(xDiff, zDiff) * tps;
	}

}
