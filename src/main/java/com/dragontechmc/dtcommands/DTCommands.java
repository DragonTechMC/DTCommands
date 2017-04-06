package com.dragontechmc.dtcommands;

import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;

import com.google.inject.Inject;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.command.CommandMapping;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;

// Plugin info for Sponge
@Plugin(id = "dtc", name = "DTCommands", version = "0.1")
public class DTCommands {

	// Logging Dependency
	@Inject
	private Logger logger;

	// This uses Guice (the DI framework that Sponge is using) and a Sponge specific annotation (@DefaultConfig)
	// to tell Guice to provide to us the path to the config file for our plugin. 
	// If we use "sharedRoot = true" it returns a full filename (called the same thing as the plugin)
	// We only need 1 config file, so this is fine for this plugin. For more complex plugins, you might want
	// to get the path to the config directory, not a filename, in which case use sharedRoot = false
	@Inject 
	@DefaultConfig(sharedRoot = true)
	private Path defaultConfig;

	// We will use this list to track and commands that we have registered so that we can unregister them
	// when you want to reload the config
	private ArrayList<CommandMapping> mappings = new ArrayList<CommandMapping>();

	// Load plugin
	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		this.logger.info("Starting DTCommands");

		// Command registering
		// CP - instead of registering commands in code, we are going to read the configuration file
		// and use that. This way you can create loads more links in the file and it's flexible.
		// 
		// The config file will look something like this:
		// commands: [
		//   {
		//	   name: "<command_name>",
		//     aliases: ["<alias1>", "<alias2>"],
		//	   link_text: "<http://somelink.com>"
		//   }, 
		//	 ... and so on
		// ]
		// 
		// 
		// The file just contains an array of command definitions. We can loop over these and use them
		// to build the commands...

		// First we will build a "reload" command that will allow someone to reload the configuration
		// after they have changed the config
		CommandSpec reloadCommand = CommandSpec.builder()
				.description(Text.of("Reload all plugin configuration"))
				.permission("dtc.admin")
				.executor(new ReloadCommand(this))
				.build();

		Sponge.getCommandManager().register(this, reloadCommand, "reloadcommands");

		// Register any dynamic commands
		registerCommands();
	}

	public void registerCommands() {
		// We made this a separate function so that it could be run from either the server startup
		// or from the "reload" command

		try {
			// Try and load the config file by creating a loader
			HoconConfigurationLoader loader = HoconConfigurationLoader
					.builder() 				// Get a builder for the loader, lets you build a loader :D
					.setPath(defaultConfig) 	// Set the directory the loader will point to to try and load
					.build();				// Build the loader (gives you a ready-to-go loader instance)

			CommentedConfigurationNode rootNode = loader.load();				// Use the built loader to try and load the config

			// Now we have the loaded config file in a tree - we start at the root and we can 
			// look down the tree to any level by navigating through the child nodes

			// First save the config to the disk, just in case this is a first-run and we have no config file yet
			loader.save(rootNode);

			// Er... this one might require some explaining in chat but I'll try :D
			// First we want to get the "commands" node from the file
			CommentedConfigurationNode commandNode = rootNode.getNode("commands");

			// The node may be "virtual" - this means that it didn't exist (ask me about this)
			// Make sure we didn't get a virtual node...
			if(!commandNode.isVirtual()) {

				// Since the command node holds an array of commands, we can pull them back as a list
				// This looks a little confusing but essentially, the node has a generic function called "GetList"
				// (ask me about generics/generic functions later!)
				// We can say that we want it to give us back a list of "CommandDefinition" objects but it needs
				// to know how to transform all the textual data into one of those
				// We provide a new function that takes an "Object" and returns a "CommandDefinition". 
				// This "apply" function runs for each item in the list and the getList function pulls it all together
				List<CommandDefinition> commands = commandNode.<CommandDefinition>getList(new Function<Object, CommandDefinition>() {
					// The transformer object requires that you implement an "apply" function that will run on each
					// item in the list. It returns each item as a "LinkedHashMap" (essentially a dictionary of keys -> values)
					public CommandDefinition apply(Object node) {
						// We know the object is a linkedhashmap so cast it
						LinkedHashMap configNode = (LinkedHashMap)node;

						// Create a new CommandDefinition
						CommandDefinition def = new CommandDefinition();

						// The node is just a load of strings (apart from aliases which is a list of strings)
						// Pull each property out by its key

						// We should probably error check here - maybe see if the key exists
						// first in case people forgot to put a description etc in
						def.commandName = (String)configNode.get("name");
						def.description = (String)configNode.get("description");
						def.permission = (String)configNode.get("permission");
						def.aliases = (List<String>)configNode.get("aliases");
						def.linkText = (String)configNode.get("link_text");
						def.linkUrl = (String)configNode.get("link_url");

						return def; 
					}				
				});

				// I've create a nice "CommandDefinition" object which just takes the configuration
				// data and packages it up neatly - just makes it easier to work with later
				for(CommandDefinition def : commands) {
					logger.info("Attempting to register command: " + def.commandName);

					// Make sure there was link text/url or raise a warning in console
					if(def.linkText == null || def.linkText.isEmpty() || def.linkUrl == null || def.linkUrl.isEmpty()) {
						logger.warn("Could not register command: " + def.commandName + " - there was no link text/URL");
						continue;
					}

					// Create a builder for the command
					CommandSpec.Builder commandBuilder = CommandSpec.builder();

					// Check that there's a description
					if(def.description != null && !def.description.isEmpty() )
						commandBuilder.description(Text.of(def.description));

					// Check if it had permissions
					if(def.permission != null && !def.permission.isEmpty())
						commandBuilder.permission(def.permission);
					
					// Build the command
					CommandSpec dynamicCommand = commandBuilder.executor(new HyperlinkCommand(def.linkText, new URL(def.linkUrl))).build();

					// Since a command may conflict with others, adding a mapping may fail
					// Sponge returns an "Optional" which either has a value (success) or has no value (failed)
					Optional<CommandMapping> mappingOpt = Sponge.getCommandManager().register(this, dynamicCommand, def.aliases);

					// If there was a value...
					if(mappingOpt.isPresent()) {
						// Add this command mapping to the list to track it
						mappings.add(mappingOpt.get());
					} else {
						// Otherwise notify the admin in the console that something is up
						logger.warn("Command " + def.commandName + " could not be registered due to a conflict");
					}
				}
			}
		} catch(Exception e) {
			// Something went horribly wrong, put an error in the console
			logger.error(e.getMessage(), e);
		}
	}

	public void removeCommands() {
		// Loop over each mapping and remove it
		for(CommandMapping mapping : mappings) {
			Sponge.getCommandManager().removeMapping(mapping);
		}
	}

}
