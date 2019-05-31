package main;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;

import java.io.*;
import java.util.GregorianCalendar;

public class speedy {
    public static void sendMess(MessageChannel channel, String contenu){
        channel.sendMessage(contenu).queue();
    }
    public static void sendPrivMess(Message mess, String id, String message){
        mess.getGuild().getMemberById(id).getUser().openPrivateChannel().queue(
                (channel) -> channel.sendMessage(message).queue()
        );
    }
    public static void sendPrivMess(Guild server, String id, String message){
        server.getMemberById(id).getUser().openPrivateChannel().queue(
                (channel) -> channel.sendMessage(message).queue()
        );
    }
    public static void addMessEmote(Message mess, String code){ mess.addReaction(code).queue(); }
    public static void delete(Message mess){
        mess.delete().queue();
    }
    public static String espace() {
        return "\n" + "\n";
    }
    public static String idMay = "301715312603168769";
    public static java.util.Date getDate(){
        return new GregorianCalendar().getTime();
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

    public static String getDateDetails(){
        String[] date = speedy.getDate().toString().substring(4,9).split(" ");
        String[] time = speedy.getDate().toString().substring(12,19).split(":");
        int[] seconds = {Integer.parseInt(time[0]),Integer.parseInt(time[1]),Integer.parseInt(time[2])};
        int secondes = seconds[0]*3600 + seconds[1]*60 + seconds[2];
        return secondes + " " + date[0] + " " + date[1];
    }

}
