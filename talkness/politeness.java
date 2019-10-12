package ur.nyroma.talk;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import ur.nyroma.main.speedy;

import java.util.Random;

public class politeness {

    public void politeness(Message mess, Guild server) {
        String txt = mess.getContentRaw();

        if (txt.contains("Coucou") || txt.contains("Salut") || txt.contains("salut") || txt.contains("coucou")) {
            greetings(mess.getChannel());
        }

        if (txt.contains("ça va") && mess.getMentionedUsers().contains(server.getJDA().getSelfUser())) {
            alright(mess.getChannel());
        }
    }

    public void politenessPrivate(Message mess) {
        String txt = mess.getContentRaw();

        if (txt.contains("Coucou") || txt.contains("Salut") || txt.contains("salut") || txt.contains("coucou")) {
            greetings(mess.getChannel());
        }
        if (txt.contains("ça va")) {
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
