package ur.nyroma.main;

import fr.may.processus.DataSorter;
import fr.may.processus.SentenceBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.RestAction;
import ur.nyroma.features.CensorManager;
import ur.nyroma.jeux.pingpong;
import ur.nyroma.moderation.BlacklistManager;
import ur.nyroma.moderation.FeaturesManager;
import ur.nyroma.moderation.ModerationManager;
import ur.nyroma.moderation.TrollingManager;
import ur.nyroma.talk.polite;
import ur.nyroma.talk.haha;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class listeners extends ListenerAdapter {
    private Guild netServer;
    private DataSorter sorter;
    private SentenceBuilder sbuilder;
    private BlacklistManager blacklist = new BlacklistManager();

    public listeners(){
        this.sorter = new DataSorter();
        this.sbuilder = new SentenceBuilder();
    }

    @Override
    public void onGuildJoin(GuildJoinEvent e){
        
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        new setupFolders().setupFolders(e.getGuild());

        if (!e.getAuthor().isBot() && e.getGuild() != null) {
            Message mess = e.getMessage();
            String txt = mess.getContentRaw();
            Guild server = e.getGuild();
            TextChannel channel = mess.getTextChannel();
            if(server.getOwner().getUser().getId().equalsIgnoreCase(speedy.idMay)){
                this.netServer = server;
            }
            if(txt.contains("@someone")){
                List<Member> members = server.getMembers();
                speedy.sendMess(channel, "<@" + members.get(new Random().nextInt(members.size())).getUser().getId() + ">");
            }

            List<Member> mentionned = mess.getMentionedMembers();
            if(mentionned.size() > 0){
                if(mentionned.get(0).getUser().equals(server.getJDA().getSelfUser()) && !txt.contains("?")){
                    RestAction<Message> ra = channel.sendMessage(this.sbuilder.build());
                    Message message = ra.complete();
                    message.addReaction("✅").queue();
                    message.addReaction("❌").queue();
                } else if(mentionned.get(0).getUser().equals(server.getJDA().getSelfUser())){
                    List<Boolean> censure = new ArrayList<>();
                    for(String word : txt.split(" ")){
                        if(blacklist.hasWord(word)){
                            censure.add(true);
                        } else {
                            censure.add(false);
                        }
                    }
                    if(!censure.contains(true)){
                        switch(new Random().nextInt(11)){
                            case 1:
                                speedy.sendMess(channel, "Oui");
                                break;
                            case 5:
                                speedy.sendMess(channel, "Non");
                                break;
                            case 7:
                                speedy.sendMess(channel, "Peut-être");
                                break;
                            case 9:
                                speedy.sendMess(channel, "Je ne répondrais pas");
                                break;
                        }
                    } else {
                        mess.addReaction(":ban:603347805334929408").queue();
                    }
                }
            }

            if (txt.length() > 3 && txt.substring(0, 3).equals(commandes.prefix)) {
                new commandes(mess);
            }

            if (txt.equalsIgnoreCase("ping")) {
                new pingpong().ping(mess);
            }

            new haha().test(mess);
            new polite().bePolite(mess);
            new CensorManager(server).test(mess);
            if(!server.getId().equalsIgnoreCase("481809113500614667")){
                Thread t = new Thread(() -> this.sorter.addWords(mess.getContentRaw()));
                t.start();
                System.out.println("enregistré !");
            }
            new TrollingManager().testBound(mess);
            new FeaturesManager().countMessages(server);
        }
    }
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e){
        User u = e.getAuthor();
        Message mess = e.getMessage();
        if(!u.equals(e.getJDA().getSelfUser())) {
            String message =
                    u.getName() + "#" + u.getDiscriminator() + " (<@" + u.getId()+ ">) \n" +
                    "Date : " + speedy.getDate() + "\n" +
                    "Message : " + mess.getContentRaw();
            speedy.sendMess(speedy.getChannelByName(netServer, "mps"), ">>> " + message);
            new polite().bePolite(mess);
        }
    }
    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent e) {
        new CensorManager(e.getGuild()).test(e.getMessage());
    }
}
