package ur.nyroma.jeux;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import ur.nyroma.main.speedy;

import java.io.File;
import java.util.Random;

import static ur.nyroma.main.speedy.espace;
import static ur.nyroma.main.speedy.sendMess;

public class mathsplay {

    public void mathsplay(Member member, Message mess) {

        MessageChannel channel = mess.getChannel();
        String txt = mess.getContentDisplay();
        String[] mots = txt.split(" ");
        File playerFile = new File(speedy.getServerFolder(mess.getGuild()) + "/games/mathsplay/" + member.getUser().getId() + ".txt");

        if (mots.length == 2 || mots.length == 3) {
            switch (mots[1]) {
                case "respond":
                    if (mots[2].equals(speedy.getFileContent(playerFile).get(1))) {
                        sendMess(channel, "Bien joué ! Tu as trouvé le résultat :wink:");
                        playerFile.delete();
                    } else {
                        sendMess(channel, "Non, ce n'est pas le bon résultat !");
                    }
                    break;
                case "remind":
                    sendMess(channel, "Tu dois trouver la réponse au calcul '" + speedy.getFileContent(playerFile).get(0) + "'");
                    break;
                case "stop":
                    sendMess(channel, "La réponse au calcul '" + speedy.getFileContent(playerFile).get(0) + "' était " + speedy.getFileContent(playerFile).get(1));
                    playerFile.delete();
                    break;
            }
        } else if (mots.length == 4 && mots[1].equals("new")) {
            speedy.testFileExist(playerFile);
            String calcul = "";
            int nombreMaximal = Integer.parseInt(mots[2]);
            Random r = new Random();
            int t1 = r.nextInt(nombreMaximal);
            int t2 = r.nextInt(nombreMaximal);
            int total = 0;
            switch (mots[3]) {
                case "+":
                    total = t1 + t2;
                    calcul = t1 + " + " + t2;
                    break;
                case "-":
                    total = t1 - t2;
                    calcul = t1 + " - " + t2;
                    break;
                case "*":
                    total = t1 * t2;
                    calcul = t1 + " * " + t2;
                    break;
            }
            String toWrite = String.join(calcul + "\n" + total);
            speedy.writeInFile(playerFile, toWrite, true);
            sendMess(channel, "Combien font " + calcul + " ?");
        } else if (txt.equals("ur/mathsplayinfo")) {
            sendMess(channel, "Le jeu \"mathsplay\" est un jeu de calcul mental," + espace() +
                    "Vous pouvez lancer une partie via la commande `ur/mathsplay new <nombreMaximal> <signe>`" + espace() +
                    "Sachant que le nombre maximum du nombre maximal est 2147483647, et le signe peut être `+`, `-` ou`*`" + espace() +
                    "Pour répondre, écrivez la commande `ur/mathsplay respond <réponse>`, " + espace() +
                    "Si vous ne vous souvenez plus le bot peut vous rappeler le calcul avec la commande `ur/mathsplay remind`," + espace() +
                    "Et pour stopper la partie faites `ur/mathsplay stop` !");
        } else {
            sendMess(channel, "[Erreur] Syntaxe : `ur/mathsplay new <nombreMaximal> <signe>`" + espace() +
                    "Sachant que le nombre maximal peut être au maximum 2147483647, et signe peut être `+`, `-` ou`*`");
        }
    }
}
