package xyz.nyroma.neter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.entities.*;
import xyz.nyroma.main.SpeedyException;
import xyz.nyroma.main.speedy;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class NeterCommands {
    public final static String prefix = "un/";
    public List<String> commands = Arrays.asList("resetnames","delete","mpall","renameall");

    public NeterCommands(Message mess, String cmd){
        Guild server = mess.getGuild();
        MessageChannel channel = mess.getChannel();
        Member member = mess.getMember();
        String txt = mess.getContentRaw();
        String[] args = txt.split(" ");

        try {
            if(cmd.equals(commands.get(0)) && speedy.isStaff(member)){
                Thread th = new Thread(() -> {
                    for(Member m : server.getMembers()){
                        if(!m.isOwner()) {
                            m.modifyNickname(m.getUser().getName()).queue();
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                th.setDaemon(true);
                th.start();
            }
            else if(cmd.equals(commands.get(1)) && speedy.isStaff(member)){
                try {
                    List<Message> messages = channel.getHistory().retrievePast(Integer.parseInt(args[1])).complete();
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
                } catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
                    speedy.sendMess(channel, "[:x:] Arguments invalides ! Syntaxe : `un/delete <nombre>`");
                }
            }
            else if(cmd.equals(commands.get(2)) && member.isOwner()){
                StringBuilder message = new StringBuilder();
                User ua = mess.getAuthor();
                for(String s : args){
                    message.append(s);
                }

                Thread send = new Thread(() -> {
                    for (Member m : server.getMembers()) {
                        User u = m.getUser();
                        if (!u.isBot() && u != server.getJDA().getSelfUser()) {
                            speedy.sendPrivMess(server, u.getId(), "[Par " + ua.getName() + "#" + ua.getDiscriminator() +
                                    ", du serveur **" + server.getName() + "**] : \n" + message.toString());
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                send.setDaemon(true);
                send.start();
            }
            else if(cmd.equals(commands.get(3)) && member.isOwner()){
                String[] mots = txt.split(" ");
                if (mots.length > 1) {
                    Thread rename = new Thread(() -> {
                        mots[0] = "";
                        String newNick = String.join(" ", mots);
                        List<Member> listUser = server.getMembers();
                        for (Member m : listUser) {
                            if (!m.isOwner()) {
                                m.modifyNickname(newNick).queue();
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        speedy.sendMess(channel, "Tout le monde a été renommé en ***" + newNick + "*** !");
                    });
                    rename.setDaemon(true);
                    rename.start();
                } else {
                    speedy.sendMess(channel, "[:x:] Arguments invalides ! Syntaxe : `ur/renameall <pseudo>`");
                }
            }
        } catch (SpeedyException e) {
            e.printStackTrace();
        }
    }
}
