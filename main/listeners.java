package ur.nyroma.main;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import ur.nyroma.jeux.pingpong;
import ur.nyroma.moderation.features;
import ur.nyroma.moderation.realModeration;
import ur.nyroma.moderation.troll;
import ur.nyroma.talk.politeness;
import ur.nyroma.talk.haha;

import java.util.List;
import java.util.Random;

public class listeners extends ListenerAdapter {
    private Guild netServer;

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

            if (txt.length() > 3 && txt.substring(0, 3).equals(commandes.prefix)) {
                new commandes().doCommand(mess, server);
            }
            if (txt.equalsIgnoreCase("ping")) {
                new pingpong().ping(mess);
            }

            new haha().test(mess);
            new politeness().politeness(mess, server);
            new realModeration().censure(server, mess, e.getAuthor());
            new troll().testBound(mess);
            new features().countMessages(server);
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
            new politeness().politenessPrivate(e.getMessage());
        }
    }
    @Override
    public void onGuildMessageUpdate(GuildMessageUpdateEvent e) {
        new realModeration().censure(e.getGuild(), e.getMessage(), e.getAuthor());
    }
}
