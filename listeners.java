import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class listeners extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {


        if (!e.getAuthor().isBot() && e.getGuild() != null) {

            Message mess = e.getMessage();
            String contenuMess = mess.getContentDisplay();
            Guild server = e.getGuild();

            if (contenuMess.contains("ur/")) {
                new Commandes().testCommande(mess, server, e.getMember());
            }

            if (contenuMess.equalsIgnoreCase("ping")) {
                new jeux.pingpong().ping(mess, server);
            }

            if (contenuMess.contains("Coucou") || contenuMess.contains("Salut") || contenuMess.contains("salut") || contenuMess.contains("coucou")) {

                Random r = new Random();
                int i = r.nextInt(4);

                switch (i) {

                    case 1:
                        sendMess(mess,"Yooo");
                        break;
                    case 2:
                        sendMess(mess,"Salut !");
                        break;
                    case 3:
                        sendMess(mess,"Coucouu");
                        break;
                    case 4:
                        sendMess(mess,"Wsh");
                        break;

                }


            }

            if (contenuMess.contains("ça va")){
                List<User> mentionned = mess.getMentionedUsers();
                server.getJDA().getSelfUser();
                if(mentionned.contains(server.getJDA().getSelfUser())){
                    Random r = new Random();
                    int i = r.nextInt(3);

                    switch (i) {

                        case 1:
                            sendMess(mess,"Super et toi ?");
                            break;
                        case 2:
                            sendMess(mess,"Bien, comme toujours :D et toi ?");
                            break;
                        case 3:
                            sendMess(mess,"Trkl et toi ?");
                            break;

                    }
                }
            }

            censure(e);
        }

    }
    private static void registerInfr(User u, File file) {

        try {
            FileWriter writer = new FileWriter(file);
            PrintWriter bw = new PrintWriter(writer);
            java.util.GregorianCalendar calendar = new GregorianCalendar();
            java.util.Date time  = calendar.getTime();
            bw.write(u.getName() + " (" + u.getId() + "), date : " + time);
            bw.close();
            writer.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    private static void sendMess(Message mess, String contenu){
        mess.getChannel().sendMessage(contenu).queue();
    }
    private void censure(MessageReceivedEvent e) {

        Message mess = e.getMessage();
        String contenuMess = mess.getContentDisplay();

        File censorWords = new File("data/censure.txt");

        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(censorWords), StandardCharsets.UTF_8));

            try {

                String line = reader.readLine();

                while (line != null) {

                    if (contenuMess.contains(line)) {

                        mess.delete().queue();
                        sendMess(mess,"Hey " + e.getAuthor().getName() + " ! restes poli s'il te plaît ! '" + line + "' est censuré");
                        line = null;
                        File userInfr = new File("data/infractions.txt");
                        registerInfr(e.getAuthor(), userInfr);
                    } else {
                        line = reader.readLine();
                    }
                }
                reader.close();

            } catch (IOException ee) {
                sendMess(mess,"Problème dans le reader...");
            }
        } catch (FileNotFoundException ee) {
            sendMess(mess,"Le fichier de censure a pas été trouvé...");
        }
    }
}
