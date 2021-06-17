package dev.zihasz.zware.features.module.player;

import dev.zihasz.zware.ZWare;
import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class TuxHackPhase extends Module {

	private final Setting<Mode> mode = new Setting<>("Mode", "", Mode.Absolute);
	private final Setting<Boolean> debug = new Setting<>("Debug", "Show debug information", false);
	private final Setting<Double> speed = new Setting<>("Speed", "", 6.25, 0.0, 6.25);
	private final Setting<Boolean> delay = new Setting<>("Delay", "Do a delay between actions.", true);
	private final Setting<Integer> tickDelay = new Setting<>("TickDelay", "The amount of delay in ticks.", 2, 0, 40);
	private final Setting<Boolean> sendRotations = new Setting<>("Rotations", "Send rotations packets", true);
	private final Setting<Boolean> enhancedControl = new Setting<>("EnhancedControl", "Control yaw and pitch with custom key binds.", false);
	private final Setting<Integer> enhancedControlAmount = new Setting<>("EnhancedControlSpeed", "The speed of enhanced control", 2, 0, 20, v -> enhancedControl.getValue());
	private final Setting<Boolean> invert = new Setting<>("Invert", "Inverts your yaw", false);
	private final Setting<Boolean> pup = new Setting<>("PUP", "Moves you forward I would guess :O", true);
	private final Setting<Boolean> avd = new Setting<>("AVD", "2b2tpvp.net bypass", false);
	private final Setting<Boolean> twoBPvp = new Setting<>("2bPvP", "2b2tpvp.net bypass", true);

	private final KeyBinding left, right, down, up;
	private long last = 0;

	public TuxHackPhase() {
		super("TuxHackPhase", "BlockPhase pasted from TuxHack", Category.PLAYER);

		left = new KeyBinding("Left", Keyboard.KEY_LEFT, "combat");
		right = new KeyBinding("Right", Keyboard.KEY_RIGHT, "combat");
		down = new KeyBinding("Down", Keyboard.KEY_DOWN, "combat");
		up = new KeyBinding("Up", Keyboard.KEY_UP, "combat");

		ClientRegistry.registerKeyBinding(left);
		ClientRegistry.registerKeyBinding(right);
		ClientRegistry.registerKeyBinding(down);
		ClientRegistry.registerKeyBinding(up);

	}

	@SubscribeEvent
	public void onPacketRead(PacketEvent.Read event) {
		if (event.getPacket() instanceof SPacketPlayerPosLook) {
			SPacketPlayerPosLook pak = (SPacketPlayerPosLook) event.getPacket();
			pak.yaw = mc.player.rotationYaw;
			pak.pitch = mc.player.rotationPitch;
		}
		if (event.getPacket() instanceof SPacketPlayerPosLook) {
			SPacketPlayerPosLook pak = (SPacketPlayerPosLook) event.getPacket();

			double dx = Math.abs(pak.getFlags().contains(SPacketPlayerPosLook.EnumFlags.X) ? pak.x : mc.player.posX - pak.x);
			double dy = Math.abs(pak.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Y) ? pak.y : mc.player.posY - pak.y);
			double dz = Math.abs(pak.getFlags().contains(SPacketPlayerPosLook.EnumFlags.Z) ? pak.z : mc.player.posZ - pak.z);

			if (dx < 1E-3) dx = 0;
			if (dz < 1E-3) dz = 0;

			if (!(dx == 0 && dy == 0 && dz == 0) && debug.getValue())
				mc.player.sendMessage(new TextComponentString("position pak, dx=" + dx + " dy=" + dy + " dz=" + dz));

			if (pak.yaw != mc.player.rotationYaw || pak.pitch != mc.player.rotationPitch) {
				if (sendRotations.getValue())
					mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, mc.player.rotationPitch, mc.player.onGround));
				pak.yaw = mc.player.rotationYaw;
				pak.pitch = mc.player.rotationPitch;
			}
		}
	}

	@Override
	public void onUpdate() {
		try {
			mc.player.noClip = true;
			if (tickDelay.getValue() > 0)
				if (mc.player.ticksExisted % tickDelay.getValue() != 0 && delay.getValue())
					return;

			int eca = enhancedControlAmount.getValue();

			if (enhancedControl.getValue() && up.isKeyDown()) mc.player.rotationPitch -= eca;
			if (enhancedControl.getValue() && down.isKeyDown()) mc.player.rotationPitch += eca;

			if (enhancedControl.getValue() && left.isKeyDown()) mc.player.rotationYaw -= eca;
			if (enhancedControl.getValue() && right.isKeyDown()) mc.player.rotationYaw += eca;

			double yaw = (mc.player.rotationYaw + 90) * (invert.getValue() ? -1 : 1);
			double xDir, zDir;

			if (mode.getValue() == Mode.Relative) {
				double dO_number = 0;
				double dO_denominator = 0;

				if (mc.gameSettings.keyBindLeft.isKeyDown()) {
					dO_number -= 90;
					dO_denominator++;
				}
				if (mc.gameSettings.keyBindRight.isKeyDown()) {
					dO_number += 90;
					dO_denominator++;
				}
				if (mc.gameSettings.keyBindBack.isKeyDown()) {
					dO_number += 180;
					dO_denominator++;
				}
				if (mc.gameSettings.keyBindForward.isKeyDown()) {
					dO_denominator++;
				}

				if (dO_denominator > 0) yaw += (dO_number / dO_denominator) % 361;

				if (yaw < 0) yaw = 360 - yaw;
				if (yaw > 360) yaw = yaw % 361;

				xDir = Math.cos(Math.toRadians(yaw));
				zDir = Math.sin(Math.toRadians(yaw));
			} else {
				xDir = 0;
				zDir = 0;

				xDir += mc.gameSettings.keyBindForward.isKeyDown() ? 1 : 0;
				xDir -= mc.gameSettings.keyBindBack.isKeyDown() ? 1 : 0;

				zDir += mc.gameSettings.keyBindLeft.isKeyDown() ? 1 : 0;
				zDir -= mc.gameSettings.keyBindRight.isKeyDown() ? 1 : 0;
			}

			if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown()) {
				mc.player.motionX = xDir * (speed.getValue() / 100);
				mc.player.motionZ = zDir * (speed.getValue() / 100);
			}
			mc.player.motionY = 0;

			boolean yes = false;
			if (avd.getValue()) {
				if (last + 50 < System.currentTimeMillis()) {
					last = System.currentTimeMillis();
					yes = true;
				}
			}

			mc.player.noClip = true;
			if (yes)
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, mc.player.posY + (mc.player.posY < (twoBPvp.getValue() ? 1.1 : -0.98) ? (speed.getValue() / 100) : 0) + (mc.gameSettings.keyBindJump.isKeyDown() ? (speed.getValue() / 100) : 0) - (mc.gameSettings.keyBindSneak.isKeyDown() ? (speed.getValue() / 100) : 0), mc.player.posZ + mc.player.motionZ, false)); // mc.player.rotationYaw, mc.player.rotationPitch, false));

			if (pup.getValue()) {
				mc.player.noClip = true;
				mc.player.setLocationAndAngles(mc.player.posX + mc.player.motionX, mc.player.posY, mc.player.posZ + mc.player.motionZ, mc.player.rotationYaw, mc.player.rotationPitch);
			}

			mc.player.noClip = true;
			if (yes)
				mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, mc.player.posY - 42069, mc.player.posZ + mc.player.motionZ, true));   //, mc.player.rotationYaw , mc.player.rotationPitch, true));

			mc.player.setLocationAndAngles(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.rotationYaw, mc.player.rotationPitch);
			mc.player.connection.sendPacket(new CPacketPlayer.Position(
					mc.player.posX, mc.player.posY, mc.player.posZ, false));
		} catch (Exception e) {
			if (debug.getValue())
				ZWare.LOGGER.error("TH Phase error: ", e);
			disable();
		}
	}

	@Override
	public void onDisable() {
		if (mc.player != null)
			mc.player.noClip = false;
	}

	private enum Mode {
		Relative,
		Absolute
	}

}