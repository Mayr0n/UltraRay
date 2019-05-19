package main;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
    public static Boolean testCooldown(Member member, Message mess){

        Boolean canDo = false;

        File cooldown = new File(speedy.getServerFolder(mess.getGuild()) + "moderation/cooldowns/" + member.getUser().getId() + ".txt");
        speedy.testFileExist(cooldown);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cooldown), StandardCharsets.UTF_8));
            String lastTime = reader.readLine();
            reader.close();
            if(lastTime != null){
                if(Integer.parseInt(speedy.getSecondsAmount()) - Integer.parseInt(lastTime) > 500){
                    canDo = true;
                }
            } else {
                canDo = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return canDo;
    }
    public static void setCooldown(Member member, Message mess){
        File cooldown = new File(speedy.getServerFolder(mess.getGuild()) + "moderation/cooldowns/" + member.getUser().getId() + ".txt");
        speedy.testFileExist(cooldown);
        try {
            FileWriter writer = new FileWriter(cooldown);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(speedy.getSecondsAmount());
            bw.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getCooldown(Member member, Message mess){
        File cooldown = new File(speedy.getServerFolder(mess.getGuild()) + "moderation/cooldowns/" + member.getUser().getId() + ".txt");
        speedy.testFileExist(cooldown);
        String cd = "Il y a eu un problème dans le code";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cooldown), StandardCharsets.UTF_8));
            String lastTime = reader.readLine();
            reader.close();
            int c = 500 - (Integer.parseInt(speedy.getSecondsAmount()) - Integer.parseInt(lastTime));
            cd = Integer.toString(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cd;
    }
    private static String getSecondsAmount(){
        String temps = speedy.getDate().toString().substring(12,19);
        String[] time = temps.split(":");
        int[] seconds = new int[3];
        seconds[0] = Integer.parseInt(time[0]);
        seconds[1] = Integer.parseInt(time[1]);
        seconds[2] = Integer.parseInt(time[2]);
        int secondes = seconds[0]*3600 + seconds[1]*60 + seconds[2];
        return Integer.toString(secondes);
    }

}
