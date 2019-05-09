package jeux;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class pingpong {

    public void pingpong(Message mess, Member member){
        String contenuMess = mess.getContentDisplay();
        String[] mots = contenuMess.split(" ");

        if(mots.length == 2 || mots.length == 3){
            switch(mots[1]){
                case "new":
                    if(mots.length == 3) {
                        try {
                            File register = new File("data/pingpong/" + member.getUser().getId() + ".txt");
                            if (!register.exists()) {
                                register.createNewFile();
                            }
                            FileWriter writer = new FileWriter(register);
                            BufferedWriter bw = new BufferedWriter(writer);

                            bw.write(member.getUser().getName());
                            bw.newLine();
                            bw.write(mots[2]);
                            bw.newLine();
                            bw.write(mess.getChannel().getId());

                            bw.close();
                            writer.close();

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
                    File register = new File("data/pingpong/"  + member.getUser().getId() + ".txt");

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
        File file = new File("data/pingpong/" + mess.getAuthor().getId() + ".txt");

        if(file.exists()){

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));

                reader.readLine();
                String difficulty = reader.readLine();
                String idSalon = reader.readLine();
                String nomSalon = server.getTextChannelById(idSalon).getName();

                reader.close();

                if(mess.getChannel().getId().equals(idSalon)){
                    Random r = new Random();
                    int i = r.nextInt(100);

                    switch(difficulty){
                        case "facile":
                            if(i <= 25){
                                sendMess(mess, "Point pour moi :muscle:");
                            } else if(i >= 50 && i <= 75) {
                                sendMess(mess, "Bien joué, point pour toi :clap:");
                            } else {
                                sendMess(mess, "Pong :ping_pong:");
                            }
                            break;
                        case "moyen":
                            if(i <= 25){
                                sendMess(mess, "Point pour moi :muscle:");
                            } else if(i >= 50 && i <= 63){
                                sendMess(mess, "Bien joué, point pour toi :clap:");
                            } else {
                                sendMess(mess, "Pong :ping_pong:");
                            }
                            break;
                        case "difficile":
                            if(i <= 12){
                                sendMess(mess, "Point pour moi :muscle:");
                            } else if(i >= 50 && i <= 63){
                                sendMess(mess, "Bien joué, point pour toi :clap:");
                            } else {
                                sendMess(mess, "Pong :ping_pong:");
                            }
                            break;
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

    private static void sendMess(Message mess, String contenu){
        mess.getChannel().sendMessage(contenu).queue();
    }

    private static String espace() {return "\n" + "\n";}
}
