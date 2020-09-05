package xyz.nyroma.main;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;

public class MainUtils {
    public static boolean isStaff(Member member) {
        return member.hasPermission(Permission.KICK_MEMBERS);
    }
    public static void sendMess(MessageChannel channel, String contenu){
        channel.sendMessage(contenu).queue();
    }
    public static void deleteMess(Message mess){
        mess.delete().queue();
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
    public static String idMay = "301715312603168769";
    public static Optional<TextChannel> getChannelByName(Guild server, String name) {
        for(TextChannel ch : server.getTextChannels()){
            if(ch.getName().equals(name)){
                return Optional.of(ch);
            }
        }
        return Optional.empty();
    }
    public static String getTime() {
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY) + 6;
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        String min;
        String h;
        String s;
        if (hours >= 24) {
            h = "0" + (hours - 24);
        } else {
            h = String.valueOf(hours);
        }
        if (minutes < 10) {
            min = "0" + minutes;
        } else {
            min = String.valueOf(minutes);
        }
        if (seconds < 10) {
            s = "0" + seconds;
        } else {
            s = String.valueOf(seconds);
        }
        return h + ":" + min + ":" + s;
    }
}
