package xyz.nyroma.neter;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.nyroma.main.SpeedyException;
import xyz.nyroma.main.speedy;

import java.util.List;
import java.util.Random;

public class NeterListeners extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if(!e.getAuthor().isBot()){
            Message mess = e.getMessage();
            String txt = mess.getContentRaw();
            Guild server = e.getGuild();
            TextChannel channel = mess.getTextChannel();

            try {
                if (txt.contains("@someone") && speedy.isStaff(e.getMember())) {
                    List<Member> members = server.getMembers();
                    speedy.sendMess(channel, "<@" +
                            members.get(new Random().nextInt(members.size())).getUser().getId() + ">"
                    );
                }
            } catch(SpeedyException ignored){
            }
        }
    }
}
