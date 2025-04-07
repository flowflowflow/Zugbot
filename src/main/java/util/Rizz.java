package util;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public final class Rizz {

    private Rizz() {

    }

    public static String getRizz() {
        final List<String> imagesList = new ArrayList<>();

        imagesList.add("https://cdn.discordapp.com/attachments/1042034256286863410/1061592141622681640/FlfjEF0WAAMo5Xl__v2.jpg");
        imagesList.add("https://cdn.discordapp.com/attachments/1042034256286863410/1060105789731262514/Tom_Familiy_rizz.png");
        imagesList.add("https://cdn.discordapp.com/attachments/1042034256286863410/1062555835345162331/Schizzler.jpg");
        imagesList.add("https://cdn.discordapp.com/attachments/1042034256286863410/1123809968458584074/fee81262-9cd8-4334-ae1c-fad1a7c2bed1.jpg");
        imagesList.add("https://cdn.discordapp.com/attachments/1042034256286863410/1124190087312330802/7377bbb1-78d7-4f68-a60e-4d5f6384e8da_cleanup_waifu2x_photo_noise0_scale_waifu2x_photo_noise0_scale.png");

        Random random = new Random();
        int i = random.nextInt(imagesList.size());
        log.info("List index " + i + " selected");

        return imagesList.get(i);
    }



    public static void scheduleRizzImage(GatewayDiscordClient discordClient) {

        GatewayDiscordClient client = discordClient;
        Date scheduledTime = new Date();
        Timer timer = new Timer();

        timer.schedule(new TimerTask(){
            public void run(){
                MessageChannel channel = (MessageChannel) client.getChannelById(Snowflake.of(509398215074775049l)).block();
                channel.createMessage("<@509384367307751424> <a:Nerdge:1037714990985138186>");
                channel.createMessage("Daily Rizzsczenski post").block();
                channel.createMessage(getRizz()).block();

                log.info("Scheduler created for daily rizzsczenski message");
            }
        },scheduledTime, 24*60*60*1000);
    }

    public static void scheduleRizzImage(GatewayDiscordClient discordClient, long schedTimeMillis) {

        GatewayDiscordClient client = discordClient;
        Date scheduledTime = new Date(schedTimeMillis);
        Timer timer = new Timer();

        timer.schedule(new TimerTask(){
            public void run(){
                MessageChannel channel = (MessageChannel) client.getChannelById(Snowflake.of(509398215074775049l)).block();
                channel.createMessage("Daily Rizzsczenski post <@509384367307751424> <a:Nerdge:1037714990985138186>").block();
                channel.createMessage(getRizz()).block();

                log.info("Scheduler created for daily rizzsczenski message");
            }
        },scheduledTime, 24*60*60*1000);
    }
}
