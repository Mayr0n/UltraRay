package xyz.nyroma.gelp;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.nyroma.main.SpeedyException;
import xyz.nyroma.main.speedy;

public class GelpListeners extends ListenerAdapter {
    private Guild server = null;

    public void setServer(Guild server){
        this.server = server;
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Message mess = e.getMessage();
        Guild server = e.getGuild();

        new PolitenessManager().build(e.getMessage());
        new FunnyManager().build(e.getMessage());

        TrollingManager tm;
        try {
             tm = new TrollingCache().get(server);
        } catch (TrollingException e1) {
            tm = new TrollingManager(server.getId());
        }

        if(tm.getBounds().contains(mess.getMember().getId())){
            tm.annoy(mess);
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
        User u = e.getAuthor();
        Message mess = e.getMessage();
        if (!u.equals(e.getJDA().getSelfUser())) {
            String message =
                    u.getName() + "#" + u.getDiscriminator() + " (<@" + u.getId() + ">) \n" +
                            "Date : " + speedy.getDate() + "\n" +
                            "Message : " + mess.getContentRaw();
            TextChannel channel;
            try {
                channel = speedy.getChannelByName(server, "mps");
            } catch (SpeedyException e1) {
                channel = server.createTextChannel("mps").complete();
                channel.getManager().setTopic("Ici seront répertoriés les mps envoyés au bot.").queue();
            }
            speedy.sendMess(channel, ">>> " + message);

            new PolitenessManager().build(mess);
        }
    }

}
