import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.channel.MessageChannel;
import listeners.MessageCommandListener;
import listeners.SlashCommandListener;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import util.Constants;
import util.GuildCommandRegistrar;
import util.Rizz;

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
                "addserver.json"/*, "rizz.json"*/);

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
            //new util.GlobalCommandRegistrar(client.getRestClient()).registerCommands();
            new GuildCommandRegistrar(client.getRestClient()).registerCommands(commands);
        } catch (Exception e) {
            log.error("Failed command registration", e);
        }

        // start bot with rizz as argument
        if(args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].toLowerCase().equals("rizz")) {
                    Rizz.scheduleRizzImage(client);
                }
            }
        }

        client.on(ChatInputInteractionEvent.class, SlashCommandListener::handle).subscribe();

        client.on(MessageCreateEvent.class, event ->{
            String message = event.getMessage().getContent();
            int bound = 11;

            if (message.contains("11gag")) {
                bound = 11;
                event.getMessage().delete().block();
                log.info("Bound for RNG set to 100% (11)");
            }

            if(message.contains("0gag")) {
                bound = 1;
                event.getMessage().delete().block();
                log.info("Bound for RNG set to 0% (1)");
            }

            if(message.contains("9gag")) {
                if(random.nextInt(0, bound) >= 1) {
                    event.getMessage().delete().block();
                    log.info("Message " + event.getMessage().getId().asString() + " by " + event.getMember().get().getDisplayName() + " deleted");
                }
            }

            if(message.contains("https://x.com") || message.contains("https://twitter.com")) {
                MessageChannel channel = event.getMessage().getChannel().block();
                String authorId = event.getMessage().getAuthor().get().getId().asString();
                String authorName = "<@".concat(authorId).concat(">");
                String original = event.getMessage().getContent();
                String urlPath = original.substring(original.indexOf("/", 8));

                event.getMessage().delete().block();
                channel.createMessage(authorName + ": ").block();
                channel.createMessage("https://fxtwitter.com".concat(urlPath)).block();

                log.info("EmbedFix: Changed " + original + " to " + "https://fxtwitter.com".concat(urlPath));
            }

            return Mono.empty();
        }).subscribe();

        client.on(MessageInteractionEvent.class, MessageCommandListener::handle)
                .then(client.onDisconnect())
                .block();
    }
}
