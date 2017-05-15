package com.dragontechmc.dtcommands;

import java.util.List;

import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class CommandDefinition {
	public List<String> aliases;
	
	public String commandName;
	
	public String linkText;
	
	public String linkUrl;
	
	public String description;
	
	public String permission;
}
