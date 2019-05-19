package moderation;

import main.speedy;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

import static main.speedy.sendMess;

public class troll {
    public void bound(Message mess, Guild server){

        try {
            File file = new File(speedy.getServerFolder(server) + "moderation/troll.txt");

            if(!file.exists() && mess.getMentionedMembers() != null && mess.getMentionedMembers().size() == 1){
                FileWriter writer = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(writer);
                Member member = mess.getMentionedMembers().get(0);

                String id = member.getUser().getId();
                bw.write(id);
                bw.close();
                writer.close();

                sendMess(mess, member.getUser().getName() + " a été bound");
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                String m = reader.readLine();
                sendMess(mess, server.getMemberById(m).getUser().getName() + "#" + server.getMemberById(m).getUser().getDiscriminator() + " est déjà bound !");
                reader.close();
            }

        } catch(IOException e){
            e.printStackTrace();
        }

    }
    public void testBound(Member member, Message mess){
        File file = new File(speedy.getServerFolder(mess.getGuild()) + "moderation/troll.txt");

        if(file.exists()){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
                String id = reader.readLine();
                reader.close();

                if(member.getUser().getId().equals(id)){
                    Random r = new Random();
                    int i = r.nextInt(100);

                    if (i >= 10 && i <= 20) {
                        sendMess(mess, "Mais carrément");
                    } else if (i >= 40 && i <= 50) {
                        sendMess(mess, "Chut");
                    } else if(i > 50 && i < 60){
                        for(int ii = 0 ; ii <= 10 ; ii++){
                            sendMess(mess, "<@" + member.getUser().getId() + ">");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        sendMess(mess, ":)");
                    } else if (i >= 60 && i <= 80) {
                        sendMess(mess, "C'est chiant");
                    } else if (i >= 90) {
                        sendMess(mess, "Mais tg");
                    } else if (i <= 10) {
                        mess.delete();
                        sendMess(mess, "Plop un un message nul en moins");
                    }
                }
            } catch(IOException e){
                e.printStackTrace();
            }
        }


    }
    public void unbound(Message mess){
        File file = new File(speedy.getServerFolder(mess.getGuild()) + "moderation/troll.txt");

        if(file.exists()){
            file.delete();
            sendMess(mess, "Plus personne n'est bound !");
        } else {
            sendMess(mess, "Personne n'est bound !");
        }
    }
    public void spam(Message mess){
        try {
            String contenuMess = mess.getContentDisplay();
            String[] mots = contenuMess.split(" ");
            List<Member> mentionned = mess.getMentionedMembers();
            if(speedy.testCooldown(mess.getMember(), mess)){
                if (mots.length == 3 && mentionned.size() == 1) {
                    int nb = Integer.parseInt(mots[1]);
                    if (!(nb > 20)) {
                        for (int i = 0; i < nb; i++) {
                            sendMess(mess, "<@" + mentionned.get(0).getUser().getId() + ">");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        sendMess(mess, ":)");
                    } else {
                        sendMess(mess, "Ça fait un peu trop de messages :sweat_smile: (20 maximum)");
                    }
                    speedy.setCooldown(mess.getMember(), mess);
                } else {
                    sendMess(mess, "[Erreur] Syntaxe : `ur/spam <nb de fois> <membre>");
                }
            } else {
                sendMess(mess, "Il y a un cooldown sur cette commande ! Il te reste " + speedy.getCooldown(mess.getMember(), mess) + " secondes à attendre !");
            }
        } catch(NumberFormatException e){
            sendMess(mess, "[Erreur] Syntaxe : `ur/spam <nb de fois> <membre>");
        }
    }
}
