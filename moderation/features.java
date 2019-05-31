package moderation;

import main.speedy;
import net.dv8tion.jda.core.entities.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

import static main.speedy.*;

public class features {

    public void say(String[] mots, Message mess){
        if (mots.length >= 2) {
            mess.delete().queue();
            sendMess(mess.getChannel(), mess.getContentDisplay().substring(6) + " ||(demandé par " + mess.getAuthor().getName() + ")||");
        } else {
            sendMess(mess.getChannel(),"Mais quel message dire ? `Syntaxe : ur/say <Message>`");
        }
    }
    public void tellJoke(MessageChannel channel){
        File jokeFolder = new File("data/jokes/");
        File[] jokes = jokeFolder.listFiles();
        if(jokes != null){
            File joke = jokes[new Random().nextInt(jokes.length)];
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(joke), StandardCharsets.UTF_8));
                String line = reader.readLine();
                while(line != null){
                    sendMess(channel, line);
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void resetAllNames(Guild server) {
        List<Member> listUser = server.getMembers();
        for (Member m : listUser) {
            if (!m.isOwner()) {
                server.getController().setNickname(m, m.getUser().getName()).queue();
                speedy.sleep(200);
            }
        }
    }
    public void renameAll(Message mess, Guild server){
        String contenuMess = mess.getContentDisplay();
        String[] contMess = contenuMess.split(" ");
        MessageChannel channel = mess.getChannel();
        if (contMess.length > 1) {
            contMess[0] = "";
            String newNick = String.join(" ", contMess);
            List<Member> listUser = server.getMembers();
            for (Member m : listUser) {
                if (!m.isOwner()) {
                    server.getController().setNickname(m, newNick).queue();
                    speedy.sleep(200);
                }
            }
            sendMess(channel, "Tout le monde a été renommé en ***" + newNick + "*** !");
        } else {
            sendMess(channel, "[**Erreur**] Syntaxe : `ur/renameall <rename en ?>`");
        }

    }

    public void getIdsAll(Guild server){
        File file = new File("data/servers/" + server.getId() + "/idsall.txt");
        speedy.testFileExist(file);
        try {
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            List <Member> ms = server.getMembers();
            for(Member m : ms){
                String name = m.getUser().getName();
                String id = m.getUser().getId();
                bw.write(name + " : " + id);
                bw.newLine();
            }
            bw.close();
            writer.close();
            sendPrivMess(server, idMay, "Terminé !");
        } catch (IOException e) {
            e.printStackTrace();
            sendPrivMess(server, idMay, "Une erreur est survenue !");
        }
    }
    public void getID(Message mess){
        List<Member> mentionned = mess.getMentionedMembers();
        if(mentionned.size() == 1){
            sendPrivMess(mess.getGuild(), idMay, mentionned.get(0).getUser().getId());
        } else {
            sendPrivMess(mess.getGuild(), idMay, "[**Erreur**] Syntaxe : `ur/getid <@membre#0000>`");
        }
    }
    public void sendMP(Message mess){
        String[] mots = mess.getContentDisplay().split(" ");
        StringBuilder message = new StringBuilder();
        for(int i = 2 ; i < mots.length ; i++){
            message.append(mots[i]);
        }
        speedy.sendPrivMess(mess, mots[1], message.toString());
        sendPrivMess(mess.getGuild(), idMay, "Terminé !");
    }
    public void getMessagesInChannel(MessageChannel channel, Guild server){
        List<Message> messages = channel.getHistory().retrievePast(100).complete();
        File file = new File(speedy.getServerFolder(server) + "moderation/messages/" + channel.getId() + ".txt");
        testFileExist(file);
        try {
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Message message : messages) {
                String date = "{ " + message.getCreationTime().toString().substring(0,9);
                String heure = message.getCreationTime().toString().substring(11,18) + " }";
                User auteur = message.getAuthor();

                bw.write(date + " / " + heure + " /// " + auteur.getName() + "#" + auteur.getDiscriminator() + " : " + message.getContentDisplay());
                bw.newLine();
                bw.write("---------");
                bw.newLine();
            }
            bw.close();
            fw.close();
            sendPrivMess(server, idMay, "Terminé !");
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    public void getMessagesInChannelD(MessageChannel channel, Guild server, int i){
        List<Message> messages = channel.getHistory().retrievePast(i).complete();
        File file = new File(speedy.getServerFolder(server) + "moderation/messages/" + channel.getId() + ".txt");
        speedy.testFileExist(file);
        try {
            FileWriter fw = new FileWriter(file,true);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Message message : messages) {
                String date = "{ " + message.getCreationTime().toString().substring(0,10);
                String heure = message.getCreationTime().toString().substring(11,18) + " }";
                User auteur = message.getAuthor();

                bw.write(date + " / " + heure + " /// " + auteur.getName() + "#" + auteur.getDiscriminator() + " : " + message.getContentDisplay());
                bw.newLine();
                bw.write("---------");
                bw.newLine();
                if(message.getAttachments().size() == 0){
                    message.delete().queue();
                }
                speedy.sleep(500);
            }
            bw.close();
            fw.close();
            sendPrivMess(server, idMay, "Terminé !");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void countMessages(Guild server) {
        System.out.println();
        File counterFolder = new File(speedy.getServerFolder(server) + "moderation/counters/");
        File counter = new File(speedy.getServerFolder(server) + "moderation/counters/" + speedy.getDate().toString().substring(0,10) + ".txt");
        speedy.testFolderExist(counterFolder);
        speedy.testFileExist(counter);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(counter), StandardCharsets.UTF_8));
            String nb = reader.readLine();
            FileWriter writer = new FileWriter(counter);
            BufferedWriter bw = new BufferedWriter(writer);
            if(nb == null){
                nb = "1";
                bw.write(nb);
            } else {
                int i = Integer.parseInt(nb);
                i++;
                nb = Integer.toString(i);
                bw.write(nb);
            }
            bw.close();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
