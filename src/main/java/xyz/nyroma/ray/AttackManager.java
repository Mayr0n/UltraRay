package xyz.nyroma.ray;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import xyz.nyroma.main.speedy;

import java.io.File;
import java.util.List;

public class AttackManager {

    public void build(Message mess){
        String txt = mess.getContentRaw();
        List<User> mentionned = mess.getMentionedUsers();
        if(txt.contains("occupe toi de ")){
            for(User u : mentionned){
                if(!u.isBot()){
                    u.openPrivateChannel().queue(
                            (channel) -> channel.sendMessage(new MessageBuilder().append(" ").build()).addFile(new File("data/omfg.png")).queue()
                    );
                    speedy.sendPrivMess(mess.getGuild(), u.getId(),
                            "Не бойтесь снова нарушать правила, и я заставляю вас есть их, " +
                                    "не используя рот, прежде чем разорвать голову на части зубами. твои зубы.");
                    speedy.sendMess(mess.getChannel(), ":)");
                }
            }
        }
    }

}
