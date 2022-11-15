package commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

import java.time.Instant;

public class AddServerCommand implements SlashCommand {

    @Override
    public String getName() {
        return "addserver";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        Mono<MessageChannel> channel = event.getInteraction().getChannel();
        EmbedCreateSpec embed;

        String title = event.getOption("servername")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .get();

        String description = event.getOption("description")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .get();

        String connectionDetails = event.getOption("ip")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .get();

        String password = event.getOption("password")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("");

        String preset = event.getOption("preset")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString).orElse("default");



        switch(preset.toLowerCase()) {
            case "valheim":
                embed = EmbedCreateSpec
                        .builder()
                        .color(Color.BISMARK)
                        .title(title)
                        .author("Zugbot", "https://discord4j.com", "https://cdn.discordapp.com/attachments/1042034256286863410/1042059572363411577/Md3E3Sp_-_Copy.png")
                        .description(description)
                        .thumbnail("https://cdn.discordapp.com/attachments/1042034256286863410/1042062301416656977/valheim_icon.png")
                        .addField("IP address", connectionDetails, false)
                        .addField("Password", password, false)
                        .image("https://cdn.discordapp.com/attachments/1042034256286863410/1042063270355415060/capsule_616x353.jpg")
                        .timestamp(Instant.now())
                        .build();
                break;
            case "minecraft":
                embed = EmbedCreateSpec
                        .builder()
                        .color(Color.BISMARK)
                        .title(title)
                        .author("Zugbot", "https://discord4j.com", "https://cdn.discordapp.com/attachments/1042034256286863410/1042059572363411577/Md3E3Sp_-_Copy.png")
                        .description(description)
                        .thumbnail("https://cdn.discordapp.com/attachments/871187487815503902/984999860585521162/pack__1_.jpg")
                        .addField("IP address", connectionDetails, false)
                        .addField("Password", password, false)
                        .image("https://cdn.discordapp.com/attachments/1042034256286863410/1042065561804345504/minecraft-1-19-the-wild-update-will-be-officially-released-on-june-7.jpg")
                        .timestamp(Instant.now())
                        .build();
                break;
            default:
                embed = EmbedCreateSpec
                        .builder()
                        .color(Color.BISMARK)
                        .title(title)
                        .author("Zugbot", "https://discord4j.com", "https://cdn.discordapp.com/attachments/1042034256286863410/1042059572363411577/Md3E3Sp_-_Copy.png")
                        .description(description)
                        .thumbnail("https://cdn.discordapp.com/attachments/1042034256286863410/1042060328483164220/3208726.png")
                        .addField("IP address", connectionDetails, false)
                        .addField("Password", password, false)
                        .timestamp(Instant.now())
                        .build();
        }

        channel.ofType(GuildMessageChannel.class).flatMap(messageChannel -> messageChannel.createMessage(embed)).subscribe();

        return event.reply().withEphemeral(true).withContent("Embed created!");
    }
}
