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
        final String[] images = new String[7];
        images[0] = "https://cdn.discordapp.com/attachments/1042034256286863410/1058083626232860712/Rizzsczenski.png";
        images[1] = "https://cdn.discordapp.com/attachments/1042034256286863410/1059389197082951680/EkQqMsiU4AAMcus_2.png";
        images[2] = "https://cdn.discordapp.com/attachments/1042034256286863410/1059391516503707678/the-best-korean-musical-dramas-that-you-must-watch.jpg";
        images[3] = "https://cdn.discordapp.com/attachments/1042034256286863410/1061592141622681640/FlfjEF0WAAMo5Xl__v2.jpg";
        images[4] = "https://cdn.discordapp.com/attachments/1042034256286863410/1060105789731262514/Tom_Familiy_rizz.png";
        images[5]  = "https://cdn.discordapp.com/attachments/1042034256286863410/1060823000447975434/tom_rizzle.jpg";
        images[6] = "https://cdn.discordapp.com/attachments/1042034256286863410/1062555835345162331/Schizzler.jpg";

        Random random = new Random();
        int i = random.nextInt(0, 7);
        log.info("Random RizzImages number generated: " + i);

        return images[i];
    }

    public static void scheduleRizzImage(GatewayDiscordClient discordClient) {

        GatewayDiscordClient client = discordClient;
        Date date=new Date();
        Timer timer = new Timer();

        timer.schedule(new TimerTask(){
            public void run(){
                MessageChannel channel = (MessageChannel) client.getChannelById(Snowflake.of(509398215074775049l)).block();
                channel.createMessage("Daily Rizzsczenski post <@509384367307751424> <a:Nerdge:1037714990985138186>").block();
                channel.createMessage(getRizz()).block();

                log.info("Scheduler created for daily rizzsczenski message");
            }
        },date, 24*60*60*1000);
    }
}
