# DTCommands
A simple hyperlink-enabled dynamic command plugin

### How does it work?
It uses a configuration file to define simple commands that will popup URLs

```
commands=[
    {
        aliases=[
            website,
            site,
            dragontech
        ]
        commandName=Website
        description="Website command"
        linkText="Click here to go to the website"
        linkUrl="http://www.dragontechmc.com"
        permission="dtc.new"
    },
    {
        aliases=[
            discord
        ]
        commandName=Discord
        description="Discord command"
        linkText="Click here to join the Discord"
        linkUrl="https://discord.gg/Q6mzRPU"
        permission="dtc.new"
    }
]

```
