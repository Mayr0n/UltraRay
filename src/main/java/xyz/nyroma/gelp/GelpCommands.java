package xyz.nyroma.gelp;

import jdk.nashorn.internal.runtime.arrays.ArrayIndex;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import xyz.nyroma.main.SpeedyException;
import xyz.nyroma.main.speedy;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GelpCommands {

    public final static String prefix = "ug/";
    public List<String> commands = Arrays.asList("bound","unbound","joke");
    private TrollingCache tc = new TrollingCache();

    public GelpCommands(Message mess, String cmd){
        Guild server = mess.getGuild();
        MessageChannel channel = mess.getChannel();
        Member member = mess.getMember();
        String txt = mess.getContentRaw();
        String[] args = txt.split(" ");

        try {
            if (cmd.equals(commands.get(0)) && speedy.isStaff(member)) {
                if(mess.getMentionedMembers().size() > 0) {
                    TrollingManager tm = tc.get(server);
                    for(Member m : mess.getMentionedMembers()){
                        tm.addBound(m.getId());
                        speedy.sendMess(channel, m.getUser().getName() + " a été bound :)");
                        speedy.sendPrivMess(mess, m.getId(), "LMAO");
                        speedy.sendPrivMess(mess, m.getId(), "C MAREN");
                    }
                } else {
                    speedy.sendMess(channel, "[:x:] Arguments invalides ! Syntaxe : `ur/bound <membre(s)>`");
                }

            } else if(cmd.equals(commands.get(1)) && speedy.isStaff(member)){
                if(mess.getMentionedMembers().size() > 0) {
                    TrollingManager tm = tc.get(server);
                    for(Member m : mess.getMentionedMembers()){
                        tm.removeBound(m.getId());
                        speedy.sendMess(channel, m.getUser().getName() + " a été unbound :(");
                    }
                } else {
                    speedy.sendMess(channel, "[:x:] Arguments invalides ! Syntaxe : `ur/unbound <membre(s)>`");
                }
            } else if(cmd.equals(commands.get(2))){
                if(args.length > 1){
                    switch(args[1]){
                        case "add":
                            try {
                                String title = args[2];
                                StringBuilder sb = new StringBuilder();
                                for(int i = 3 ; i < args.length ; i++){
                                    sb.append(args[i]).append(" ");
                                }
                                new Joke(title, sb.toString());
                            } catch(ArrayIndexOutOfBoundsException e){
                                speedy.sendMess(channel, "[:x:] Arguments invalides ! Syntaxe : `ug/joke add <title> <blague>`");
                            }
                            break;
                        case "remove":
                            if(speedy.isStaff(member)){
                                try {
                                    JokeCache jc = new JokeCache();
                                    String title = args[2];
                                    try {
                                        if(jc.remove(jc.get(title))){
                                            speedy.sendMess(channel, "La blague " + title + " a été supprimée.");
                                        } else {
                                            speedy.sendMess(channel, "La blague " + title + " n'a pas pu être supprimée.");
                                        }
                                    } catch (JokeException e) {
                                        speedy.sendMess(channel, e.getMessage());
                                    }
                                } catch(ArrayIndexOutOfBoundsException e){
                                    speedy.sendMess(channel, "[:x:] Arguments invalides ! Syntaxe : `ug/joke add <title> <blague>`");
                                }
                            } else {
                                speedy.sendMess(channel, "Vous n'avez pas les permissions nécessaires.");
                            }
                    }
                } else {
                    JokeCache jc = new JokeCache();
                    Joke j = jc.getJokes().get(new Random().nextInt(jc.getJokes().size()-1));
                    speedy.sendMess(channel, ":clown: __Titre :__" + j.getTitle());
                    speedy.sendMess(channel, j.getJoke());
                }
            }
        } catch(SpeedyException | TrollingException e){
            speedy.sendMess(channel, e.getMessage());
        }
    }
}
