package util;

import java.util.Random;

public final class RizzImages {

    private RizzImages() {

    }

    public static String getRizz() {
        final String[] images = new String[3];
        images[0] = "https://cdn.discordapp.com/attachments/1042034256286863410/1058083626232860712/Rizzsczenski.png";
        images[1] = "https://cdn.discordapp.com/attachments/1042034256286863410/1059389197082951680/EkQqMsiU4AAMcus_2.png";
        images[2] = "https://cdn.discordapp.com/attachments/1042034256286863410/1059391516503707678/the-best-korean-musical-dramas-that-you-must-watch.jpg";

        Random random = new Random();
        int i = random.nextInt(0, 3);

        return images[i];
    }
}
