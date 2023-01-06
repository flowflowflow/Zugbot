package commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;
import util.RizzImages;

public class RizzCommand implements SlashCommand {
    @Override
    public String getName() {
        return "rizz";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event
                .reply(RizzImages.getRizz());
    }
}
