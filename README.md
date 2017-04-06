# DTCommands
A simple hyperlink-enabled dyanamic command plugin

### How does it work?
It uses a configuration file to define simple commands that will popup URLs

```
commands: [
	name: {
		name: "<command_name>",
		description: "<desc>",
		permission: "<permission_string>",
		aliases: ["<alias1>", "<alias2>"],
		link_text: "click here!",
		link_url: "<http://somelink.com>"
	} 
	// ... and so on
]
```
