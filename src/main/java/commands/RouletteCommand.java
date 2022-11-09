package commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.User;
import reactor.core.publisher.Mono;

import java.util.Random;

public class RouletteCommand implements SlashCommand {
    @Override
    public String getName() {
        return "roulette";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        Random random = new Random();

        int rndInt = random.nextInt(100) + 1;
        String userId = event.getInteraction().getMember().get().getId().asString();

        Mono<User> user = event.getOption("name")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asUser).get();

        System.out.println("command sender: " + userId);

        return event.reply().withEphemeral(true).withContent("Done");
    }
}
