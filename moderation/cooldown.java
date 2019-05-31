package moderation;

import main.speedy;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class cooldown {

    public static Boolean testCooldown(Member member, Message mess){
        Boolean canDo = false;
        File cooldown = new File(speedy.getServerFolder(mess.getGuild()) + "moderation/cooldowns/" + member.getUser().getId() + ".txt");
        speedy.testFileExist(cooldown);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(cooldown), StandardCharsets.UTF_8));
            String lastTime = reader.readLine();
            reader.close();
            String[] time = speedy.getDateDetails().split(" ");
            if(lastTime == null){
                canDo = true;
            } else if(lastTime.contains(time[1]) && lastTime.contains(time[2])){
                canDo = Integer.parseInt(time[0]) - Integer.parseInt(lastTime) > 500;
            } else {
                canDo = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(canDo){
            setCooldown(member, mess);
        }
        return canDo;
    }
    public static void setCooldown(Member member, Message mess){
        File cooldown = new File(speedy.getServerFolder(mess.getGuild()) + "moderation/cooldowns/" + member.getUser().getId() + ".txt");
        speedy.testFileExist(cooldown);
        try {
            FileWriter writer = new FileWriter(cooldown);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(speedy.getDateDetails());
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
            cd = Integer.toString((Integer.parseInt(speedy.getDateDetails().split(" ")[0]) - Integer.parseInt(lastTime)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cd;
    }
}
