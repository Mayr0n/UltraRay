package moderation;

import main.speedy;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.managers.GuildController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static main.speedy.sendMess;

public class features {

    public void renameAll(Message mess, Guild server){
        String contenuMess = mess.getContentDisplay();
        String[] contMess = contenuMess.split(" ");

        if (contMess.length > 1) {
            contMess[0] = "";
            String newNick = String.join(" ", contMess);
            List<Member> listUser = server.getMembers();

            for (Member m : listUser) {
                GuildController serveradmin = server.getController();
                if(!m.isOwner()) {
                    serveradmin.setNickname(m, newNick).queue();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ee) {
                        System.out.print("oof");
                    }
                }
            }
            sendMess(mess,"Tout le monde a été renommé en ***" + newNick + "*** !");
        } else {

            sendMess(mess,"[Nop !] Syntaxe : ur/renameall <rename en ?>");

        }
    }
    public void getIdsAll(Message mess, Guild server){
        File file = new File("data/servers/" + server.getId() + "/idsall.txt");
        speedy.testFileExist(file);
        try {

            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            List <Member> ms = server.getMembers();

            for(Member m : ms){

                String name = m.getUser().getName();
                String id = m.getUser().getId();
                bw.write(name + " : " + id);
                bw.newLine();
            }

            bw.close();
            writer.close();

            sendMess(mess, "Terminé !");

        } catch (IOException e) {
            e.printStackTrace();
            sendMess(mess, "Une erreur est survenue...");
        }
    }
    public void resetAllNames(Message mess, Guild server){
        List<Member> listUser = server.getMembers();
        GuildController serverAdmin = server.getController();
        for (Member m : listUser) {
            if(!m.isOwner()) {
                serverAdmin.setNickname(m, m.getUser().getName()).queue();
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        sendMess(mess,"Terminé !");
    }
    public void getID(Message mess, Guild server){
        String contenuMess = mess.getContentDisplay();
        String[] message = contenuMess.split(" ");

        if(message.length == 2){

            if(message[1].contains("#") && message[1].contains("@")){

                String[] name = message[1].split("#");
                List <Member> mentionned = server.getMembersByName(name[0], true);
                sendMess(mess, mentionned.get(0).getUser().getId());
            } else {
                sendMess(mess, mess.getChannel().getId());
            }

        } else {
            sendMess(mess, "[Erreur syntaxe !] Syntaxe : `ur/getid <salon ou membre#0000>");
        }
    }
}
