package main;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.*;
import java.util.List;
import java.util.Random;

public class listeners extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {

        new setupFolders().setupFolders(e.getGuild());

        if (!e.getAuthor().isBot() && e.getGuild() != null) {
            Message mess = e.getMessage();
            String contenuMess = mess.getContentDisplay();
            Guild server = e.getGuild();

            if(contenuMess.contains("@someone")){
                List<Member> members = server.getMembers();
                Random r = new Random();
                int random = r.nextInt(members.size());
                speedy.sendMess(mess, "<@" + members.get(random).getUser().getId() + ">");
            }

            if (contenuMess.length() > 3 && contenuMess.substring(0, 3).equals("ur/")) {
                new commandes().doCommande(mess, server, e.getMember());
            }
            if (contenuMess.equalsIgnoreCase("ping")) {
                new jeux.pingpong().ping(mess, server);
            }

            new talkness.wordsGames().test(mess);
            new talkness.politeness().politeness(mess, server);
            new moderation.realModeration().censure(server, mess, e.getAuthor());
            new moderation.troll().testBound(e.getMember(), mess);
            new moderation.realModeration().countMessages(server);
        }
    }
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e){
        File privateMess = new File("data/privateMessages.txt");
        try {
            if(!privateMess.exists()){
                privateMess.createNewFile();
            }
            FileWriter writer = new FileWriter(privateMess, true);
            PrintWriter pw = new PrintWriter(writer);
            pw.println(e.getAuthor().getName() + "#" + e.getAuthor().getDiscriminator() + " (" + e.getAuthor().getId() + ") " +
                    " /// date : " + speedy.getDate() + " /// contenu du message : " + e.getMessage().getContentDisplay());
            pw.close();
            writer.close();
        } catch(IOException ee){
            ee.printStackTrace();
        }
    }
}
