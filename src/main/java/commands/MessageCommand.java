package commands;

import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public interface MessageCommand {
    public String getName();

    public Mono<Message> handle(MessageInteractionEvent event);
}
