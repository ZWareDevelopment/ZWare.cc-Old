package dev.zihasz.zware.utils.math;

import dev.zihasz.zware.utils.Util;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Random;

public class MathUtils implements Util {

	public static double clamp(double min, double max, double val) {
		if (val > max || min > max) {
			return max;
		}
		return Math.max(val, min);
	}

	public static double clamp(double min, double max, double val, double height) {
		if (val <= min || max <= min) {
			return min;
		}
		if (val + height >= max || min >= max) {
			return max - height;
		}
		return val;
	}

	public static double square(double one) {
		return one * one;
	}

	public static double roundAvoid(double value, int places) {
		double scale = Math.pow(10, places);
		return Math.round(value * scale) / scale;
	}

	public static Vec3d extrapolatePlayerPosition(Entity player, int ticks) {
		Vec3d lastPos = new Vec3d(player.lastTickPosX, player.lastTickPosY, player.lastTickPosZ);
		Vec3d currentPos = new Vec3d(player.posX, player.posY, player.posZ);
		double distance = multiply(player.motionX) + multiply(player.motionY) + multiply(player.motionZ);
		Vec3d tempVec = calculateLine(lastPos, currentPos, distance * ticks);
		return new Vec3d(tempVec.x, player.posY, tempVec.z);
	}

	public static double multiply(double one) {
		return one * one;
	}

	public static Vec3d calculateLine(Vec3d x1, Vec3d x2, double distance) {
		double length = Math.sqrt(multiply(x2.x - x1.x) + multiply(x2.y - x1.y) + multiply(x2.z - x1.z));
		if (length != 0.0) {
			double unitSlopeX = (x2.x - x1.x) / length;
			double unitSlopeY = (x2.y - x1.y) / length;
			double unitSlopeZ = (x2.z - x1.z) / length;
			double x = x1.x + unitSlopeX * distance;
			double y = x1.y + unitSlopeY * distance;
			double z = x1.z + unitSlopeZ * distance;
			return new Vec3d(x, y, z);
		} else {
			return x2;
		}
	}

	public static Vec3d interpolate(float y, float p, float delta, float one, float dist) {
		final float d1 = (delta / one) * dist;
		final double[] calc = calc3d(p, y, d1);
		return new Vec3d(calc[0], calc[1], calc[2]);
	}

	public static Vec3d interpolate(float p, float delta, float one, float dist) {
		final float d1 = (delta / one) * dist;
		final double[] calc = calc2d(p, d1);
		return new Vec3d(calc[0], 0, calc[1]);
	}

	public static double[] calc2d(float p, float dist) {
		final double x = dist * (Math.cos(Math.toRadians(p)));
		final double y = dist * (Math.sin(Math.toRadians(p)));

		return new double[]{x, y};
	}

	public static double[] calc3d(float p, float y, float dist) {
		double x = dist * Math.cos(Math.toRadians(p));
		double z = dist * Math.sin(Math.toRadians(p));
		double dist1 = Math.abs(0 - x);

		double x1 = dist1 * Math.cos(Math.toRadians(y));
		double z1 = dist1 * Math.sin(Math.toRadians(y));

		return new double[]{x1, z, z1};
	}

	public static double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static int normalizeAngle(int angle, int maxAngle) {
		return (angle % maxAngle + maxAngle) % maxAngle;
	}

	public static float normalizeAngle(float angle, float maxAngle) {
		return (angle % maxAngle + maxAngle) % maxAngle;
	}

	public static double normalizeAngle(double angle, double maxAngle) {
		return (angle % maxAngle + maxAngle) % maxAngle;
	}

	public static double normalizeAngle(double angle) {
		angle %= 360.0;

		if (angle >= 180.0) {
			angle -= 360.0;
		}

		if (angle < -180.0) {
			angle += 360.0;
		}

		return angle;
	}

	public static float normalizeAngle(float angle) {
		angle %= 360.0f;

		if (angle >= 180.0f) {
			angle -= 360.0f;
		}

		if (angle < -180.0f) {
			angle += 360.0f;
		}

		return angle;
	}

	public static float wrapDegrees(float value) {
		value %= 360.0F;
		if (value >= 180.0F) {
			value -= 360.0F;
		}

		if (value < -180.0F) {
			value += 360.0F;
		}

		return value;
	}

	public static double wrapDegrees(double value) {
		value %= 360.0D;
		if (value >= 180.0D) {
			value -= 360.0D;
		}

		if (value < -180.0D) {
			value += 360.0D;
		}

		return value;
	}

	public static int wrapDegrees(int angle) {
		angle %= 360;
		if (angle >= 180) {
			angle -= 360;
		}

		if (angle < -180) {
			angle += 360;
		}

		return angle;
	}

	public static Vec2f vectorToPY(Vec3d vec) {
		double lengthXZ = Math.hypot(vec.x, vec.z);
		double yaw = normalizeAngle(Math.toDegrees(Math.atan2(vec.z, vec.x)) - 90.0);
		double pitch = normalizeAngle(Math.toDegrees(-Math.atan2(vec.y, lengthXZ)));
		return new Vec2f((float) yaw, (float) pitch);
	}

	public static int randomIntBetween(int min, int max) {
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}

	public static double doubleAndAdd(double x, double y, double z) {
		return x * x + y * y + z * z;
	}

	private double average(Collection<Double> collection) {
		if (collection.isEmpty()) return 0.0;

		double sum = 0.0;
		int size = 0;

		for (double element : collection) {
			sum += element;
			size++;
		}

		return sum / size;
	}

}
