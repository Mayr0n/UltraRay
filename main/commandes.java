package ur.nyroma.main;

import ur.nyroma.jeux.mathsplay;
import ur.nyroma.moderation.features;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import ur.nyroma.jeux.pingpong;
import ur.nyroma.moderation.realModeration;
import ur.nyroma.moderation.troll;

import java.util.ArrayList;
import java.util.List;

import static ur.nyroma.main.speedy.*;

public class commandes {
    public static String prefix = "ur/";
    public void doCommand(Message mess, Guild server) {
        String txtMess = mess.getContentDisplay();
        MessageChannel channel = mess.getChannel();
        Member member = mess.getMember();
        String[] mots = txtMess.split(" ");
        String[] normalCommands = {"help", "say", "joke", "pp", "dab", "mathsPlay"};
        String[] staffCommands = {"renameAll", "bound", "unbound", "spam"};
        String[] ownerCommands = {"resetAllNames", "closeChannels", "openChannels", "sendMpAll", "delete", "renameAll"};
        String[] mayCommands = {"idsAll", "getId", "sendMp", "shutdown"};
        List<String> nCommToTest = new ArrayList<>();
        List<String> sCommToTest = new ArrayList<>();
        List<String> oCommToTest = new ArrayList<>();
        List<String> mCommToTest = new ArrayList<>();

        for (String c : normalCommands) {
            nCommToTest.add(prefix.concat(c));
        }
        for(String s : staffCommands){
            sCommToTest.add(prefix.concat(s));
        }
        for(String o : ownerCommands){
            oCommToTest.add(prefix.concat(o));
        }
        for(String m : mayCommands){
            mCommToTest.add(prefix.concat(m));
        }

        for(int i = 0; i < nCommToTest.size(); i++) {
            if (mots[0].equals(nCommToTest.get(i))) {
                switch (i) {
                    case 0:
                        sendMess(channel, "`ur/say : Pour que @Ultra Ray dise votre message`" + espace() +
                                "`ur/joke : PARCE QUE L'HUMOUR C'EST DROLE`" + espace() +
                                "`ur/pp info : permet d'avoir des informations sur le jeu de 'pingpong' d'Ultra Ray" + espace() +
                                "`ur/dab : DAAAAAAAAAAAAAAAAB`" + espace() +
                                "`ur/mathsplayinfo : permet d'avoir des informations sur le jeu 'mathsplay' d'Ultra Ray");
                        break;
                    case 1:
                        new features().say(mots, mess);
                        break;
                    case 2:
                        new features().tellJoke(mess);
                        break;
                    case 3:
                        new pingpong().pingpong(mess);
                        break;
                    case 4:
                        sendMess(channel, "https://tenor.com/view/dab-dab-problem-dab-dance-gif-5412848");
                        break;
                    case 5:
                        new mathsplay().mathsplay(member, mess);
                        break;
                }
            }
        }
        for(int i = 0 ; i < sCommToTest.size(); i++){
            if(mots[0].equals(sCommToTest.get(i)) && member.hasPermission(Permission.MANAGE_ROLES)){
                switch(i){
                    case 0:
                        new features().renameAll(mess);
                        break;
                    case 1:
                        new troll().bound(mess);
                        break;
                    case 2:
                        new troll().unbound(mess);
                        break;
                    case 3:
                        new troll().spam(mess);
                        break;
                }
            }
        }
        for(int i = 0 ; i < oCommToTest.size(); i++) {
            if (mots[0].equals(oCommToTest.get(i)) && member.isOwner()) {
                switch (i){
                    case 0:
                        new features().resetAllNames(server);
                        break;
                    case 1:
                        new realModeration().closeChannels(server);
                        break;
                    case 2:
                        new realModeration().openChannels(server);
                        break;
                    case 3:
                        new realModeration().sendMPall(mess);
                        break;
                    case 4:
                        new realModeration().delete(channel, Integer.parseInt(mots[1]));
                        break;
                    case 5:
                        new features().renameAll(mess);
                }
                sendPrivMess(mess, member.getUser().getId(), "Terminé !");
            }
        }
        for(int i = 0 ; i < mCommToTest.size(); i++){
            if(mots[0].equals(mCommToTest.get(i)) && member.getUser().getId().equals(idMay)){
                switch(i){
                    case 0:
                        new features().getIdsAll(server);
                        break;
                    case 1:
                        new features().getID(mess);
                        break;
                    case 2:
                        new features().sendMP(mess);
                        break;
                    case 3:
                        speedy.sendMess(channel,"Bip boup.. boup.... bip..........");
                        server.getJDA().shutdown();
                }
            }
        }
        speedy.deleteMess(mess);
    }
}
