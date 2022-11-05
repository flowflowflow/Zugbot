package commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class WeatherCommand implements SlashCommand {
    public String getName() {
        return "weather";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event
                .reply()
                .withEphemeral(true)
                .withContent("not implemented yet :^)");
    }
}
