package ur.nyroma.moderation;

import net.dv8tion.jda.core.entities.*;
import ur.nyroma.main.speedy;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import static ur.nyroma.main.speedy.sendMess;
import static ur.nyroma.main.speedy.sendPrivMess;

public class TrollingManager {
    public boolean bound(List<Member> mentionned, String authorID, Guild server) {
        File file = new File(speedy.getServerFolder(server) + "moderation/TrollingManager.txt");
        speedy.testFileExist(file);
        if (file.exists() && mentionned != null) {
            List<String> lines = speedy.getFileContent(file);
            if (mentionned.size() + lines.size() <= 5) {
                for (Member m : mentionned) {
                    if (lines.contains(m.getUser().getId())) {
                        sendPrivMess(server, authorID, m.getEffectiveName() + " est déjà bound !");
                        return false;
                    } else {
                        speedy.writeInFile(file, m.getUser().getId() + "\n", false);
                        return true;
                    }
                }
                return false;
            } else {
                sendPrivMess(server, authorID, "Tu as mentionné trop de personnes ! Il y a déjà " + lines.size() + " bound, sur 5 :/");
                return false;
            }
        } else {
            sendPrivMess(server, authorID, "[**Erreur**] Syntaxe : `ur/bound <mention(s)>`");
            return false;
        }
    }

    public boolean unbound(Guild server, String memberID) {
        File file = new File(speedy.getServerFolder(server) + "moderation/TrollingManager.txt");
        if (file.exists()) {
            try {
                if(memberID.equals("all")){
                    file.delete();
                } else if (speedy.fileHas(file, memberID, false)) {
                    List<String> lines = speedy.getFileContent(file);
                    file.delete();
                    file.createNewFile();
                    for (String l : lines) {
                        if (!l.contains(memberID)) {
                            speedy.writeInFile(file, l + "\n", false);
                        }
                    }
                }
                return true;
            } catch (IOException e){
                return false;
            }
        } else {
            return false;
        }
    }

    public void spam(List<Member> mentionned, MessageChannel channel) {
        if (mentionned.size() == 1) {
            Thread spam = new Thread(() -> {
                for (int i = 0; i <= 20; i++) {
                    sendMess(channel, "<@" + mentionned.get(0).getUser().getId() + ">");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                sendMess(channel, ":)");
            });
            spam.setDaemon(true);
            spam.start();
        } else {
            sendMess(channel, "Il faut mentionner une seule personne ! Syntaxe : `ur/spam @pseudo`");
        }
    }

    public void testBound(Message mess) {
        MessageChannel channel = mess.getChannel();
        User u = mess.getAuthor();
        File file = new File(speedy.getServerFolder(mess.getGuild()) + "moderation/TrollingManager.txt");

        if (file.exists()) {
            List<String> lines = speedy.getFileContent(file);
            if (lines.contains(u.getId())) {
                switch (new Random().nextInt(10)+1) {
                    case 1:
                        sendMess(channel, "Mais carrément");
                        break;
                    case 2:
                        sendMess(channel, "Chut");
                        break;
                    case 3:
                        sendMess(channel, "je t'aime :3");
                        break;
                    case 4:
                        sendMess(channel, "C'est chiant");
                        break;
                    case 5:
                        sendMess(channel, "Mais tg");
                        break;
                    case 6:
                        speedy.deleteMess(mess);
                        sendMess(channel, "Oups ! Supprimé... C'était surement pas intéressant toute façon");
                        break;
                    case 7:
                        sendMess(channel, "ok");
                        break;
                    case 8:
                        sendMess(channel, "OMG JE VIENS DE GAGNEZ 1 MILLION D'EURO SUR BRAVOLOTO");
                        break;
                }
            }
        }
    }
}
