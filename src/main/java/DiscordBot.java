import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.User;
import listeners.MessageCommandListener;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;
import util.IOHelper;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

@Log
public class DiscordBot {
    public static void main(String[] args) {

        // hard coded list of available commands in /resources/commands
        // see https://github.com/Discord4J/example-projects/commit/567ec1c432d9fb7457423e3950a4c2e2ec87319f
        final List<String> commands = List.of("greet.json", "ping.json", "roulette.json", "cringe.json", "weather.json");

        String discordApiToken = null;
        String owmApiToken = null;
        String ritoApiToken = null;
        long guildId = 0L;
        long appId = 0L;

        IOHelper io = null;
        Random random = new Random();

        try {
            io = new IOHelper();


            discordApiToken = io.readDiscordApiToken();
            owmApiToken = io.readOwmApiToken();
            ritoApiToken = io.readRitoApiToken();
            guildId = io.readGuildId();
            appId = io.readAppId();

        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to load properties", e);
            System.exit(-1);
        }

        log.info("Bot token: " + discordApiToken + " GuildID: " + guildId);

        final GatewayDiscordClient client = DiscordClientBuilder.create(discordApiToken).build().login().block();

        try {
            //use guildcommands for testing because they don't have a one-hour delay
            new GuildCommandRegistrar(client.getRestClient(), io).registerCommands(commands);
            //new GlobalCommandRegistrar(client.getRestClient()).registerCommands();
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed command registration", e);
        }


        client.on(ChatInputInteractionEvent.class, event -> {

            if(event.getCommandName().equals("greet")) {
                String name = event.getOption("name")
                        .flatMap(ApplicationCommandInteractionOption::getValue)
                        .map(ApplicationCommandInteractionOptionValue::asString)
                        .orElse("stranger");
                return event.reply("Hi " + name + "! Nice to meet you :^)");
            }

            if(event.getCommandName().equals("roulette")) {
                int rndInt = random.nextInt(100) + 1;
                String userId = event.getInteraction().getMember().get().getId().asString();

                Mono<User> user = event.getOption("name")
                        .flatMap(ApplicationCommandInteractionOption::getValue)
                        .map(ApplicationCommandInteractionOptionValue::asUser).get();




                System.out.println("command sender: " +  userId);
            }
            return Mono.empty();
        }).subscribe();

        client.on(MessageInteractionEvent.class, MessageCommandListener::handle);

        client.on(MessageCreateEvent.class, event -> {
            Member member = event.getMember().get();
            String memberId = member.getId().asString();
            System.out.println("New message from " + member.getDisplayName() + " / " + member.getNickname().get() +  " / " + memberId);
            return Mono.empty();
        }).subscribe();

        client.onDisconnect().block();

    }
}
