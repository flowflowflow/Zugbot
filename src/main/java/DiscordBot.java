import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.reaction.ReactionEmoji;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;
import util.IOHelper;

import java.io.IOException;
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
            new GuildCommandRegistrar(client.getRestClient(), io).registerCommands();
            //new GlobalCommandRegistrar(client.getRestClient()).registerCommands();
        } catch (Exception e) {
            log.log(Level.WARNING, "Failed command registration", e);
        }

 /*
        client.on(ChatInputInteractionEvent.class, event -> {

            if(event.getCommandName().equals("greet")) {
                String name = event.getOption("name")
                        .flatMap(ApplicationCommandInteractionOption::getValue)
                        .map(ApplicationCommandInteractionOptionValue::asString)
                        .orElse("stranger");
                return event.reply("Hi " + name + "! Nice to meet you :^)");
            }

            if(event.getCommandName().equals("roulette")) {
                Double rndDouble = random.nextDouble();
                String userId = event.getInteraction().getMember().get().getId().asString();
                Member member = event.getInteraction().getMember().get();

                System.out.println("command sender: " +  userId);
                System.out.println("rolled a: " +  rndDouble);

                if(rndDouble < 0.9) {

                }
            }

            return Mono.empty();
        }).subscribe();
*/

        client.on(MessageInteractionEvent.class, event -> {
            Member member = event.getInteraction().getMember().get();
            String memberName = member.getDisplayName();

            if (event.getCommandName().equals("Cringe")) {
                System.out.println("New cringe issued by " + memberName);
                return event.deferReply().withEphemeral(true)
                        .then(event.getTargetMessage())
                        .flatMap(it -> it.addReaction(ReactionEmoji.unicode("\uD83C\uDDE8")))
                        .then(event.getTargetMessage())
                        .flatMap(it -> it.addReaction(ReactionEmoji.unicode("\uD83C\uDDF7")))
                        .then(event.getTargetMessage())
                        .flatMap(it -> it.addReaction(ReactionEmoji.unicode("\uD83C\uDDEE")))
                        .then(event.getTargetMessage())
                        .flatMap(it -> it.addReaction(ReactionEmoji.unicode("\uD83C\uDDF3")))
                        .then(event.getTargetMessage())
                        .flatMap(it -> it.addReaction(ReactionEmoji.unicode("\uD83C\uDDEC")))
                        .then(event.getTargetMessage())
                        .flatMap(it -> it.addReaction(ReactionEmoji.unicode("\uD83C\uDDEA")))
                        .then(event.editReply("Done!"));
            }
            return Mono.empty();
        }).subscribe();

        client.on(MessageCreateEvent.class, event -> {
            Member member = event.getMember().get();
            String memberId = member.getId().asString();
            System.out.println("New message from " + member.getDisplayName() + " / " + member.getNickname().get().toString() +  " / " + memberId);

            member = null;
            memberId = null;
            return Mono.empty();
        }).subscribe();

        client.onDisconnect().block();

    }
}
