package commands;

import com.kdotj.simplegiphy.SimpleGiphy;
import com.kdotj.simplegiphy.data.RandomGiphy;
import com.kdotj.simplegiphy.data.RandomGiphyResponse;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import util.Constants;

@Slf4j
public class RandomGifCommand implements SlashCommand {

    @Override
    public String getName() {
        return "RandomGIF";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        String giphyApiKey = Constants.GIPHY_API_TOKEN.value;
        SimpleGiphy.setApiKey(giphyApiKey);
        SimpleGiphy giphy = SimpleGiphy.getInstance();

        String searchTerm = event.getOption("keyword")
                .flatMap(ApplicationCommandInteractionOption::getValue)
                .map(ApplicationCommandInteractionOptionValue::asString)
                .orElse("");

        RandomGiphyResponse randomGiphyResponse = giphy.random(searchTerm, "r");

        if (randomGiphyResponse == null) {
            log.error("Failed deserialization of Giphy response");
            return event.reply("Sorry, gifs for the keyword couldn't be found :(");
        } else {
            RandomGiphy randomGiphy = randomGiphyResponse.getRandomGiphy();
            return event.reply(randomGiphy.getUrl());
        }
    }
}
