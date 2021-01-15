package captfudgecake;

import org.javacord.api.*;
/**
 * Hello world!
 *
 */
public class TallyBot 
{
    public static void main( String[] args )
    {
        String token = "";
        DiscordApi test = new DiscordApiBuilder()
                                .setToken(token)
                                .login().join();

        test.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                event.getChannel().sendMessage("Pong!");
            }
        });
    }
}
