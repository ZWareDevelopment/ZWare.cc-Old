package dev.zihasz.zware.features.module.misc;

import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.manager.RelationManager;
import dev.zihasz.zware.utils.client.Message;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AutoClip extends Module {

	private final KeyBinding clipKey = new KeyBinding("Clip", Keyboard.KEY_F8, "ZWare.cc");
	private final Thread clip = new Thread(() -> {
		try {
			Robot clipper = new Robot();
			clipper.keyPress(clipKey.getKeyCode());
			Thread.sleep(100);
			clipper.keyRelease(clipKey.getKeyCode());
		} catch (Exception exception) {
			Message.sendErrorMessage("Failed clipping! " + exception.getMessage());
			exception.printStackTrace();
		}
	});
	private final List<EntityPlayer> targets = new ArrayList<>();

	public AutoClip() {
		super("AutoClip", "Automatically clip when you get a kill.", Category.MISC);
		ClientRegistry.registerKeyBinding(clipKey);
	}

	@Override
	public void onDisable() {
		targets.clear();
	}

	@Override
	public void onUpdate() {
		if (mc.player.ticksExisted % 200 == 0) {
			for (EntityPlayer player : targets) {
				if (player.isDead || player.getHealth() <= 0) {
					clip.start();
					targets.remove(player);
				}
			}
		}
	}

	@SubscribeEvent
	public void onAttackEntity(AttackEntityEvent event) {
		if (event.getTarget() instanceof EntityPlayer) {
			if (!RelationManager.isFriend(event.getEntityPlayer().getGameProfile().getId()))
				targets.add(event.getEntityPlayer());
		}
		if (event.getTarget() instanceof EntityEnderCrystal) {
			Objects.requireNonNull(mc.world.getNearestPlayerNotCreative(event.getTarget(), 6D));

			if (!RelationManager.isFriend(mc.world.getNearestPlayerNotCreative(event.getTarget(), 6D).getGameProfile().getId()))
				targets.add(mc.world.getNearestPlayerNotCreative(event.getTarget(), 6D));
		}
	}

}
