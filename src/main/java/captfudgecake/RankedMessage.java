package captfudgecake;

import org.javacord.api.entity.message.MessageAuthor;

public class RankedMessage{
    public double score;
    public String text;
    public String imageUrl;
    public MessageAuthor author;

    RankedMessage(double s, String t, String imgUrl, MessageAuthor a){
        score = s;
        text = t;
        imageUrl = imgUrl;
        author = a;
    }
}
