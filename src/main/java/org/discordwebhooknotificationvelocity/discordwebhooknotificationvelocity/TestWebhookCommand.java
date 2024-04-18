package org.discordwebhooknotificationvelocity.discordwebhooknotificationvelocity;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TestWebhookCommand implements SimpleCommand {

    public Discord_webhook_notification_velocity dwn;

    public TestWebhookCommand (Discord_webhook_notification_velocity dwn) {
        this.dwn = dwn;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();

        if (!(source instanceof Player)) {
            source.sendPlainMessage("Webhooks are sent.");
            try {
                URL url = new URL(dwn.Webhook);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoOutput(true);

                // String json = "{\"content\":\"null\",\"embeds\":[{\"title\":\"CalamariaRPG\",\"description\":\"Merhaba! Sunucu başlatıldı, artık herkes girebilir! :D\\n\\nIP: `calamariarpg.elixir.host`\\n\\nSürüm: `1.16.5` \\n\\n**UYARI!** Eğer bir sorun ile karşılaşırsanız yetkililere haber vermenizi öneririz!\",\"color\":3238202,\"thumbnail\":{\"url\":\"https://cdn.discordapp.com/attachments/853315527607189536/1228699243125014550/server-icon.png?ex=662cfe57&is=661a8957&hm=2efa429b4a957de5c4b60781a4e37ad819b1a8a36ad8bcea0c86f6427b42d7d3&\"}}]}";


                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    wr.write(dwn.onStartHeadersJSON.getBytes(StandardCharsets.UTF_8));
                }

                int responseCode = connection.getResponseCode();
            } catch (Exception e) {
                source.sendPlainMessage("Error on start webhook request! Try to change config.toml! Then run dwn-reload!");
            }

            try {
                URL url = new URL(dwn.Webhook);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setDoOutput(true);

                // String json = "{\"content\":\"null\",\"embeds\":[{\"title\":\"CalamariaRPG\",\"description\":\"Merhaba! Sunucu başlatıldı, artık herkes girebilir! :D\\n\\nIP: `calamariarpg.elixir.host`\\n\\nSürüm: `1.16.5` \\n\\n**UYARI!** Eğer bir sorun ile karşılaşırsanız yetkililere haber vermenizi öneririz!\",\"color\":3238202,\"thumbnail\":{\"url\":\"https://cdn.discordapp.com/attachments/853315527607189536/1228699243125014550/server-icon.png?ex=662cfe57&is=661a8957&hm=2efa429b4a957de5c4b60781a4e37ad819b1a8a36ad8bcea0c86f6427b42d7d3&\"}}]}";


                try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                    wr.write(dwn.onStopHeadersJSON.getBytes(StandardCharsets.UTF_8));
                }

                int responseCode = connection.getResponseCode();
            } catch (Exception e) {
                source.sendPlainMessage("Error on stop webhook request! Try to change config.toml! Then run dwn-reload!");
            }
        }




    }
}
