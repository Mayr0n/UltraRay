package xyz.nyroma.neter;

import fr.may.processus.DataSorter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import xyz.nyroma.main.MainUtils;

import java.util.Hashtable;
import java.util.Set;

public class AIManager {
    private DataSorter sorter;

    public AIManager(boolean log){
        this.sorter = new DataSorter(log);
    }

    public void register(String txt, Guild server){
        Thread t = new Thread(() -> {
            Hashtable<String, Boolean> added = sorter.addWords(txt);
            Set<String> set = added.keySet();
            for(String s : set){
                if(added.get(s)){
                    TextChannel channel = MainUtils.getChannelByName(server, "ia_logs").isPresent()
                            ? MainUtils.getChannelByName(server, "ia_logs").get() : server.createTextChannel("ia_logs").complete();
                    channel.getManager().setTopic("Ici seront répertoriés les logs de l'IA.").queue();
                    MainUtils.sendMess(channel, "**" + s + "** a été enregistré.");
                }
            }
        });
        t.start();
        System.out.println("enregistré !");
    }

}
