package captfudgecake;

import java.util.Collection;

import org.javacord.api.*;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
/**
 * Hello world!
 *
 */
public class TallyBot 
{
    private DiscordApi api;
    private TextChannel foodPicsChannel;

    public static void main( String[] args )
    {
        String token = "";
        TallyBot bot = new TallyBot(token);
        bot.addPingPong();
    }

    TallyBot(String token){
        this.api = new DiscordApiBuilder()
                                .setToken(token)
                                .login().join();
        
        getFoodPicsChannel();
    }

    private void getFoodPicsChannel(){
        Collection<Channel> foodPics = api.getChannelsByName("📝discussion");
        if(foodPics != null && foodPics.size() > 0){
            foodPicsChannel = foodPics.iterator().next().asTextChannel().get();
        }
    }

    public void addPingPong(){
        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                System.out.println(event.getChannel().getId());
                event.getChannel().sendMessage("Pong!");
                sendMessageToFoodPics("Test message, please ignore");
            }
        });
    }

    public void sendMessageToFoodPics(String message){
        foodPicsChannel.sendMessage(message);
    }
}
