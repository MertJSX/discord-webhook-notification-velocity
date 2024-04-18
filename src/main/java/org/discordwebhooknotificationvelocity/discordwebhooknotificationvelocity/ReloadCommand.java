package org.discordwebhooknotificationvelocity.discordwebhooknotificationvelocity;

import com.google.gson.Gson;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import java.io.File;
import java.nio.file.Path;

public class ReloadCommand implements SimpleCommand {
    private Path folder;
    public Discord_webhook_notification_velocity dwn;

    public ReloadCommand (Discord_webhook_notification_velocity dwn, @DataDirectory final Path folder) {
        this.folder = folder;
        this.dwn = dwn;
    }
    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        if (!(source instanceof Player)) {
            Toml toml = config(folder);
            Embed[] startEmbeds = new Embed[toml.getList("on_start.embeds").size()];
            for (int i = 0;i < startEmbeds.length;i++) {
                startEmbeds[i] = new Embed(
                        toml.getString("on_start.embeds[" + i +"].title"),
                        toml.getString("on_start.embeds[" + i +"].description"),
                        String.valueOf(toml.getString("on_start.embeds["+i+"].color")),
                        new Thumbnail(toml.getString("on_start.embeds["+i+"].thumbnail.url"))
                );
            }
            Embed[] stopEmbeds = new Embed[toml.getList("on_start.embeds").size()];
            for (int i = 0;i < stopEmbeds.length;i++) {
                stopEmbeds[i] = new Embed(
                        toml.getString("on_stop.embeds[" + i +"].title"),
                        toml.getString("on_stop.embeds[" + i +"].description"),
                        String.valueOf(toml.getString("on_stop.embeds["+i+"].color")),
                        new Thumbnail(toml.getString("on_stop.embeds["+i+"].thumbnail.url"))
                );
            }
            dwn.onStartHeaders = new Webhook_Headers(toml.getString("on_start.content"), startEmbeds);
            dwn.onStopHeaders = new Webhook_Headers(toml.getString("on_stop.content"), stopEmbeds);
            dwn.Webhook = toml.getString("webhook");

            Gson gson = new Gson();

            dwn.onStartHeadersJSON = gson.toJson(dwn.onStartHeaders);

            dwn.onStopHeadersJSON = gson.toJson(dwn.onStopHeaders);

            source.sendPlainMessage("Reloaded successfully!");
        }
    }

    private Toml config(Path path) {
        File folder = path.toFile();
        File file = new File(folder, "config.toml");
        return new Toml().read(file);
    }
}
