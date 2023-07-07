import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import listeners.MessageCommandListener;
import listeners.SlashCommandListener;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import util.Constants;
import util.Rizz;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Random;

@Slf4j
public class DiscordBot {
    public static void main(String[] args) {

        // hard coded list of available commands in /resources/commands
        // see https://github.com/Discord4J/example-projects/commit/567ec1c432d9fb7457423e3950a4c2e2ec87319f
        final List<String> commands = List.of("greet.json", "ping.json", "roulette.json",
                "cringe.json",
                "uncringe.json", "randomgif.json",
                "addserver.json", "rizz.json");

        Random random = new Random();
        String discordApiToken = Constants.DISCORD_API_TOKEN.value;
        String owmApiToken = Constants.OWM_API_TOKEN.value;
        String ritoApiToken = Constants.RIOT_API_TOKEN.value;
        long guildId = Long.parseLong(Constants.GUILD_ID.value);
        long appId = Long.parseLong(Constants.APP_ID.value);
        String[] applicationArgs = args;

        log.info("Bot token: " + discordApiToken + " GuildID: " + guildId);

        final GatewayDiscordClient client = DiscordClientBuilder.create(discordApiToken).build().login().block();

        try {
            //use guildcommands for testing because they don't have a one-hour delay
            //new GlobalCommandRegistrar(client.getRestClient()).registerCommands();
            new GuildCommandRegistrar(client.getRestClient()).registerCommands(commands);
        } catch (Exception e) {
            log.error("Failed command registration", e);
        }

        //Daily Rizzsczenski post
        if(args.length > 0) {
            long scheduledTime = 0L;

            try {
                //use java -jar .... Zugbot.jar 19:00:00

                //Todo: check if it works
                scheduledTime = DateFormat.getDateInstance().parse(args[0]).getTime();
                Rizz.scheduleRizzImage(client, scheduledTime);
            } catch (ParseException pe) {
                log.error("Error while parsing scheduled Time for Rizz image. Falling back to current timestamp");
                Rizz.scheduleRizzImage(client, scheduledTime);
            }
        } else {
            Rizz.scheduleRizzImage(client);
        }

        client.on(ChatInputInteractionEvent.class, SlashCommandListener::handle).subscribe();

        client.on(MessageCreateEvent.class, event ->{
            String message = event.getMessage().getContent();
            int bound = 11;

            if (message.contains("11gag")) {
                bound = 11;
                event.getMessage().delete().block();
                log.info("Blocking 9gag");
            }

            if(message.contains("0gag")) {
                bound = 1;
                event.getMessage().delete().block();
                log.info("Unblocking 9gag");
            }

            if(message.contains("9gag")) {
                if(random.nextInt(0, bound) >= 1) {
                    event.getMessage().delete().block();
                    log.info("Message " + event.getMessage().getId().asString() + " by " + event.getMember().get().getDisplayName() + " deleted");
                }
            }

            return Mono.empty();
        }).subscribe();

        client.on(MessageInteractionEvent.class, MessageCommandListener::handle)
                .then(client.onDisconnect())
                .block();
    }
}
