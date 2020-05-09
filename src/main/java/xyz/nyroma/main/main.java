package xyz.nyroma.main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import xyz.nyroma.gelp.GelpListeners;
import xyz.nyroma.gelp.JokeCache;
import xyz.nyroma.gelp.TrollingCache;
import xyz.nyroma.ray.BlackCache;
import xyz.nyroma.neter.NeterListeners;

import javax.security.auth.login.LoginException;

public class main {
    private static JDA bot;

    public static void main(String[] args){
        JDABuilder builder = new JDABuilder("");
        builder.setActivity(Activity.watching("https://discord.gg/9VfbD6d"));
        GelpListeners gl = new GelpListeners();
        builder.addEventListeners(new MainListeners(gl));
        builder.addEventListeners(gl);
        builder.addEventListeners(new NeterListeners());
        try {
            bot = builder.build();
        } catch(LoginException e){
            e.printStackTrace();
        }
        System.out.println("Yoooooooooo je suis co !");
        BlackCache.setup();
        System.out.println("Blacklist activée.");
        TrollingCache.setup();
        System.out.println("Trolls activés.");
        JokeCache.setup();
        System.out.println("Jokes activées.");
    }
}
