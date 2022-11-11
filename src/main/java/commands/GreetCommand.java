package commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.Member;
import reactor.core.publisher.Mono;

public class GreetCommand implements SlashCommand {

    @Override
    public String getName() {
        return "Greet";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        Member member = event.getInteraction().getMember().get();
        String name = member.getDisplayName();

        return event.reply()
                .withEphemeral(true)
                .withContent("Hi, nice to meet you " + name);
    }
}
