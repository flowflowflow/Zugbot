package commands;

import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;
import reactor.core.publisher.Mono;

public class CringeCommand implements MessageCommand {

    @Override
    public String getName() {
        return "cringe";
    }

    @Override
    public Mono<Message> handle(MessageInteractionEvent event) {
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
    }
}
