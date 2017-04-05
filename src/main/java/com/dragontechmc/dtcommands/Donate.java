package com.dragontechmc.dtcommands;

import java.net.MalformedURLException;
import java.net.URL;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

public class Donate implements CommandExecutor {


	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {


		// URL
		URL storeURL;
		try {
			storeURL = new URL("http://www.dragontechmc.com/shop");


			// Text
			Text memberText = Text.builder("Click here to go to Donation Store").color(TextColors.YELLOW).onClick(TextActions.openUrl(storeURL)).build();


			// Command
			if(src instanceof Player) {
				Player player = (Player) src;
				player.sendMessage(memberText);
			}

			else if(src instanceof ConsoleSource) {
				src.sendMessage(Text.of("Player Only Command"));
			}



		} catch (MalformedURLException e) {
			
			}



		return CommandResult.success();

	}
	
	
}
