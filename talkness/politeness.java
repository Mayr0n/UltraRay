package talkness;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import java.util.Random;

import static main.speedy.sendMess;

public class politeness {

    public void politeness(Message mess, Guild server){
        String contenuMess = mess.getContentDisplay();
        if (contenuMess.contains("Coucou") || contenuMess.contains("Salut") || contenuMess.contains("salut") || contenuMess.contains("coucou")) {
            greetings(mess);
        }

        if (contenuMess.contains("ça va") && mess.getMentionedUsers().contains(server.getJDA().getSelfUser())){
            alright(mess);
        }
    }
    private void greetings(Message mess){
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
    private void alright(Message mess){
        Random r = new Random();
        int i = r.nextInt(3);

        switch (i) {
            case 1: sendMess(mess,"Super et toi ?"); break;
            case 2:sendMess(mess,"Bien, comme toujours :D et toi ?"); break;
            case 3: sendMess(mess,"Trkl et toi ?"); break;
        }
    }

}
