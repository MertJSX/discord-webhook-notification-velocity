package org.discordwebhooknotificationvelocity.discordwebhooknotificationvelocity;

public class Embed {
    public String title;
    public String description;
    public String color;
    public Thumbnail thumbnail;

    public Embed(String title, String description, String color, Thumbnail thumbnail) {
        this.title = title;
        this.description = description;
        this.color = color;
//        if (color == null) {
//            this.color = Integer.valueOf(color);
//        } else {
//            this.color = 16405074;
//        }

        this.thumbnail = thumbnail;
    }


}
