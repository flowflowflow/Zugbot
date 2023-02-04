package commands;

import audio.LavaPlayerAudioProvider;
import audio.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.playback.NonAllocatingAudioFrameBuffer;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.VoiceState;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import discord4j.core.object.entity.Member;
import discord4j.voice.AudioProvider;
import reactor.core.publisher.Mono;

public class PlayCommand implements SlashCommand {

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {

        // Creates AudioPlayer instances and translates URLs to AudioTrack instances
        final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
        // This is an optimization strategy that Discord4J can utilize. It is not important to understand
        playerManager.getConfiguration().setFrameBufferFactory(NonAllocatingAudioFrameBuffer::new);
        // Allow playerManager to parse remote sources like YouTube links
        AudioSourceManagers.registerRemoteSources(playerManager);
        // Create an AudioPlayer so Discord4J can receive audio data
        final AudioPlayer player = playerManager.createPlayer();
        // We will be creating LavaPlayerAudioProvider in the next step
        AudioProvider provider = new LavaPlayerAudioProvider(player);

        final TrackScheduler scheduler = new TrackScheduler(player);

        this.connectToVoice(event, provider);

        String youtubeLink = event.getOption("input")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("");

        if(!youtubeLink.isEmpty()) {
            return Mono.justOrEmpty(playerManager.loadItem(youtubeLink, scheduler)).then(event.reply("Playing " + youtubeLink));
        } else {
            return event.reply("Post a valid youtube link you monkey");
        }


        /*
        return Mono.justOrEmpty(event.getInteraction().getMessage().get().getContent())
                .map(content -> Arrays.asList(content.split(" ")))
                .doOnNext(command -> playerManager.loadItem(youtubeLink, scheduler))
                .then();

         */
    }

    private void connectToVoice(ChatInputInteractionEvent event, AudioProvider provider) {
        Mono.justOrEmpty(event.getInteraction().getMember()).flatMap(Member::getVoiceState)
                .flatMap(VoiceState::getChannel)
                .flatMap(channel -> channel.join().withProvider(provider))
                .block();
    }
}
