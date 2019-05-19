package talkness;

import net.dv8tion.jda.core.entities.Message;

import static main.speedy.addMessEmote;
import static main.speedy.sendMess;

public class wordsGames {

    public void test(Message mess){
        String[] mots = mess.getContentDisplay().split(" ");
        String contenuMess = mess.getContentDisplay();
        for(String mot : mots){
            if(mot.equalsIgnoreCase("ah")){
                addMessEmote(mess, ":ah:401027642373177344");
            }
            if(mot.equalsIgnoreCase("k")){
                sendMess(mess, "k");
            }
            if(mot.equalsIgnoreCase("oof")){
                addMessEmote(mess, ":OOF:524622837953200138");
            }
        }

        if(contenuMess.length() > 6 && contenuMess.substring(0,6).equals("/spawn") &&
                mess.getMentionedMembers().size() == 1 && mess.getMentionedMembers().get(0).getUser().equals(mess.getGuild().getJDA().getSelfUser())){
            sendMess(mess, "Plop");
        }
    }
}
