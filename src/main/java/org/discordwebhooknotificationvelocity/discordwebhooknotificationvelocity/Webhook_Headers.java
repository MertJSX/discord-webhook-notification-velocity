package org.discordwebhooknotificationvelocity.discordwebhooknotificationvelocity;

public class Webhook_Headers {
    public String content;
    public Embed[] embeds;
    public Webhook_Headers (String content, Embed[] embeds) {
        this.content = content;
        this.embeds = embeds;
    }
}
