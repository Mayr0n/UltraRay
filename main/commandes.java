package main;

import moderation.features;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.util.ArrayList;
import java.util.List;

import static main.speedy.*;

public class commandes {
    public static String prefix = "ur/";
    public void doCommand(Message mess, Guild server, Member member) {
        String txtMess = mess.getContentDisplay();
        MessageChannel channel = mess.getChannel();
        String[] mots = txtMess.split(" ");
        String[] normalCommands = {"help", "say", "joke", "pp", "dab", "mathsPlay"};
        String[] staffCommands = {"kick", "renameAll", "bound", "unbound", "spam"};
        String[] ownerCommands = {"resetAllNames", "closeChannels", "openChannels", "sendMpAll", "delete", "renameAll"};
        String[] mayCommands = {"idsAll", "getId", "sendMp", "getMessages", "getMessagesDel"};
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
                        new features().tellJoke(channel);
                        break;
                    case 3:
                        new jeux.pingpong().pingpong(mess, member);
                        break;
                    case 4:
                        sendMess(channel, "https://tenor.com/view/dab-dab-problem-dab-dance-gif-5412848");
                        break;
                    case 5:
                        new jeux.mathsplay().mathsplay(member, mess);
                        break;
                }
            }
        }
        for(int i = 0 ; i < sCommToTest.size(); i++){
            if(mots[0].equals(sCommToTest.get(i)) && member.hasPermission(Permission.MANAGE_ROLES)){
                switch(i){
                    case 0:
                        new moderation.realModeration().kick(mess, server);
                        break;
                    case 1:
                        new moderation.features().renameAll(mess, server);
                        break;
                    case 2:
                        new moderation.troll().bound(mess, server);
                        break;
                    case 3:
                        new moderation.troll().unbound(mess);
                        break;
                    case 4:
                        new moderation.troll().spam(mess);
                        break;
                }
            }
        }
        for(int i = 0 ; i < oCommToTest.size(); i++) {
            if (mots[0].equals(oCommToTest.get(i)) && member.isOwner()) {
                switch (i){
                    case 0:
                        new moderation.features().resetAllNames(server);
                        break;
                    case 1:
                        new moderation.realModeration().closeChannels(server);
                        break;
                    case 2:
                        new moderation.realModeration().openChannels(server);
                        break;
                    case 3:
                        new moderation.realModeration().sendMPall(mess);
                        break;
                    case 4:
                        new moderation.realModeration().delete(channel, Integer.parseInt(mots[1]));
                        break;
                    case 5:
                        new moderation.features().renameAll(mess, server);
                }
                sendPrivMess(mess, member.getUser().getId(), "Terminé !");
            }
        }
        for(int i = 0 ; i < mCommToTest.size(); i++){
            if(mots[0].equals(mCommToTest.get(i)) && member.getUser().getId().equals(idMay)){
                switch(i){
                    case 0:
                        new moderation.features().getIdsAll(server);
                        break;
                    case 1:
                        new moderation.features().getID(mess);
                        break;
                    case 2:
                        new moderation.features().sendMP(mess);
                        break;
                    case 3:
                        new moderation.features().getMessagesInChannel(channel, server);
                        break;
                    case 4:
                        new moderation.features().getMessagesInChannelD(channel, server, Integer.parseInt(mots[1]));
                }
            }
        }
    }
}
