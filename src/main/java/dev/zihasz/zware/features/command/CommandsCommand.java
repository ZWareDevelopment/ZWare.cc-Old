package dev.zihasz.zware.features.command;

import dev.zihasz.zware.manager.CommandManager;
import dev.zihasz.zware.utils.client.Message;

public class CommandsCommand extends Command {

	public CommandsCommand() {
		super("commands", "List all commands", new String[] { "help" });
	}

	@Override
	public void execute(String[] args) {
		for (Command command : CommandManager.getCommands()) {
			Message.sendInfoMessage(command.getName() + " - " + command.getDescription());
		}
	}
}
