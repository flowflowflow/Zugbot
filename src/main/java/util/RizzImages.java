package util;

import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.object.entity.channel.MessageChannel;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


@Slf4j
public final class RizzImages {

    private RizzImages() {

    }

    public static String getRizz() {
        final String[] images = new String[3];
        images[0] = "https://cdn.discordapp.com/attachments/1042034256286863410/1058083626232860712/Rizzsczenski.png";
        images[1] = "https://cdn.discordapp.com/attachments/1042034256286863410/1059389197082951680/EkQqMsiU4AAMcus_2.png";
        images[2] = "https://cdn.discordapp.com/attachments/1042034256286863410/1059391516503707678/the-best-korean-musical-dramas-that-you-must-watch.jpg";

        Random random = new Random();
        int i = random.nextInt(0, 3);

        return images[i];
    }

    public static void scheduleRizzImage(GatewayDiscordClient discordClient) {

        GatewayDiscordClient client = discordClient;
        Date date=new Date();
        Timer timer = new Timer();

        timer.schedule(new TimerTask(){
            public void run(){
                MessageChannel channel = (MessageChannel) client.getChannelById(Snowflake.of(509398215074775049l)).block();
                channel.createMessage("Daily Rizzsczenski post <a:Nerdge:1037714990985138186>").block();
                channel.createMessage(getRizz()).block();

                log.info("Scheduler created daily rizzsczenski message");
            }
        },date, 24*60*60*1000);
    }
}
