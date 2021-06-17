package dev.zihasz.zware.features.module.movement;

import dev.zihasz.zware.event.events.MoveEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.utils.entity.EntityUtils;
import dev.zihasz.zware.utils.entity.TimerUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author zihasz
 * @since 2021/06/02
 * @version 1.0
 */
public class Strafe extends Module {

	public Strafe() {
		super("Strafe", "Makes strafing easy", Category.MOVEMENT);
	}

	private final Setting<Boolean> jump = new Setting<>("Jump", "Makes you jump", true);
	private final Setting<Float> height = new Setting<>("Jump Height", "The height of each jump", .41f, .0f, 1.5f, v -> jump.getValue());
	private final Setting<Boolean> fall = new Setting<>("Fast Fall", "Makes you fall faster", true, v -> jump.getValue());

	private final Setting<Boolean> timer = new Setting<>("Timer", "Speed up even more using timer", true);
	private final Setting<Float> timerSpeed = new Setting<>("Timer Speed", "The speed of the timer", 1.1f, 0f, 2f, v -> timer.getValue());

	private final Setting<Float> forwardMp = new Setting<>("ForwardMP", "The vanilla forward multiplier.", 1.25f, 0f, 2f);
	private final Setting<Float> strafeMp = new Setting<>("StrafingMP", "The vanilla strafe multiplier.", 1.125f, 0f, 2f);

	private double prevMotionY = 0;

	@Override
	public void onEnable() {
		prevMotionY = mc.player.motionY;
	}

	@Override
	public void onDisable() {
		TimerUtils.resetTimer();
	}

	@Override
	public void onUpdate() {
		mc.player.moveForward *= forwardMp.getValue();
		mc.player.moveStrafing *= strafeMp.getValue();
	}

	@SubscribeEvent
	public void onMove(MoveEvent event) {

		if (timer.getValue()) {
			if (mc.player.onGround)
				TimerUtils.setTimer(timerSpeed.getValue());
			else
				TimerUtils.resetTimer();
		}


		if (fall.getValue() && !mc.player.onGround) {
			prevMotionY = mc.player.motionY;
			mc.player.motionY = -(prevMotionY * 1.1);
		}

		Vec3d curVelocity = new Vec3d(mc.player.motionX, mc.player.motionY, mc.player.motionZ);
		BlockPos lastTick = new BlockPos(mc.player.lastTickPosX, mc.player.lastTickPosY, mc.player.lastTickPosZ);
		BlockPos thisTick = new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ);
		BlockPos nextTick = EntityUtils.getPosNextTick(mc.player);

	}

}
