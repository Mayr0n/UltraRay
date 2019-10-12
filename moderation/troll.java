package ur.nyroma.moderation;

import net.dv8tion.jda.core.entities.*;
import ur.nyroma.main.speedy;

import java.io.File;
import java.util.List;
import java.util.Random;

import static ur.nyroma.main.speedy.sendMess;
import static ur.nyroma.main.speedy.sendPrivMess;

public class troll {
    public void bound(Message mess) {
        Guild server = mess.getGuild();
        MessageChannel channel = mess.getChannel();
        User u = mess.getAuthor();
        List<Member> mentionned = mess.getMentionedMembers();
        File file = new File(speedy.getServerFolder(server) + "moderation/troll.txt");

        speedy.testFileExist(file);
        if (file.exists() && mentionned != null) {
            List<String> lines = speedy.getFileContent(file);
            if (mentionned.size() + lines.size() <= 5) {
                for (Member m : mentionned) {
                    if (lines.contains(m.getUser().getId())) {
                        sendPrivMess(server, u.getId(), m.getEffectiveName() + " est déjà bound !");
                        speedy.deleteMess(mess);
                    } else {
                        speedy.writeInFile(file, m.getUser().getId(), false);
                        sendMess(channel, m.getEffectiveName() + " a été bound !");
                    }
                }
            } else {
                sendPrivMess(server, u.getId(), "Tu as mentionné trop de personnes ! Il y a déjà " + lines.size() + " bound, sur 5 :/");
                speedy.deleteMess(mess);
            }
        } else if (mentionned == null) {
            sendPrivMess(server, u.getId(), "[**Erreur**] Syntaxe : `ur/bound <mention(s)>`");
            speedy.deleteMess(mess);
        }
    }

    public void unbound(Message mess) {
        File file = new File(speedy.getServerFolder(mess.getGuild()) + "moderation/troll.txt");
        if (file.exists()) {
            file.delete();
            sendMess(mess.getChannel(), "Plus personne n'est bound !");
        } else {
            sendMess(mess.getChannel(), "Personne n'est bound !");
        }
    }

    public void spam(Message mess) {
        List<Member> mentionned = mess.getMentionedMembers();
        if (mentionned.size() == 1) {
            Thread spam = new Thread(() -> {
                for (int i = 0; i <= 20; i++) {
                    sendMess(mess.getChannel(), "<@" + mentionned.get(0).getUser().getId() + ">");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                sendMess(mess.getChannel(), ":)");
            });
            spam.setDaemon(true);
            spam.start();
        } else {
            sendMess(mess.getChannel(), "Il faut mentionner une seule personne ! Syntaxe : `ur/spam @pseudo`");
        }
    }

    public void testBound(Message mess) {
        MessageChannel channel = mess.getChannel();
        User u = mess.getAuthor();
        File file = new File(speedy.getServerFolder(mess.getGuild()) + "moderation/troll.txt");

        if (file.exists()) {
            List<String> lines = speedy.getFileContent(file);
            if (lines.contains(u.getId())) {
                switch (new Random().nextInt(6)) {
                    case 1:
                        sendMess(channel, "Mais carrément");
                        break;
                    case 2:
                        sendMess(channel, "Chut");
                        break;
                    case 3:
                        Thread t = new Thread(() -> {
                            for (int a = 0; a <= 10; a++) {
                                sendMess(channel, "<@" + u.getId() + ">");
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            sendMess(channel, ":)");
                        });
                        t.setDaemon(true);
                        t.start();
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
                }
            }
        }
    }
}
