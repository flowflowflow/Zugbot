import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import listeners.MessageCommandListener;
import listeners.SlashCommandListener;
import lombok.extern.slf4j.Slf4j;
import util.Constants;

import java.util.List;
import java.util.Random;

@Slf4j
public class DiscordBot {
    public static void main(String[] args) {

        // hard coded list of available commands in /resources/commands
        // see https://github.com/Discord4J/example-projects/commit/567ec1c432d9fb7457423e3950a4c2e2ec87319f
        final List<String> commands = List.of("greet.json", "ping.json", "roulette.json",
                "cringe.json", "weather.json", "play.json",
                "uncringe.json", "randomgif.json",
                "addserver.json");

        Random random = new Random();

        String discordApiToken = Constants.DISCORD_API_TOKEN.value;
        String owmApiToken = Constants.OWM_API_TOKEN.value;
        String ritoApiToken = Constants.RIOT_API_TOKEN.value;
        long guildId = Long.parseLong(Constants.GUILD_ID.value);
        long appId = Long.parseLong(Constants.APP_ID.value);

        log.info("Bot token: " + discordApiToken + " GuildID: " + guildId);

        final GatewayDiscordClient client = DiscordClientBuilder.create(discordApiToken).build().login().block();

        try {
            //use guildcommands for testing because they don't have a one-hour delay
            //new GlobalCommandRegistrar(client.getRestClient()).registerCommands();
            new GuildCommandRegistrar(client.getRestClient()).registerCommands(commands);
        } catch (Exception e) {
            log.error("Failed command registration", e);
        }

        client.on(ChatInputInteractionEvent.class, SlashCommandListener::handle).subscribe();

        client.on(MessageInteractionEvent.class, MessageCommandListener::handle)
                .then(client.onDisconnect())
                .block();
    }
}
