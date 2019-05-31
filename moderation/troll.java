package moderation;

import main.speedy;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static main.speedy.*;

public class troll {
    public void bound(Message mess, Guild server){
        try {
            File file = new File(speedy.getServerFolder(server) + "moderation/troll.txt");
            MessageChannel channel = mess.getChannel();
            Member auteur = mess.getMember();
            List<Member> mentionned = mess.getMentionedMembers();
            if(file.exists() && mentionned != null){
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                List<String> lines = reader.lines().collect(Collectors.toList());
                reader.close();
                if(mentionned.size() + lines.size() <= 5){
                    for(Member m : mentionned){
                        if(lines.contains(m.getUser().getId())){
                            sendPrivMess(server, auteur.getUser().getId(), m.getEffectiveName() + " est déjà bound !");
                            delete(mess);
                        } else if(lines.size() == 5){
                            sendPrivMess(server, auteur.getUser().getId(), "Le nombre maximal de personnes bound est déjà atteint ! (5)");
                            delete(mess);
                        } else {
                            FileWriter writer = new FileWriter(file, true);
                            BufferedWriter bw = new BufferedWriter(writer);
                            bw.write(m.getUser().getId());
                            bw.newLine();
                            sendMess(channel, m.getEffectiveName() + " a été bound !");
                            bw.close();
                            writer.close();
                        }
                    }
                } else {
                    sendPrivMess(server, auteur.getUser().getId(), "Tu as mentionné trop de personnes ! Il y a déjà " + lines.size() + " bound, sur 5 :/");
                    delete(mess);
                }
            } else if(!file.exists() && mentionned != null){
                if(mentionned.size() <= 5){
                    testFileExist(file);
                    for(Member m : mentionned){
                        FileWriter writer = new FileWriter(file, true);
                        BufferedWriter bw = new BufferedWriter(writer);
                        bw.write(m.getUser().getId());
                        bw.newLine();
                        sendMess(channel, m.getEffectiveName() + " a été bound !");
                        bw.close();
                        writer.close();
                    }
                } else {
                    sendPrivMess(server, auteur.getUser().getId(), "Tu as mentionné trop de personnes ! Seulement 5 personnes peuvent être bound maximum !");
                    delete(mess);
                }
            } else if(mentionned == null){
                sendPrivMess(server, auteur.getUser().getId(), "[**Erreur**] Syntaxe : `ur/bound <mention(s)>`");
                delete(mess);
            } else {
                sendMess(channel, "Bizarre :thinking_face: <@" + idMay + ">, besoin  de toi !");
            }
        } catch(IOException e){
            e.printStackTrace();
        }

    }
    public void unbound(Message mess){
        File file = new File(speedy.getServerFolder(mess.getGuild()) + "moderation/troll.txt");
        if(file.exists()){
            file.delete();
            sendMess(mess.getChannel(), "Plus personne n'est bound !");
        } else {
            sendMess(mess.getChannel(), "Personne n'est bound !");
        }
    }
    public void spam(Message mess){
        try {
            String[] mots = mess.getContentDisplay().split(" ");
            List<Member> mentionned = mess.getMentionedMembers();
            MessageChannel channel = mess.getChannel();
            if(cooldown.testCooldown(mess.getMember(), mess)){
                if (mots.length == 3 && mentionned.size() == 1) {
                    int nb = Integer.parseInt(mots[1]);
                    if (!(nb > 20)) {
                        for (int i = 0; i < nb; i++) {
                            sendMess(channel, "<@" + mentionned.get(0).getUser().getId() + ">");
                            sleep(1000);
                        }
                        sendMess(channel, ":)");
                    } else {
                        sendPrivMess(mess.getGuild(), mess.getAuthor().getId(), "Ça fait un peu trop de messages :sweat_smile: (20 maximum)");
                        delete(mess);
                    }
                    cooldown.setCooldown(mess.getMember(), mess);
                } else {
                    sendPrivMess(mess.getGuild(), mess.getAuthor().getId(), "[Erreur] Syntaxe : `ur/spam <nb de fois> <membre>`");
                    delete(mess);
                }
            } else {
                sendPrivMess(mess.getGuild(), mess.getAuthor().getId(), "Il y a un cooldown sur cette commande ! Il te reste " + cooldown.getCooldown(mess.getMember(), mess) + " secondes à attendre !");
                delete(mess);
            }
        } catch(NumberFormatException e){
            sendPrivMess(mess.getGuild(), mess.getAuthor().getId(), "[Erreur] Syntaxe : `ur/spam <nb de fois> <membre>`");
            delete(mess);
        }
    }

    public void testBound(Member member, Message mess){
        File file = new File(speedy.getServerFolder(mess.getGuild()) + "moderation/troll.txt");
        MessageChannel channel = mess.getChannel();

        if(file.exists()){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                List<String> lines = reader.lines().collect(Collectors.toList());
                reader.close();

                if(lines.contains(member.getUser().getId())){
                    int i = new Random().nextInt(100);

                    if (i >= 10 && i <= 20) {
                        sendMess(channel, "Mais carrément");
                    } else if (i >= 40 && i <= 50) {
                        sendMess(channel, "Chut");
                    } else if(i > 50 && i < 60){
                        for(int ii = 0 ; ii <= 10 ; ii++){
                            sendMess(channel, "<@" + member.getUser().getId() + ">");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        sendMess(channel, ":)");
                    } else if (i >= 60 && i <= 80) {
                        sendMess(channel, "C'est chiant");
                    } else if (i >= 90) {
                        sendMess(channel, "Mais tg");
                    } else if (i <= 10) {
                        delete(mess);
                        sendMess(channel, "Plop un un message nul en moins");
                    }
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}
