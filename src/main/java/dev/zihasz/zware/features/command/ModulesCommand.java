package dev.zihasz.zware.features.command;

import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.manager.ModuleManager;
import dev.zihasz.zware.utils.client.Message;
import net.minecraft.util.text.TextFormatting;

public class ModulesCommand extends Command {

	public ModulesCommand() {
		super("modules", "List all modules", new String[] { "allmodules", "list" });
	}

	@Override
	public void execute(String[] args) {
		for (Module module : ModuleManager.getModules()) {
			Message.sendClientMessage((module.isEnabled() ? TextFormatting.GREEN : TextFormatting.RED) + module.getName() + " - " + module.getDescription());
		}
	}
}
