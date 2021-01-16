package captfudgecake;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.Period;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Reaction;
import org.javacord.api.entity.message.embed.EmbedBuilder;
/**
 * Hello world!
 *
 */
public class TallyBot 
{
    private static DecimalFormat df = new DecimalFormat("0.00");
    private DiscordApi api;
    private TextChannel foodPicsChannel;
    private TextChannel botTestChannel;

    private Comparator<RankedMessage> rankedMessageComparator = new Comparator<RankedMessage>(){
        public int compare(RankedMessage a, RankedMessage b) {
            double dif = b.score - a.score;
            return dif > 0 ? 
                        1 : 
                        dif < 0 ? 
                            -1 : 
                            0;
        }
    };

    public static void main( String[] args )
    {
        // Add bot token here
        String token = "";
        TallyBot bot = new TallyBot(token);
        bot.addPingPong();
    }

    TallyBot(String token){
        this.api = new DiscordApiBuilder()
                                .setToken(token)
                                .login().join();
        
        getChannels();
    }

    private void getChannels(){
        Collection<Channel> foodPics = api.getChannelsByName("🍗food-pics");

        if(foodPics != null && foodPics.size() > 0){
            foodPicsChannel = foodPics.iterator().next().asTextChannel().get();
        }
        Collection<Channel> botTestChannels = api.getChannelsByName("bot-test-output");
        if(botTestChannels != null && botTestChannels.size() > 0){
            botTestChannel = botTestChannels.iterator().next().asTextChannel().get();
        }
    }

    public void addPingPong(){
        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("!ping")) {
                List<RankedMessage> test = getWeeksMessagesWithImages();
                System.out.println(test.size());
                test.forEach(m -> {
                    EmbedBuilder scoreMessage = new EmbedBuilder().setTitle(m.text).addField("Score", df.format(m.score)).setImage(m.imageUrl).setAuthor(m.author);
                    botTestChannel.sendMessage(scoreMessage);
                });
            }
        });
    }

    public void sendMessageToFoodPics(String message){
        foodPicsChannel.sendMessage(message);
    }

    public double averageScore(Message m){
        List<Reaction> reactions = m.getReactions();
        double validReactionTotal = 0;
        double validReactionCount = 0;
        for (Reaction reaction : reactions) {
            double count = reaction.getCount();
            String code = reaction.getEmoji().getMentionTag().replaceAll("<:(\\d+_):\\w*>", "$1");
            switch (code) {
                case "1_":
                    validReactionTotal += (1 * count);
                    validReactionCount += (1 * count);
                    break;
    
                case "2_":
                    validReactionTotal += 2 * count;
                    validReactionCount += (1 * count);
                    break;
    
                case "3_":
                    validReactionTotal += 3 * count;
                    validReactionCount += (1 * count);
                    break;
    
                case "4_":
                    validReactionTotal += 4 * count;
                    validReactionCount += (1 * count);
                    break;
    
                case "5_":
                    validReactionTotal += 5 * count;
                    validReactionCount += (1 * count);
                    break;
    
                case "6_":
                    validReactionTotal += 6 * count;
                    validReactionCount += (1 * count);
                    break;
    
                case "7_":
                    validReactionTotal += 7 * count;
                    validReactionCount += (1 * count);
                    break;
    
                case "8_":
                    validReactionTotal += 8 * count;
                    validReactionCount += (1 * count);
                    break;
    
                case "9_":
                    validReactionTotal += 9 * count;
                    validReactionCount += (1 * count);
                    break;
    
                case "10_":
                    validReactionTotal += 10 * count;
                    validReactionCount += (1 * count);
                    break;
                    
                default:
                    break;
            }
            
        }

        if(validReactionTotal != 0 && validReactionCount !=0){
            return (validReactionTotal / validReactionCount);
        } else {
            return 0;
        }   
    }

    private List<RankedMessage> getWeeksMessagesWithImages(){
        Instant now = Instant.now();
        Instant weekAgo = now.minus(Period.ofDays(7));
        System.out.println(weekAgo.toString());
        Predicate<Message> messageCondition = m -> m.getCreationTimestamp().isBefore(weekAgo);
        return foodPicsChannel.getMessagesUntil(messageCondition).join().stream()
                    .filter(m ->m.getAttachments().size() > 0  && getTotalReactions(m) >= 6)
                    .map(m -> new RankedMessage(averageScore(m), m.getContent(), m.getAttachments().get(0).getProxyUrl().toString(), m.getAuthor()))
                    .sorted(rankedMessageComparator)
                    .limit(3)
                    .collect(Collectors.toList());
    }

    private int getTotalReactions(Message m){
        Optional<Integer> count = m.getReactions().stream().map(r -> r.getCount()).reduce((agg, r) -> agg += r);
        if(count.isPresent()){
            return count.get();
        } else {
            return 0;
        }
    }
}
