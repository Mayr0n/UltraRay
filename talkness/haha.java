package ur.nyroma.talk;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import ur.nyroma.main.speedy;

import static ur.nyroma.main.speedy.addMessEmote;
import static ur.nyroma.main.speedy.sendMess;

public class haha {

    public void test(Message mess){
        String[] mots = mess.getContentDisplay().split(" ");
        String txt = mess.getContentDisplay();
        MessageChannel channel = mess.getChannel();
        for(String mot : mots){
            if(mot.equalsIgnoreCase("ah")){
                addMessEmote(mess, ":ah:401027642373177344");
            }
            if(mot.equalsIgnoreCase("k") && txt.length() == 1){
                sendMess(channel, "k");
            }
            if(mot.equalsIgnoreCase("oof")){
                addMessEmote(mess, ":OOF:524622837953200138");
            }
        }
        int i = mots.length - 1;
        if(mots[i].equals("quoi")){
            sendMess(channel, "feur");
        } else if(txt.equalsIgnoreCase("carré") || mots[i].equalsIgnoreCase("carré")){
            speedy.sendMess(channel, "gnagna carré");
            speedy.addMessEmote(mess,  ":haaan:575457625324519475");
        }
    }
}
