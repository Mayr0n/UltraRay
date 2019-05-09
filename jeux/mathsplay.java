package jeux;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class mathsplay {

    public void mathsplay(Member member, Message mess) {

        String contenuMess = mess.getContentDisplay();
        String[] mots = contenuMess.split(" ");
        File playerFile = new File("data/mathsplay/" + member.getUser().getId() + ".txt");
        
        if (mots.length == 2 || mots.length == 3) {

            try {

                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(playerFile), StandardCharsets.UTF_8));

                switch (mots[1]) {
                    case "respond":
                        if (playerFile.exists()) {
                            reader.readLine();
                            String result = reader.readLine();
                            reader.close();

                            if (mots[2].equals(result)) {
                                sendMess(mess, "Bien joué ! Tu as trouvé le résultat :wink:");
                                playerFile.delete();
                            } else {
                                sendMess(mess, "Non, ce n'est pas le bon résultat !");
                            }
                        } else {
                            sendMess(mess, "Tu n'a pas créé de partie !" + espace() + "Pour en créer une, écris `ur/mathsplay new <nombreMaximal> <signe>`" + espace() +
                                    "Sachant que le nombre maximal peut être au maximum 2147483647, et signe peut être `+`, `-` ou`*`");
                        }
                        break;

                    case "remind":
                        if (playerFile.exists()) {
                            sendMess(mess, "Tu dois trouver la réponse au calcul '" + reader.readLine() + "'");
                        } else {
                            sendMess(mess, "Tu n'a pas créé de partie !" + espace() + "Pour en créer une, écris `ur/mathsplay new <nombreMaximal> <signe>`" + espace() +
                                    "Sachant que le nombre maximal peut être au maximum 2147483647, et signe peut être `+`, `-` ou`*`");
                        }
                        reader.close();
                        break;

                    case "stop":
                        if (playerFile.exists()) {
                            sendMess(mess, "La réponse au calcul '" + reader.readLine() + "' était " + reader.readLine());
                            playerFile.delete();
                        } else {
                            sendMess(mess, "Tu n'a pas créé de partie !" + espace() + "Pour en créer une, écris `ur/mathsplay new <nombreMaximal> <signe>`" + espace() +
                                    "Sachant que le nombre maximal peut être au maximum 2147483647, et signe peut être `+`, `-` ou`*`");
                        }
                        reader.close();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if(mots.length == 4 && mots[1].equals("new")){
            try {
                if (!playerFile.exists()) {
                    playerFile.createNewFile();
                }

                FileWriter writer = new FileWriter(playerFile);
                BufferedWriter bw = new BufferedWriter(writer);

                int nombreMaximal = Integer.parseInt(mots[2]);

                Random r = new Random();
                int t1 = r.nextInt(nombreMaximal);
                int t2 = r.nextInt(nombreMaximal);
                int total = 0;
                String calcul = "";

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


                bw.write(calcul);
                bw.newLine();
                bw.write(Integer.toString(total));

                bw.close();
                writer.close();

                sendMess(mess, "Combien font " + calcul + " ?");
            } catch(IOException e){
                e.printStackTrace();
            }
        } else {
            sendMess(mess, "[Erreur] Syntaxe : ur/mathsplay new <nombreMaximal> <signe>" + espace() + "Sachant que le nombre maximal peut être au maximum 2147483647, et signe peut être `+`, `-` ou`*`");
        }


    }

    private static void sendMess(Message mess, String contenu) {
        mess.getChannel().sendMessage(contenu).queue();
    }

    private static String espace() {
        return "\n" + "\n";
    }


}
