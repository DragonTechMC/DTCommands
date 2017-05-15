package com.dragontechmc.dtcommands;

import java.net.URL;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.text.format.TextColors;

public class HyperlinkCommand implements CommandExecutor {

	private String linkText;
	private URL linkUrl;

	public HyperlinkCommand(String linkText, URL linkUrl) {
		this.linkText = linkText;
		this.linkUrl = linkUrl;
	}

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		// Command
		if(src instanceof Player) {
			Player player = (Player) src;

			// Text
			Text memberText = Text.builder(linkText).color(TextColors.YELLOW).onClick(TextActions.openUrl(linkUrl)).build();
			player.sendMessage(memberText);
		}
		else if(src instanceof ConsoleSource) {
			src.sendMessage(Text.of("Player Only Command"));
		}

		return CommandResult.success();
	}
}
