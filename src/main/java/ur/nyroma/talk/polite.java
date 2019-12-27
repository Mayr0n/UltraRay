package ur.nyroma.talk;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import ur.nyroma.main.speedy;

import java.util.Random;

public class polite {

    public void bePolite(Message mess) {
        String txt = mess.getContentRaw();
        for(String mot : txt.split(" ")){
            if(mot.equalsIgnoreCase("coucou") || mot.equalsIgnoreCase("salut")){
                greetings(mess.getChannel());
            }
        }

        if (txt.contains("ça va") && mess.getMentionedUsers().contains(mess.getGuild().getJDA().getSelfUser())) {
            alright(mess.getChannel());
        }
    }

    private void greetings(MessageChannel channel) {
        Random r = new Random();
        int i = r.nextInt(4);
        switch (i) {
            case 1:
                speedy.sendMess(channel, "Yooo");
                break;
            case 2:
                speedy.sendMess(channel, "Salut !");
                break;
            case 3:
                speedy.sendMess(channel, "Coucouuuu");
                break;
            case 4:
                speedy.sendMess(channel, "Wsh wsh canne à pêche");
                break;
        }
    }

    private void alright(MessageChannel channel) {
        Random r = new Random();
        int i = r.nextInt(3);
        switch (i) {
            case 1:
                speedy.sendMess(channel, "Super et toi ?");
                break;
            case 2:
                speedy.sendMess(channel, "Bien, comme toujours :D et toi ?");
                break;
            case 3:
                speedy.sendMess(channel, "Trkl et toi ?");
                break;
        }
    }
}
