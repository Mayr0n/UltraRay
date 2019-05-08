import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;

public class main {

    public static void main(String[] args){
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(""); //je garde le token privé évidemment x)
        builder.setGame(Game.playing("manger du chocolat"));
        builder.addEventListener(new listeners());
        try {
            builder.build();
        } catch(LoginException e){
            e.printStackTrace();
        }
    }
}
