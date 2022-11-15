package util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class IOHelper {

    Properties prop = new Properties();

    public IOHelper() throws IOException {
        InputStream input = IOHelper.class.getClassLoader().getResourceAsStream("token.properties");
        prop.load(input);
        log.info("token.properties loaded");
    }


    /**
     * Provides a Discord API authentication token from properties file
     *
     * @return Token
     */
    public String readDiscordApiToken() {
        return prop.getProperty("discord_api_token");
    }

    /**
     * Provides a personal Discord Guild ID
     *
     * @return guild ID value
     */
    public long readGuildId() {
        return Long.parseLong(prop.getProperty("guild_id"));
    }

    /**
     * Provides the bot's App ID
     *
     * @return aA ID value
     */
    public long readAppId() {
        return Long.parseLong(prop.getProperty("app_id"));
    }

    /**
     * Provides an Openweathermap.org API key
     *
     * @return OWM API key
     * @see {https://openweathermap.org/api}
     */
    public String readOwmApiToken() {
        return prop.getProperty("owm_api_token");
    }

    /**
     * Provides a Rito Games API token
     *
     * @return Rito Games API token
     * @see {https://developer.riotgames.com/}
     */
    public String readRitoApiToken() {
        return prop.getProperty("riot_api_token");
    }

    public String readGiphyApiToken() {
        return prop.getProperty("giphy_api_token");
    }
}
