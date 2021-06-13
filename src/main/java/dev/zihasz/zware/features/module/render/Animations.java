package dev.zihasz.zware.features.module.render;

import dev.xdark.ssbus.Listener;
import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import net.minecraft.init.MobEffects;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;

public class Animations extends Module {

	public Animations() {
		super("Animations", "Change animations", Category.MISC);
	}

	private final Setting<Mode> mode = new Setting<>("OldAnimations", "Bring back old animations", Mode.OneDotEight);
	private final Setting<Swing> swing = new Setting<>("Swing", "Swing settings", Swing.Mainhand);
	private final Setting<Boolean> slow = new Setting<>("Slow", "Swing slower", true);

	@Override
	public void onUpdate() {
		if (nullCheck()) return;

		if (swing.getValue() == Swing.Offhand) {
			mc.player.swingingHand = EnumHand.OFF_HAND;
		}
		if (mode.getValue() == Mode.OneDotEight) {
			if (mc.entityRenderer.itemRenderer.prevEquippedProgressMainHand >= 0.9)
				mc.entityRenderer.itemRenderer.equippedProgressMainHand = 1.0f;
		}
	}

	@Override
	public void onEnable() {
		if (slow.getValue()) {
			mc.player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 255000));
		}
	}

	@Override
	public void onDisable() {
		if (slow.getValue()) {
			mc.player.removePotionEffect(MobEffects.MINING_FATIGUE);
		}
	}

	@Listener
	public void onPacketSend(PacketEvent.Send event) {
		Packet<?> raw = event.getPacket();
		if (raw instanceof CPacketAnimation) {
			CPacketAnimation packet = (CPacketAnimation) raw;
			if (swing.getValue() == Swing.Disable)
				event.cancel();
		}
	}

	private enum Swing {
		Mainhand,
		Offhand,
		Disable,
	}

	private enum Mode {
		Normal,
		OneDotEight,
	}

}
