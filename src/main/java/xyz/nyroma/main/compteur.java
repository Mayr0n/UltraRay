package xyz.nyroma.main;

import net.dv8tion.jda.api.entities.MessageChannel;

public class compteur implements Runnable {
    private MessageChannel channel;
    public compteur(MessageChannel channel){
        this.channel = channel;
    }

    @Override
    public void run() {
        try {
            speedy.sendMess(this.channel, "3");
            Thread.sleep(1000);
            speedy.sendMess(this.channel, "2");
            Thread.sleep(1000);
            speedy.sendMess(this.channel,"1");
            Thread.sleep(200);
            speedy.sendMess(this.channel, "go");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
