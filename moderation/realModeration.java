package ur.nyroma.moderation;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.exceptions.ErrorResponseException;
import ur.nyroma.main.speedy;

import java.io.*;
import java.util.List;

import static ur.nyroma.main.speedy.*;

public class realModeration {

    public void censure(Guild server, Message mess, User u) {
        String[] mots = mess.getContentRaw().split(" ");
        File censorWords = new File("data/modGen/censure.txt");
        for(String mot : mots){
            if(speedy.fileHas(censorWords, mot)){
                speedy.deleteMess(mess);
                logMess(server, mess);
                sendPrivMess(server, u.getId(), "Hey " + u.getName() + " ! restes poli s'il te plaît ! \"" + mot + "\" est censuré");
            }
        }
    }

    public void closeChannels(Guild server) {
        List<Channel> channels = server.getChannels();
        Thread close = new Thread(() -> {
            for (Channel channel : channels) {
                if (channel.getType().equals(ChannelType.TEXT)) {
                    channel.getManager().setSlowmode(120).queue();
                }
            }
        });
        close.start();
    }

    public void openChannels(Guild server) {
        List<Channel> channels = server.getChannels();
        Thread open = new Thread(() -> {
            for (Channel channel : channels) {
                if (channel.getType().equals(ChannelType.TEXT)) {
                    channel.getManager().setSlowmode(0).queue();
                }
            }
        });
        open.start();
    }

    public void sendMPall(Message mess) throws ErrorResponseException {
        String message = mess.getContentDisplay().substring(12);
        User auteur = mess.getAuthor();
        Guild server = mess.getGuild();

        Thread send = new Thread(() -> {
            for (Member member : server.getMembers()) {
                User u = member.getUser();
                if (!u.isBot() && u != server.getJDA().getSelfUser()) {
                    sendPrivMess(server, u.getId(), "[Par " + auteur.getName() + "#" + auteur.getDiscriminator() +
                            ", du serveur **" + server.getName() + "**] : " + message);
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        send.setDaemon(true);
        send.start();
    }

    public void delete(MessageChannel channel, int i) {
        List<Message> messages = channel.getHistory().retrievePast(i).complete();
        Thread delete = new Thread(() -> {
            for (Message message : messages) {
                message.delete().queue();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        delete.setDaemon(true);
        delete.start();
    }

    private void logMess(Guild server, Message mess) {
        String txt = ">>> \"" + mess.getContentRaw() + "\" de " + mess.getAuthor().getName() + "#" + mess.getAuthor().getDiscriminator() +
                ", Salon : " + mess.getChannel().getName();
        sendMess(speedy.getChannelByName(server, "infractions_auto"), txt);
    }
}
