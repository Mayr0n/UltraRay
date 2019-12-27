package ur.nyroma.main;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class main {
    private static JDA bot;

    public static void main(String[] args){
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken("");
        builder.setGame(Game.playing("https://discord.gg/9VfbD6d"));
        builder.addEventListener(new listeners());
        try {
            bot = builder.build();
        } catch(LoginException e){
            e.printStackTrace();
        }
        System.out.println("Yoooooooooo je suis co !");
    }
}
