package com.dragontechmc.dtcommands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

public class ReloadCommand implements CommandExecutor {

	private DTCommands plugin;
	
	public ReloadCommand(DTCommands plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

		plugin.removeCommands();
		
		plugin.registerCommands();

		src.sendMessage(Text.of("All DTCommands have been reloaded"));

		return CommandResult.success();
	}	
}
