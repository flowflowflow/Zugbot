import discord4j.common.JacksonResources;
import discord4j.discordjson.json.ApplicationCommandData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;
import util.IOHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class GuildCommandRegistrar {
    private final RestClient restClient;
    private IOHelper io;

    public GuildCommandRegistrar(RestClient restClient, IOHelper io) {
        this.restClient = restClient;
        this.io = io;
    }

    //Since this will only run once on startup, blocking is okay.
    protected void registerCommands() throws IOException {
        //Create an ObjectMapper that supports Discord4J classes
        final JacksonResources d4jMapper = JacksonResources.create();

        // Convenience variables for the sake of easier to read code below
        final ApplicationService applicationService = restClient.getApplicationService();
        final long applicationId = restClient.getApplicationId().block();
        final long guildId = io.readGuildId();

        //These are commands already registered with discord from previous runs of the bot
        //Bot needs permission to create commands
        Map<String, ApplicationCommandData> discordCommands = applicationService
                .getGuildApplicationCommands(applicationId, guildId)
                .collectMap(ApplicationCommandData::name)
                .block();


        //Get our commands json from resources as command data
        Map<String, ApplicationCommandRequest> commands = new HashMap<>();
        for (String json : getCommandsJson()) {
            ApplicationCommandRequest request = d4jMapper.getObjectMapper()
                    .readValue(json, ApplicationCommandRequest.class);

            commands.put(request.name(), request); //Add to our array list

            //Check if this is a new command that has not already been registered.
            if (!discordCommands.containsKey(request.name())) {
                //Not yet created with discord, let's do it now.
                applicationService.createGuildApplicationCommand(applicationId, guildId, request).block();
            }
        }

        //Check if any commands have been deleted or changed.
        for (ApplicationCommandData discordCommand : discordCommands.values()) {
            long discordCommandId = Long.parseLong(discordCommand.id());

            ApplicationCommandRequest command = commands.get(discordCommand.name());

            if (command == null) {
                //Removed command.json, delete guild command
                applicationService.deleteGuildApplicationCommand(applicationId, guildId, discordCommandId).block();
                continue; //Skip further processing on this command.
            }

            //Check if the command has been changed and needs to be updated.
            if (hasChanged(discordCommand, command)) {
                applicationService.modifyGuildApplicationCommand(applicationId, guildId, discordCommandId, command).block();

            }
        }
    }


    private boolean hasChanged(ApplicationCommandData discordCommand, ApplicationCommandRequest command) {
        // Compare types
        if (!discordCommand.type().toOptional().orElse(1).equals(command.type().toOptional().orElse(1))) return true;

        //Check if description has changed.
        if (!discordCommand.description().equals(command.description().toOptional().orElse(""))) return true;

        //Check if default permissions have changed
        boolean discordCommandDefaultPermission = discordCommand.defaultPermission().toOptional().orElse(true);
        boolean commandDefaultPermission = command.defaultPermission().toOptional().orElse(true);

        if (discordCommandDefaultPermission != commandDefaultPermission) return true;

        //Check and return if options have changed.
        return !discordCommand.options().equals(command.options());
    }

    /* The two below methods are boilerplate that can be completely removed when using Spring Boot */

    private static List<String> getCommandsJson() throws IOException {
        //The name of the folder the commands json is in, inside our resources folder
        final String commandsFolderName = "commands/";


        //Get the folder as a resource
        URL url = GuildCommandRegistrar.class.getClassLoader().getResource(commandsFolderName);
        Objects.requireNonNull(url, commandsFolderName + " could not be found");


        List<String> list = new ArrayList<>();
        try{
            InputStream inputStream = GuildCommandRegistrar.class.getClassLoader().getResourceAsStream(commandsFolderName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //Get all the files inside this folder and return the contents of the files as a list of strings
            String file;
            while((file = reader.readLine()) != null) {
                String resourceFileAsString = getResourceFileAsString(commandsFolderName + file);
                list.add(resourceFileAsString);
                System.out.println(resourceFileAsString + " added to commands list");
            }
        } catch (Exception e) {
            System.out.println("Error loading files " + e.getMessage());
            System.exit(-1);
        }



        return list;
    }



    /**
     * Gets a specific resource file as String
     * @param fileName The file path omitting "resources/"
     * @return The contents of the file as a String, otherwise throws an exception
     */
    private static String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream resourceAsStream = classLoader.getResourceAsStream(fileName)) {
            if (resourceAsStream == null) return null;
            try (InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
                 BufferedReader reader = new BufferedReader(inputStreamReader)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }
}
