package xyz.nyroma.main;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.Role;
import xyz.nyroma.caches.ServerInfoCache;
import xyz.nyroma.listeners.MainListeners;

import javax.security.auth.login.LoginException;
import java.io.File;

public class Main {
    private static JDA bot;

    public static void main(String[] args) {
        JDABuilder builder = JDABuilder.createDefault("NTQ4NTA3OTYyMzc4MDI3MDM5.Xrb18g.IV3aGYW8cOjBvqlfC_Xb29PyrYs");
        builder.setActivity(Activity.watching("https://discord.gg/9VfbD6d"));
        builder.addEventListeners(new MainListeners());
        try {
            bot = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        System.out.println("Yoooooooooo je suis co !");
        ServerInfoCache.setup(new File("data/"));
    }


}
