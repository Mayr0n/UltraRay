package main;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;

public class speedy {
    public static void sendMess(Message mess, String contenu){
        mess.getChannel().sendMessage(contenu).queue();
    }
    public static void addMessEmote(Message mess, String code){ mess.addReaction(code).queue(); }
    public static String espace() {
        return "\n" + "\n";
    }
    public static String idMay = "301715312603168769";
    public static java.util.Date getDate(){
        java.util.GregorianCalendar calendar = new GregorianCalendar();
        java.util.Date time  = calendar.getTime();
        return time;
    }
    public static void testFileExist(File file){
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void testFolderExist(File file){
        if(!file.exists()){
            file.mkdir();
        }
    }
    public static String getServerFolder(Guild server){
        return "data/servers/" + server.getId() + " (" + server.getName() + ")/";
    }
    public static void sleep(int amount){
        try {
            Thread.sleep(amount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
