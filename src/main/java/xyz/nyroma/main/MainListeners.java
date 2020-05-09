package xyz.nyroma.main;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.nyroma.gelp.GelpCommands;
import xyz.nyroma.gelp.GelpListeners;
import xyz.nyroma.gelp.JokeCache;
import xyz.nyroma.gelp.TrollingCache;
import xyz.nyroma.ray.*;
import xyz.nyroma.neter.NeterCommands;
import xyz.nyroma.say.SayCommands;

import java.util.Arrays;
import java.util.List;

public class MainListeners extends ListenerAdapter {
    private GelpListeners gl;
    private BlackCache bc = new BlackCache();

    private int nbMess = 0;

    public MainListeners(GelpListeners gl){
        this.gl = gl;
    }

    @Override
    public void onShutdown(ShutdownEvent e){
        System.out.println("Enregistrement des blacklists...");
        BlackCache.serialize();
        System.out.println("Blacklists enregistrées !");
        System.out.println("Enregistrement des bound...");
        TrollingCache.serialize();
        System.out.println("Bound enregistrés !");
        System.out.println("Enregistrement des jokes...");
        JokeCache.serialize();
        System.out.println("Jokes enregistrées !");
        System.exit(0);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            Message mess = e.getMessage();
            String txt = mess.getContentRaw();
            Guild server = e.getGuild();
            User u = e.getAuthor();

            if(server.getId().equals("400414883843145748")){
                gl.setServer(server);
            }

            List<User> mentionned = mess.getMentionedUsers();
            if (mentionned.contains(server.getJDA().getSelfUser())) {
                mess.addReaction(":ban:603347805334929408").queue();
            }

            if(txt.length() > 3){
                String cmd = txt.split(" ")[0].substring(3);
                switch(txt.substring(0,3)){
                    case GelpCommands.prefix:
                        new GelpCommands(mess, cmd);
                        break;
                    case NeterCommands.prefix:
                        new NeterCommands(mess, cmd);
                        break;
                    case SayCommands.prefix:
                        new SayCommands();
                        break;
                    case RayCommands.prefix:
                        new RayCommands(mess, cmd);
                        break;
                }
            }

            try {
                if (mentionned.contains(server.getJDA().getSelfUser()) && speedy.isStaff(mess.getMember())) {
                    new AttackManager().build(mess);
                }

                BlacklistManager blm = bc.get(server);
                for(String w : blm.getWords()){
                    boolean b = false;
                    for(String s : txt.split(" ")){
                        if(!isAlphanumeric(s)){
                            b = s.contains(w);
                        }
                    }
                    if(Arrays.asList(txt.split(" ")).contains(w) && !speedy.isStaff(e.getMember())) {
                        b = true;
                    }
                    if(b){
                        punish(mess, w);
                    }
                }
            } catch (BlacklistException e1) {
                new BlacklistManager(server.getId());
            } catch(SpeedyException ignored){
            }

            log(mess);
        }
    }

    private void punish(Message mess, String c){
        Guild server = mess.getGuild();
        User u = mess.getAuthor();

        speedy.sendPrivMess(mess, u.getId(), "Hey " + u.getName() + " ! Restes poli s'il te plaît ! " + c + " est censuré.");
        mess.delete().queue();
        TextChannel channel;
        try {
            channel = speedy.getChannelByName(server, "infractions_auto");
        } catch(SpeedyException ee){
            channel = server.createTextChannel("infraction_auto").complete();
            channel.getManager().setTopic("Ici seront répertoriées toutes les infractions détectées par le bot.").queue();
        }
        speedy.sendMess(channel,
                ">>> **Auteur : **" + mess.getAuthor().getName() + "\n" +
                        "**Date : **" + mess.getTimeCreated().toString() + "\n" +
                        "**Channel : **#" + mess.getChannel().getName() + "\n" +
                        "**Contenu : **" + mess.getContentDisplay() + "\n" +
                        "**Censuré : **" + c
        );
    }

    private boolean isAlphanumeric(String str) {
        char[] charArray = str.toCharArray();
        for (char c : charArray) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private void log(Message mess) {
            TextChannel channel;
            try {
                channel = speedy.getChannelByName(mess.getGuild(), "messages");
            } catch (SpeedyException e) {
                channel = mess.getGuild().createTextChannel("messages").complete();
                channel.getManager().setTopic("Ici seront répertoriés tous les messages envoyés sur le serveur.").queue();
            }
            speedy.sendMess(channel, speedy.espace() + ">>> **Message n°" + this.nbMess + "** \n" +
                    "**Auteur : **" + mess.getAuthor().getName() + "\n" +
                    "**Date : **" + mess.getTimeCreated().toString() + "\n" +
                    "**Channel : **#" + mess.getChannel().getName() + "\n" +
                    "**Contenu : **" + mess.getContentDisplay() + speedy.espace()
            );
            this.nbMess++;
    }
}
