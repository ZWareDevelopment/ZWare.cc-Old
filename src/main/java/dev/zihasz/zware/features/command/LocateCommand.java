package dev.zihasz.zware.features.command;

import dev.zihasz.zware.utils.client.Message;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class LocateCommand extends Command {

	public LocateCommand() {
		super("locate", "Locates a player.", new String[]{ "loc", "find" });
	}

	@Override
	public void execute(String[] args) {
		System.out.println();
		if (args.length < 1) {
			Message.sendErrorMessage("You have to supply at least 1 argument!");
			return;
		}
		String name = args[0];
		EntityPlayer player = mc.world.getPlayerEntityByName(name);
		if (player == null) {
			Message.sendErrorMessage("You have to supply a real name (or the player is no longer online).");
			return;
		}
		BlockPos pos = player.getPosition();
		Message.sendSuccessMessage("Found " + name + "!");
		Message.sendInfoMessage(String.format("They are at X: %s, Y: %s, Z: %s! Go kill them!%n", pos.x, pos.y, pos.z));
	}

}
