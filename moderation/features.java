package ur.nyroma.moderation;

import net.dv8tion.jda.core.entities.*;
import ur.nyroma.main.speedy;

import java.io.*;
import java.util.List;
import java.util.Random;

public class features {
    public void say(String[] mots, Message mess) {
        if (mots.length >= 2) {
            speedy.deleteMess(mess);
            speedy.sendMess(mess.getChannel(), mess.getContentRaw().substring(6) + " ||(" + mess.getAuthor().getName() + ")||");
        } else {
            speedy.sendMess(mess.getChannel(), "Mais quel message dire ? `Syntaxe : ur/say <Message>`");
        }
    }
    public void tellJoke(Message mess) {
        Guild server = mess.getGuild();
        try {
            File jokeFolder = new File("data/jokes/");
            if (jokeFolder.exists()) {
                File[] jokes = jokeFolder.listFiles();
                File joke = jokes[new Random().nextInt(jokes.length)];
                for (String s : speedy.getFileContent(joke)) {
                    speedy.sendMess(mess.getChannel(), s);
                }
            } else {
                speedy.sendPrivMess(server, speedy.idMay, "Le fichier de jokes n'a pas été initialisé...");
            }
        } catch (NullPointerException e) {
            speedy.sendPrivMess(server, speedy.idMay, "Il n'y a pas de jokes dans le fichier jokes !...");
        }

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
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                speedy.sendMess(channel, "Tout le monde a été renommé en ***" + newNick + "*** !");
            });
            rename.setDaemon(false);
            rename.start();
        } else {
            speedy.sendMess(channel, "[**Erreur**] Syntaxe : `ur/renameall <rename en ?>`");
        }
    }
    public void getIdsAll(Guild server) {
        File file = new File("data/servers/" + server.getId() + "/idsall.txt");
        speedy.testFileExist(file);
        Thread t = new Thread(() -> {
            String txt = "";
            for(Member m : server.getMembers()){
                User u = m.getUser();
                txt = String.join(u.getName() + " : " + u.getId() + "\n");
            }
            speedy.writeInFile(file, txt, true);
        });
        t.setDaemon(true);
        t.start();
        speedy.sendPrivMess(server, speedy.idMay, "Terminé !");
    }
    public void getID(Message mess) {
        try {
            for (Member m : mess.getMentionedMembers()) {
                speedy.sendPrivMess(mess.getGuild(), speedy.idMay, m.getUser().getId());
            }
        } catch(NullPointerException e){
            speedy.sendPrivMess(mess.getGuild(), speedy.idMay, "Tu n'a mentionné personne...");
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
        String nb = speedy.getFileContent(counter).get(0);
        if (nb == null) {
            speedy.writeInFile(counter, "1", true);
        } else {
            int i = Integer.parseInt(nb);
            i++;
            speedy.writeInFile(counter, Integer.toString(i), true);
        }
    }
}
