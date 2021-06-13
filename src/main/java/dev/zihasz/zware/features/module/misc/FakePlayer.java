package dev.zihasz.zware.features.module.misc;

import com.mojang.authlib.GameProfile;
import dev.zihasz.zware.features.module.Category;
import dev.zihasz.zware.features.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

public class FakePlayer extends Module {

	public FakePlayer() {
		super("FakePlayer", "Creates a player for testing", Category.MISC);
	}

	private final int entityId = -99;

	EntityOtherPlayerMP player = null;

	@Override
	public void onEnable() {
		GameProfile profile = new GameProfile(UUID.fromString("7c42a18c-659f-4f49-876e-5c065e50b86d"), "06d");
		player = new EntityOtherPlayerMP(mc.world, profile);
		player.copyLocationAndAnglesFrom(mc.player);
		player.rotationYawHead = mc.player.rotationYawHead;
		mc.world.addEntityToWorld(entityId, player);
	}

	@Override
	public void onUpdate() {
		player.inventory = mc.player.inventory;
	}

	@Override
	public void onDisable() {
		mc.world.removeEntityFromWorld(entityId);
	}

}
