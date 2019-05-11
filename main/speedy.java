package main;

import net.dv8tion.jda.core.entities.Message;

public class speedy {
    public static void sendMess(Message mess, String contenu){
        mess.getChannel().sendMessage(contenu).queue();
    }
    public static String espace() {
        return "\n" + "\n";
    }
    public static String idMay = "301715312603168769";
}
