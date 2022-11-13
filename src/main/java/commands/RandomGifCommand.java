package commands;

import com.kdotj.simplegiphy.SimpleGiphy;
import com.kdotj.simplegiphy.data.RandomGiphy;
import com.kdotj.simplegiphy.data.RandomGiphyResponse;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import lombok.extern.java.Log;
import reactor.core.publisher.Mono;
import util.IOHelper;

import java.io.IOException;
import java.util.logging.Level;

@Log
public class RandomGifCommand implements SlashCommand {

    @Override
    public String getName() {
        return "RandomGIF";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        try {
            IOHelper io = new IOHelper();
            String giphyApiKey = io.readGiphyApiToken();

            SimpleGiphy.setApiKey(giphyApiKey);
            SimpleGiphy giphy = SimpleGiphy.getInstance();

            RandomGiphyResponse randomGiphyResponse = giphy.random("", "r");
            RandomGiphy randomGiphy = randomGiphyResponse.getRandomGiphy();

            return event.reply(randomGiphy.getUrl());

        } catch (IOException e) {
            log.log(Level.WARNING, "Failed to create IOHelper instance");
            return event.reply().withEphemeral(true).withContent("Sorry, that didn't work out as planned :(");
        }
    }
}
