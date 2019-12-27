package ur.nyroma.moderation;

import net.dv8tion.jda.core.entities.*;
import ur.nyroma.main.speedy;

import java.io.*;
import java.util.List;
import java.util.Random;

public class FeaturesManager {

    public Runnable compteur(MessageChannel channel){
        Runnable compteur = () -> {
            try {
                speedy.sendMess(channel, "3");
                Thread.sleep(1000);
                speedy.sendMess(channel, "2");
                Thread.sleep(1000);
                speedy.sendMess(channel,"1");
                Thread.sleep(200);
                speedy.sendMess(channel, "go");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        return compteur;
    }
    public void resetAllNames(Guild server) {
        Thread clear = new Thread(() -> {
            List<Member> listUser = server.getMembers();
            for (Member m : listUser) {
                if (!m.isOwner()) {
                    server.getController().setNickname(m, m.getUser().getName()).queue();
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        clear.setDaemon(true);
        clear.start();
    }
    public void renameAll(Message mess) {
        String txt = mess.getContentRaw();
        String[] mots = txt.split(" ");
        MessageChannel channel = mess.getChannel();
        Guild server = mess.getGuild();
        if (mots.length > 1) {
            Thread rename = new Thread(() -> {
                mots[0] = "";
                String newNick = String.join(" ", mots);
                List<Member> listUser = server.getMembers();
                for (Member m : listUser) {
                    if (!m.isOwner()) {
                        server.getController().setNickname(m, newNick).queue();
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
            speedy.sendMess(channel, "[**Erreur**] Syntaxe : `ur/renameall <rename en ?>`");
        }
    }
    public void sendMP(Message mess) {
        String[] mots = mess.getContentDisplay().split(" ");
        StringBuilder message = new StringBuilder();
        for (int i = 2; i < mots.length; i++) {
            message.append(mots[i]);
        }
        speedy.sendPrivMess(mess, mots[1], message.toString());
        speedy.sendPrivMess(mess.getGuild(), speedy.idMay, "Terminé !");
    }
    public void countMessages(Guild server) {
        File counterFolder = new File(speedy.getServerFolder(server) + "moderation/counters/");
        File counter = new File(speedy.getServerFolder(server) + "moderation/counters/" + speedy.getDate().toString().substring(0, 10) + ".txt");
        speedy.testFolderExist(counterFolder);
        speedy.testFileExist(counter);
        List<String> lines = speedy.getFileContent(counter);
        if (lines.size() == 0) {
            speedy.writeInFile(counter, "1", true);
        } else {
            String nb = lines.get(0);
            int i = Integer.parseInt(nb);
            i++;
            speedy.writeInFile(counter, Integer.toString(i), true);
        }
    }
}
