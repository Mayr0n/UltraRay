package ur.nyroma.jeux;

import net.dv8tion.jda.core.entities.*;
import ur.nyroma.main.speedy;

import java.io.*;
import java.util.List;
import java.util.Random;

import static ur.nyroma.main.speedy.espace;
import static ur.nyroma.main.speedy.sendMess;

public class pingpong {

    public void pingpong(Message mess) {
        MessageChannel channel = mess.getChannel();
        User u = mess.getAuthor();
        String txt = mess.getContentDisplay();
        String[] mots = txt.split(" ");

        if (mots.length == 2 || mots.length == 3) {
            File register = new File(speedy.getServerFolder(mess.getGuild()) + "games/pp/" + u.getId() + ".txt");
            switch (mots[1]) {
                case "new":
                    if (mots.length == 3) {
                        speedy.testFileExist(register);
                        String toWrite = String.join(
                                u.getName() + espace() +
                                        mots[2] + espace() +
                                        channel.getId() + espace() +
                                        "0" + espace() +
                                        "0"
                        );
                        speedy.writeInFile(register, toWrite, true);
                        sendMess(channel, "Tu est maintenant enregistré.e ! Tu peux jouer en écrivant `ping` dans le tchat, bonne chance ! :wink:");
                    } else {
                        sendMess(channel, "[Erreur] Syntaxe : `ur/pp new <Niveau>" + espace() + "Sachant qu'il y a les niveaux `facile`, `moyen` et `difficile`");
                    }
                    break;
                case "info":
                    sendMess(channel,
                            "ur/pp est un jeu de pingpong très simple !" + espace() +
                                    "Avant de jouer il faut d'abord vous enregistrer avec la commande `ur/pp new <niveau>` " +
                                    "puis il suffit de répondre `ping` dans le même salon dans lequel vous vous êtes enregistré.e !");
                    break;
                case "stop":
                    if (register.exists()) {
                        register.delete();
                        sendMess(channel, "Tu n'es plus enregistré.e !");
                    } else {
                        sendMess(channel, "Tu n'es même pas enregistré.e !");
                    }
                    break;
            }
        } else {
            sendMess(channel, "[Erreur] Syntaxes possibles : `ur/pp new <niveau>` ; `ur/pp info` ; `ur/pp stop`");
        }
    }

    public void ping(Message mess) {
        MessageChannel channel = mess.getChannel();
        Guild server = mess.getGuild();
        File register = new File(speedy.getServerFolder(mess.getGuild()) + "games/pp/" + mess.getAuthor().getId() + ".txt");

        if (register.exists()) {
            List<String> content = speedy.getFileContent(register);
            String difficulty = content.get(1);
            String idSalon = content.get(2);
            String nomSalon = server.getTextChannelById(idSalon).getName();

            if (channel.getId().equals(idSalon)) {
                Random r = new Random();
                int i = r.nextInt(100);
                switch (difficulty) {
                    case "facile":
                        if (i <= 25) {
                            sendMess(channel, "Point pour moi :muscle:");
                            addPoint(register, "bot");
                        } else if (i >= 50 && i <= 75) {
                            sendMess(channel, "Bien joué, point pour toi :clap:");
                            addPoint(register, "player");
                        } else {
                            sendMess(channel, "Pong :ping_pong:");
                        }
                        break;
                    case "moyen":
                        if (i <= 25) {
                            sendMess(channel, "Point pour moi :muscle:");
                            addPoint(register, "bot");
                        } else if (i >= 50 && i <= 63) {
                            sendMess(channel, "Bien joué, point pour toi :clap:");
                            addPoint(register, "player");
                        } else {
                            sendMess(channel, "Pong :ping_pong:");
                        }
                        break;
                    case "difficile":
                        if (i <= 12) {
                            sendMess(channel, "Point pour moi :muscle:");
                            addPoint(register, "bot");
                        } else if (i >= 50 && i <= 63) {
                            sendMess(channel, "Bien joué, point pour toi :clap:");
                            addPoint(register, "player");
                        } else {
                            sendMess(channel, "Pong :ping_pong:");
                        }
                        break;
                }
                content.clear();
                content = speedy.getFileContent(register);
                String ptBot = content.get(3);
                String ptPlayer = content.get(4);
                sendMess(channel, ptBot + " pour moi et " + ptPlayer + " pour toi !");

                if (ptBot.equalsIgnoreCase("11")) {
                    sendMess(channel, "Partie terminée ! J'ai gagné ! :muscle:");
                } else if (ptPlayer.equalsIgnoreCase("11")) {
                    sendMess(channel, "Partie terminée ! Tu as gagné, bien joué ! :clap:");
                }
            } else {
                sendMess(channel, "Tu n'es pas dans le bon salon ! Tu dois dire `ping` dans le salon #" + nomSalon);
            }
        } else {
            sendMess(channel, "Tu n'es pas enregistré.e ! Ecris `ur/pp info` pour en savoir plus");
        }
    }

    private void addPoint(File file, String who) {
        List<String> lines = speedy.getFileContent(file);
        String name = lines.get(0);
        String difficulty = lines.get(1);
        String IDsalon = lines.get(2);
        String ptBot = lines.get(3);
        String ptPlayer = lines.get(4);
        int pointsBot = Integer.parseInt(ptBot);
        int pointsPlayer = Integer.parseInt(ptPlayer);

        switch (who) {
            case "bot":
                pointsBot++;
                break;
            case "player":
                pointsPlayer++;
                break;
        }
        String toWrite = String.join(
                name + espace() +
                        difficulty + espace() +
                        IDsalon + espace() +
                        pointsBot + espace() +
                        pointsPlayer + espace()
        );
        speedy.writeInFile(file, toWrite, true);
    }
}