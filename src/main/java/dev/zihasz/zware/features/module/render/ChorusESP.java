package dev.zihasz.zware.features.module.render;

import dev.zihasz.zware.event.events.PacketEvent;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.features.setting.Setting;
import dev.zihasz.zware.utils.misc.Timer;
import dev.zihasz.zware.utils.render.Renderer3D;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class ChorusESP extends Module {

	private final Setting<Color> color = new Setting<>("Color", "The color of thing", new Color(255, 255, 255, 255));
	private final Setting<Float> width = new Setting<>("Width", "The width of thing", 1f, 0f, 5f);
	private final Setting<Boolean> up = new Setting<>("Double", "Render the full thing", true);

	private BlockPos position;
	private Timer timer;

	public ChorusESP() {
		super("ChorusESP", "Shows where a chorus will TP you", Category.RENDER);
		timer = new Timer();
	}

	@SubscribeEvent
	public void onPacketRead(PacketEvent.Read event) {
		Packet<?> raw = event.getPacket();
		if (raw instanceof SPacketSoundEffect) {
			SPacketSoundEffect packet = (SPacketSoundEffect) raw;
			if (packet.sound == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT) {
				position = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
				timer.reset();
			}
		}
	}

	@Override
	public void onRender3D(RenderWorldLastEvent event) {
		if (position != null) {
			if (timer.passedMS(2000)) {
				position = null;
				return;
			}
			if (width.getValue() != 0f) Renderer3D.drawBBOutline(new AxisAlignedBB(position), width.getValue(), color.getValue());
			Renderer3D.drawBBFill(new AxisAlignedBB(position), color.getValue());
		}
	}
}
