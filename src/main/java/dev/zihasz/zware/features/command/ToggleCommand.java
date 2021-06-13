package dev.zihasz.zware.features.command;

import dev.zihasz.zware.ZWare;
import dev.zihasz.zware.features.module.Module;
import dev.zihasz.zware.manager.ModuleManager;
import dev.zihasz.zware.utils.client.Message;

public class ToggleCommand extends Command {

	public ToggleCommand() {
		super("toggle", "Toggles a module", new String[] { "t", "togglemodule" });
	}

	@Override
	public void execute(String[] args) {
		if (args.length < 1) {
			Message.sendErrorMessage("Please supply a module name.");
			return;
		}

		Module module = ModuleManager.getModule(args[0]);

		if (module == null) {
			Message.sendErrorMessage("Cannot find " + args[0]);
			return;
		}
		module.toggle();
		if (ZWare.clientManager.isDebug())
			Message.sendInfoMessage(args[0] + ".enabled = " + ModuleManager.getModule(args[0]).isEnabled());
	}

}
