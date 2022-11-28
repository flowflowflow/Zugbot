package commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class MatchHistoryCommand implements SlashCommand {
    @Override
    public String getName() {
        return "matchhistory";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {


        return null;
    }
}
