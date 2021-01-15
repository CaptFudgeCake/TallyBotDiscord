package captfudgecake;

import java.time.Instant;
import java.time.Period;
import java.util.Collection;
import java.util.function.Predicate;

import org.javacord.api.*;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageSet;
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
                MessageSet test = getWeeksMessagesWithImages();
                test.forEach(m -> m.toMessageBuilder().send(foodPicsChannel));
                sendMessageToFoodPics("Test message, please ignore");
            }
        });
    }

    public void sendMessageToFoodPics(String message){
        foodPicsChannel.sendMessage(message);
    }

    private MessageSet getWeeksMessagesWithImages(){
        Instant now = Instant.now();
        Instant weekAgo = now.minus(Period.ofDays(7));
        Predicate<Message> messageCondition = m -> m.getCreationTimestamp().isAfter(weekAgo) && m.getAttachments().size() > 0;
        return foodPicsChannel.getMessagesUntil(messageCondition).join();
    }
}
