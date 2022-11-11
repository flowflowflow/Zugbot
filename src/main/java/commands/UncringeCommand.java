package commands;

import discord4j.core.event.domain.interaction.MessageInteractionEvent;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.reaction.ReactionEmoji;
import reactor.core.publisher.Mono;

public class UncringeCommand implements MessageCommand {

    @Override
    public String getName() {
        return "Uncringe";
    }

    @Override
    public Mono<Message> handle(MessageInteractionEvent event) {
        Member member = event.getInteraction().getMember().get();
        String memberName = member.getDisplayName();

        System.out.println("New uncringe issued by " + memberName);
        return event.deferReply()
                .withEphemeral(true)
                .then(event.getTargetMessage())
                .flatMap(it -> it.removeSelfReaction(ReactionEmoji.unicode("\uD83C\uDDE8")))
                .then(event.getTargetMessage())
                .flatMap(it -> it.removeSelfReaction(ReactionEmoji.unicode("\uD83C\uDDF7")))
                .then(event.getTargetMessage())
                .flatMap(it -> it.removeSelfReaction(ReactionEmoji.unicode("\uD83C\uDDEE")))
                .then(event.getTargetMessage())
                .flatMap(it -> it.removeSelfReaction(ReactionEmoji.unicode("\uD83C\uDDF3")))
                .then(event.getTargetMessage())
                .flatMap(it -> it.removeSelfReaction(ReactionEmoji.unicode("\uD83C\uDDEC")))
                .then(event.getTargetMessage())
                .flatMap(it -> it.removeSelfReaction(ReactionEmoji.unicode("\uD83C\uDDEA")))
                .then(event.editReply("Done!"));
    }
}
