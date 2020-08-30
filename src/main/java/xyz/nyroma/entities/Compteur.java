package xyz.nyroma.entities;

import net.dv8tion.jda.api.entities.MessageChannel;
import xyz.nyroma.main.MainUtils;

public class Compteur implements Runnable {
    private MessageChannel channel;
    public Compteur(MessageChannel channel){
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            MainUtils.sendMess(this.channel, "3");
            Thread.sleep(1000);
            MainUtils.sendMess(this.channel, "2");
            Thread.sleep(1000);
            MainUtils.sendMess(this.channel,"1");
            Thread.sleep(200);
            MainUtils.sendMess(this.channel, "go");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
