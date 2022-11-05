package listeners;

import commands.CringeCommand;
import commands.MessageCommand;
import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class MessageInteractionListener {
    private final static List<MessageCommand> commands = new ArrayList<>();

    static {
        commands.add(new CringeCommand());
    }

    public static Mono<Message> handle(MessageInteractionEvent event) {
        Mono<Message> message;

        message = Flux.fromIterable(commands)
                .filter(command -> command.getName().equals(event.getCommandName()))
                .next()
                .flatMap(command -> command.handle(event));

        return message;
    }

}
