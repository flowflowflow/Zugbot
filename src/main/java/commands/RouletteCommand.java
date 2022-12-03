package commands;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.channel.VoiceChannel;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;
import util.Constants;

import java.util.Random;

@Log
public class RouletteCommand implements SlashCommand {
    @Override
    public String getName() {
        return "roulette";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        Random random = new Random();

        int rndInt = random.nextInt(100) + 1;

        String commandSender = event.getInteraction().getMember().get().getDisplayName();

        Mono<Member> user =  event.getOption("name")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asUser).get().block().asMember(Snowflake.of(Constants.GUILD_ID.value));

        Member member = user.block().asFullMember().block();

        final VoiceState voiceState = member.getVoiceState().block();

        if(voiceState != null) {
            final VoiceChannel channel = voiceState.getChannel().block();
            if(channel != null) {
                if(rndInt < 20) {
                    member.edit().withNewVoiceChannelOrNull(null).block();

                    return event.reply().withContent(commandSender + " hat die Zahl " + rndInt + " gewürfelt. Das bedeutet " + member.getDisplayName() + " wurde erschossen. RIP Bozo");
                }
            }
        }
        return event.reply().withContent("Leider ist " + member.getDisplayName() + " davongekommen.. (" + rndInt + ")");
    }
}
