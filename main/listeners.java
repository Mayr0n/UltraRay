package main;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class listeners extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {


        if (!e.getAuthor().isBot() && e.getGuild() != null) {
            Message mess = e.getMessage();
            String contenuMess = mess.getContentDisplay();
            Guild server = e.getGuild();

            if (contenuMess.length() > 3 && contenuMess.substring(0, 3).equals("ur/")) {
                new commandes().doCommande(mess, server, e.getMember());
            }
            if (contenuMess.equalsIgnoreCase("ping")) {
                new jeux.pingpong().ping(mess, server);
            }

            new politeness.politeness().politeness(mess, server);
            new moderation.troll().testBound(e.getMember(), mess);
            new moderation.realModeration().censure(mess, e.getAuthor());
        }
    }
}
