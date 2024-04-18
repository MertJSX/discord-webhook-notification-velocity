package org.discordwebhooknotificationvelocity.discordwebhooknotificationvelocity;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.moandjiezana.toml.Toml;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Plugin(
        id = "discord-webhook-notification-velocity",
        name = "Discord Webhook Notification",
        version = "1.0.0",
        authors = {"MertJSX"},
        description = "This plugin sends a webhook on server start and on server shutdown."
)
public class Discord_webhook_notification_velocity {

    private final Logger logger;

    private final ProxyServer proxy;

    public Path folder = null;

    public String Webhook;

    public Webhook_Headers onStartHeaders;

    public Webhook_Headers onStopHeaders;

    public String onStartHeadersJSON;

    public String onStopHeadersJSON;

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        CommandManager commandManager = proxy.getCommandManager();

        CommandMeta ReloadMeta = commandManager.metaBuilder("dwn-reload").build();
        commandManager.register(ReloadMeta, new ReloadCommand(this, folder));

        CommandMeta TestMeta = commandManager.metaBuilder("dwn-test").build();
        commandManager.register(TestMeta, new TestWebhookCommand(this));
    }

    public Toml loadConfig(Path path) {
        File folder = path.toFile();


        File file = new File(folder, "config.toml");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try (InputStream input = Discord_webhook_notification_velocity.class.getResourceAsStream("/" + file.getName())) {
                if (input != null) {
                    Files.copy(input, file.toPath());
                } else {
                    file.createNewFile();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                return null;
            }
        }

        return new Toml().read(file);
    }

    public void LoadConfigVars() {
        Toml toml = loadConfig(folder);
        Embed[] startEmbeds = new Embed[toml.getList("on_start.embeds").size()];
        for (int i = 0;i < startEmbeds.length;i++) {
            startEmbeds[i] = new Embed(
                    toml.getString("on_start.embeds[" + i +"].title"),
                    toml.getString("on_start.embeds[" + i +"].description"),
                    String.valueOf(toml.getString("on_start.embeds[" + i + "].color")),
                    new Thumbnail(toml.getString("on_start.embeds[" + i + "].thumbnail.url"))
            );
        }
        Embed[] stopEmbeds = new Embed[toml.getList("on_start.embeds").size()];
        for (int i = 0;i < stopEmbeds.length;i++) {
            stopEmbeds[i] = new Embed(
                    toml.getString("on_stop.embeds[" + i +"].title"),
                    toml.getString("on_stop.embeds[" + i +"].description"),
                    String.valueOf(toml.getString("on_stop.embeds[" + i + "].color")),
                    new Thumbnail(toml.getString("on_stop.embeds[" + i + "].thumbnail.url"))
            );
        }
        onStartHeaders = new Webhook_Headers(toml.getString("on_start.content"), startEmbeds);
        onStopHeaders = new Webhook_Headers(toml.getString("on_stop.content"), stopEmbeds);
        Webhook = toml.getString("webhook");
    }

    @Inject
    public Discord_webhook_notification_velocity(ProxyServer proxy, Logger logger, @DataDirectory final Path folder) {
        this.proxy = proxy;
        this.logger = logger;
        this.folder = folder;

        Toml toml = loadConfig(folder);
        if (toml == null) {
            logger.warn("Failed to load config.toml. Shutting down.");
            return;
        }

        LoadConfigVars();

        // JSONObject jsonObject = new JSONObject();

        // jsonObject.put("content", onStartHeaders.content);
        // jsonObject.put("embeds", onStartHeaders.embeds);

        logger.info(onStartHeaders.embeds[0].toString());

        Gson gson = new Gson();

        onStartHeadersJSON = gson.toJson(onStartHeaders);

        onStopHeadersJSON = gson.toJson(onStopHeaders);

        try {
            URL url = new URL(Webhook);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            // String json = "{\"content\":\"null\",\"embeds\":[{\"title\":\"CalamariaRPG\",\"description\":\"Merhaba! Sunucu başlatıldı, artık herkes girebilir! :D\\n\\nIP: `calamariarpg.elixir.host`\\n\\nSürüm: `1.16.5` \\n\\n**UYARI!** Eğer bir sorun ile karşılaşırsanız yetkililere haber vermenizi öneririz!\",\"color\":3238202,\"thumbnail\":{\"url\":\"https://cdn.discordapp.com/attachments/853315527607189536/1228699243125014550/server-icon.png?ex=662cfe57&is=661a8957&hm=2efa429b4a957de5c4b60781a4e37ad819b1a8a36ad8bcea0c86f6427b42d7d3&\"}}]}";


            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(onStartHeadersJSON.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();
        } catch (Exception e) {
            logger.warn("Error on web request! Try to change config.toml! Shutting down...");
            Optional<PluginContainer> container = proxy.getPluginManager().getPlugin("discord-webhook-notification-velocity");
            container.ifPresent(pluginContainer -> pluginContainer.getExecutorService().shutdown());
            return;
        }

        logger.info("Webhook has been sent!");

    }

    @Subscribe
    public void onDisable(ProxyShutdownEvent event) {
        try {
            URL url = new URL(Webhook);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // String jsonInputString = "{\"content\":\"Sunucu kapandı!\",\"embeds\":[{\"title\":\"CalamariaRPG\",\"description\":\"Merhaba! Sunucu kapandı!\\n\\nIP: `calamariarpg.elixir.host`\\n\\nSürüm: `1.16.5` \\n\\n**UYARI!** Eğer bir sorun ile karşılaşırsanız yetkililere haber vermenizi öneririz!\",\"color\":16405074,\"thumbnail\":{\"url\":\"https://cdn.discordapp.com/attachments/853315527607189536/1228699243125014550/server-icon.png?ex=662cfe57&is=661a8957&hm=2efa429b4a957de5c4b60781a4e37ad819b1a8a36ad8bcea0c86f6427b42d7d3&\"}}]}";

            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(onStopHeadersJSON.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);
        } catch (Exception e) {

            Optional<PluginContainer> container = proxy.getPluginManager().getPlugin("discord-webhook-notification-velocity");
            container.ifPresent(pluginContainer -> pluginContainer.getExecutorService().shutdown());
        }
    }
}
