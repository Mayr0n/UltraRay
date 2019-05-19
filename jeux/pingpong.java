package jeux;

import main.speedy;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static main.speedy.espace;
import static main.speedy.sendMess;

public class pingpong {

    public void pingpong(Message mess, Member member){
        String contenuMess = mess.getContentDisplay();
        String[] mots = contenuMess.split(" ");

        if(mots.length == 2 || mots.length == 3){

            File register = new File(speedy.getServerFolder(mess.getGuild()) + "games/pp/" + member.getUser().getId() + ".txt");
            File leaderboard = new File(speedy.getServerFolder(mess.getGuild()) + "leaderboards/pp/" + member.getUser().getId() + ".txt");

            switch(mots[1]){
                case "new":
                    if(mots.length == 3) {
                        try {
                            speedy.testFileExist(register);
                            speedy.testFileExist(leaderboard);
                            FileWriter writer = new FileWriter(register);
                            BufferedWriter bw = new BufferedWriter(writer);

                            bw.write(member.getUser().getName());
                            bw.newLine();
                            bw.write(mots[2]);
                            bw.newLine();
                            bw.write(mess.getChannel().getId());
                            bw.newLine();
                            bw.write("0");
                            bw.newLine();
                            bw.write("0");

                            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(leaderboard), StandardCharsets.UTF_8));
                            writer = new FileWriter(leaderboard);

                            String nbParties = reader.readLine();
                            if(nbParties == null){
                                bw.write("0");
                            } else {
                                int nv = Integer.parseInt(nbParties);
                                nv++;
                                bw.write(Integer.toString(nv));
                            }

                            bw.close();
                            writer.close();
                            reader.close();

                            sendMess(mess, "Tu est maintenant enregistré.e ! Tu peux jouer en écrivant `ping` dans le tchat, bonne chance ! :wink:");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        sendMess(mess, "[Erreur] Syntaxe : `ur/pp new <Niveau>" + espace() + "Sachant qu'il y a les niveaux `facile`, `moyen` et `difficile`");
                    }
                    break;

                case "info":
                    sendMess(mess, "ur/pp est un jeu de pingpong très simple !" + espace() + "Avant de jouer il faut d'abord vous enregistrer avec la commande `ur/pp new <niveau>`" +
                            " puis il suffit de répondre `ping` dans le même salon dans lequel vous vous êtes enregistré.e !");
                    break;

                case "stop":
                    if(register.exists()){
                        register.delete();
                        sendMess(mess, "Tu n'es plus enregistré.e !");
                    } else {
                        sendMess(mess, "Tu n'es même pas enregistré.e !");
                    }
                    break;
            }
        } else {
            sendMess(mess, "[Erreur] Syntaxes possibles : `ur/pp new <niveau>` ; `ur/pp info` ; `ur/pp stop`");
        }
    }
    public void ping(Message mess, Guild server){
        File register = new File(speedy.getServerFolder(mess.getGuild()) + "games/pp/" + mess.getAuthor().getId() + ".txt");
        if(register.exists()){
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(register), StandardCharsets.UTF_8));
                String name = reader.readLine();
                String difficulty = reader.readLine();
                String idSalon = reader.readLine();
                String nomSalon = server.getTextChannelById(idSalon).getName();
                reader.close();

                speedy.sleep(100);

                if(mess.getChannel().getId().equals(idSalon)){
                    Random r = new Random();
                    int i = r.nextInt(100);

                    switch(difficulty){
                        case "facile":
                            if(i <= 25){
                                sendMess(mess, "Point pour moi :muscle:");
                                addPoint(register, "bot");
                            } else if(i >= 50 && i <= 75) {
                                sendMess(mess, "Bien joué, point pour toi :clap:");
                                addPoint(register, "player");
                            } else {
                                sendMess(mess, "Pong :ping_pong:");
                            }
                            break;
                        case "moyen":
                            if(i <= 25){
                                sendMess(mess, "Point pour moi :muscle:");
                                addPoint(register, "bot");
                            } else if(i >= 50 && i <= 63){
                                sendMess(mess, "Bien joué, point pour toi :clap:");
                                addPoint(register, "player");
                            } else {
                                sendMess(mess, "Pong :ping_pong:");
                            }
                            break;
                        case "difficile":
                            if(i <= 12){
                                sendMess(mess, "Point pour moi :muscle:");
                                addPoint(register, "bot");
                            } else if(i >= 50 && i <= 63){
                                sendMess(mess, "Bien joué, point pour toi :clap:");
                                addPoint(register, "player");
                            } else {
                                sendMess(mess, "Pong :ping_pong:");
                            }
                            break;
                    }

                    speedy.sleep(100);

                    BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(register), StandardCharsets.UTF_8));
                    reader2.readLine();
                    reader2.readLine();
                    reader2.readLine();
                    String ptBot = reader2.readLine();
                    String ptPlayer = reader2.readLine();
                    reader2.close();

                    sendMess(mess, ptBot + " pour moi et " + ptPlayer + " pour toi !");

                    if(ptBot.equalsIgnoreCase("11")){
                        sendMess(mess, "Partie terminée ! J'ai gagné ! :muscle:");
                        updateLeaderboard(server, mess.getMember(), "lost");
                    } else if(ptPlayer.equalsIgnoreCase("11")){
                        sendMess(mess, "Partie terminée ! Tu as gagné, bien joué ! :clap:");
                        updateLeaderboard(server, mess.getMember(), "win");
                    }
                } else {
                    sendMess(mess, "Tu n'es pas dans le bon salon ! Tu dois dire `ping` dans le salon #" + nomSalon);
                }
            } catch (IOException ie){
                ie.printStackTrace();
            }
        } else {
            sendMess(mess, "Tu n'es pas enregistré.e ! Ecris `ur/pp info` pour en savoir plus");
        }
    }
    private void addPoint(File file, String who){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

            String name = reader.readLine();
            String difficulté = reader.readLine();
            String IDsalon = reader.readLine();
            String ptBot = reader.readLine();
            String ptPlayer = reader.readLine();

            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);

            bw.write(name);
            bw.newLine();
            bw.write(difficulté);
            bw.newLine();
            bw.write(IDsalon);
            bw.newLine();

            switch(who){
                case "bot":
                    int pointsBot = Integer.parseInt(ptBot);
                    pointsBot++;
                    bw.write(Integer.toString(pointsBot));
                    bw.newLine();
                    bw.write(ptPlayer);
                    break;
                case "player":
                    bw.write(ptBot);
                    bw.newLine();
                    int pointsPlayer = Integer.parseInt(ptPlayer);
                    pointsPlayer++;
                    bw.write(Integer.toString(pointsPlayer));
                    break;
            }
            bw.close();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateLeaderboard(Guild server, Member member, String issue){
        File leaderboard = new File(speedy.getServerFolder(server) + "leaderboards/pp/" + member.getUser().getId() + ".txt");
        speedy.testFileExist(leaderboard);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(leaderboard), StandardCharsets.UTF_8));
            FileWriter writer = new FileWriter(leaderboard);
            BufferedWriter bw = new BufferedWriter(writer);

            String nbParties = reader.readLine();
            String partiesWon = reader.readLine();
            String partiesLost = reader.readLine();
            reader.close();

            if(nbParties == null){
                bw.write("0");
            } else {
                int nb = Integer.parseInt(nbParties);
                nb++;
                bw.write(Integer.toString(nb));
            }
            if(partiesWon == null){
                partiesWon = "0";
            }
            if(partiesLost == null){
                partiesLost = "0";
            }
            bw.newLine();

            switch(issue){
                case "won":
                    int nbWon = Integer.parseInt(partiesWon);
                    nbWon++;
                    bw.write(Integer.toString(nbWon));
                    bw.newLine();
                    bw.write(partiesLost);
                    break;
                case "lose":
                    bw.write(partiesWon);
                    bw.newLine();

                    int nbLose = Integer.parseInt(partiesLost);
                    nbLose++;
                    bw.write(Integer.toString(nbLose));
                    break;
            }

            bw.close();
            writer.close();
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
