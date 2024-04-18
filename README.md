# Minecraft plugin for Discord Webhook notification on starting the Velocity server

## Example ```config.toml```
Config is simple paste your discord webhook url to the ```webhook = "yourwebhook"``` then change settings! This is the same thing as the Discord webhook API!
If you don't change the ```config.toml``` file, the plugin is not going to work!

```toml
webhook = "yourwebhook"

[on_start]
    content = "Server has started!"
    [[on_start.embeds]]
        title = "ExampleServer"
        description = "Join NOW!\nip: play.example.com"
        color = "3238202"
        [on_start.embeds.thumbnail]
            url = "https://cdn-icons-png.flaticon.com/512/5110/5110487.png"

[on_stop]
    content = "Server has stopped!"
    [[on_stop.embeds]]
        title = "ExampleServer"
        description = "Sorry! :/\nip: play.example.com"
        color = "16405074"
        [on_stop.embeds.thumbnail]
            url = "https://cdn-icons-png.flaticon.com/512/5110/5110487.png"

```

Example:

<img src="https://cdn.discordapp.com/attachments/853315527607189536/1230618812232241264/image.png?ex=6633fa14&is=66218514&hm=14be8969963ff60ec759ff661dd1e928b78e9ce9b4b386fa768629143155f835&" width="530px" height="150px">
