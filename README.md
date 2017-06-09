# DTCommands
A simple hyperlink-enabled dynamic command plugin

### How does it work?
It uses a configuration file to define simple commands that will popup URLs

```
commands: {
	name: {
		name: "<command_name>",
		description: "<desc>",
		permission: "<permission_string>",
		aliases: ["<alias1>", "<alias2>"],
		link_text: "click here!",
		link_url: "<http://somelink.com>"
	},
	name2: {
		name: "<command_name2>",
		description: "<desc2>",
		permission: "<permission_string2>",
		aliases: ["<alias1>", "<alias2>"],
		link_text: "click here now you newb!",
		link_url: "<http://someotherlink.com>"
	} 
	// ... and so on
}
```
