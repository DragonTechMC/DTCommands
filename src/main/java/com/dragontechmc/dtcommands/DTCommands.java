package com.dragontechmc.dtcommands;

import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.command.spec.CommandSpec;


// Plugin info for Sponge
@Plugin(id = "dtc", name = "DTCommands", version = "0.1")

public class DTCommands {
	
	
	// Logging Dependency
	@Inject
	private Logger logger;

	
// Load plugin
	
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    	this.logger.info("Starting DTCommands");
    	
    	
    
    	// Command registering
    	
    // Member
    CommandSpec memberCommand = CommandSpec.builder()
    	    .description(Text.of("Member link command"))
    	    .permission("dtc.member")
    	    .executor(new Member())
    	    .build();
    
    // Website
    CommandSpec websiteCommand = CommandSpec.builder()
    	    .description(Text.of("Website link command"))
    	    .permission("dtc.website")
    	    .executor(new Website())
    	    .build();
    
    // Donate
    CommandSpec donateCommand = CommandSpec.builder()
    	    .description(Text.of("Donation link command"))
    	    .permission("dtc.donate")
    	    .executor(new Donate())
    	    .build();
    
    
    	Sponge.getCommandManager().register(this, memberCommand, "member", "rankup");
    	Sponge.getCommandManager().register(this, websiteCommand, "website", "site");
    	Sponge.getCommandManager().register(this, donateCommand, "donate", "store", "shop");    	
    
    }
}
