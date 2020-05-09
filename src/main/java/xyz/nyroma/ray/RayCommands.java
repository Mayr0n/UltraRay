package xyz.nyroma.ray;

import net.dv8tion.jda.api.entities.*;
import xyz.nyroma.main.SpeedyException;
import xyz.nyroma.main.speedy;

import java.util.Arrays;
import java.util.List;

public class RayCommands {
    public final static String prefix = "ur/";
    private BlackCache blc = new BlackCache();
    private List<String> commands = Arrays.asList("bl", "shutdown","help");

    public RayCommands(Message mess, String cmd){
        Guild server = mess.getGuild();
        MessageChannel channel = mess.getChannel();
        Member member = mess.getMember();
        String txt = mess.getContentRaw();
        String[] args = txt.split(" ");

        try {
            if(cmd.equals(commands.get(0)) && speedy.isStaff(member)){
                    if(args.length > 1){
                        BlacklistManager blm;
                        try {
                            blm = blc.get(mess.getGuild());
                        } catch (BlacklistException e) {
                            blm = new BlacklistManager(mess.getGuild().getId());
                        }
                        if(args.length > 2){
                            switch(args[1]){
                                case "add":
                                    if(blm.addWord(args[2])){
                                        speedy.sendMess(mess.getChannel(), ":white_check_mark: \""+ args[2] + "\" a été ajouté à la blacklist.");
                                    } else {
                                        speedy.sendMess(mess.getChannel(), ":x: \""+ args[2] + "\" n'a pas pu être ajouté à la blacklist.");
                                    }
                                    break;
                                case "remove":
                                    if(blm.removeWord(args[2])){
                                        speedy.sendMess(mess.getChannel(), ":white_check_mark: \""+ args[2] + "\" a été retiré de la blacklist.");
                                    } else {
                                        speedy.sendMess(mess.getChannel(), ":x: \""+ args[2] + "\" n'a pas pu être retiré de la blacklist.");
                                    }
                                    break;
                                default:
                                    speedy.sendMess(mess.getChannel(), "[:x:] Arguments invalides. Syntaxe : `ur/bl <add:remove> <mot>");
                            }
                        } else {
                            if(args[1].equals("list")){
                                speedy.sendMess(mess.getChannel(), ":shield: Mots censurés : :shield:");
                                for(String w : blm.getWords()){
                                    speedy.sendMess(mess.getChannel(), ":arrow_right: *" + w + "*");
                                }
                                speedy.sendMess(mess.getChannel(), ":warning: Ne les utilisez pas !");
                            } else {
                                speedy.sendMess(mess.getChannel(), "[:x:] Arguments invalides. Syntaxe : `ur/bl <add:remove> <mot>");
                            }
                        }
                    } else {
                        speedy.sendMess(mess.getChannel(), "[:x:] Arguments invalides. Syntaxe : `ur/bl <add:remove:list>");
                    }
            }
            else if(cmd.equals(commands.get(1)) && mess.getAuthor().getId().equals("301715312603168769")){
                server.getJDA().shutdown();
            }
            else if(cmd.equals(commands.get(2))){
                speedy.sendMess(channel,
                        ":rocket: signifie que la commande est réservée à May#8071. \n" +
                                ":closed_lock_with_key: signifie que la commande est réservée au propriétaire du serveur. \n" +
                                ":shield: signifie que la commande est réservée au staff du serveur. \n" +
                                ":ok_hand: signifie que la commande est disponible pour tous les membres. \n" +
                                "\n" +
                                ">>> **__Module Ray__** : \n" +
                                ":rocket: `ur/shutdown` \n" +
                                ":closed_lock_with_key: `ur/bl <add:remove:list> \n`" +
                                ":ok_hand: `ur/help` \n" +
                                "\n" +
                                "**__Module Gelp__** : \n" +
                                ":shield: `ug/bound <membre(s)>` \n" +
                                ":shield: `ug/unbound <membre(s)>` \n" +
                                ":shield: `ug/joke remove <titre>` \n" +
                                ":ok_hand: `ug/joke add <titre> <blague>` \n" +
                                "\n" +
                                "**__Module Neter__** : \n" +
                                ":closed_lock_with_key: `un/renameall <pseudo>` \n" +
                                ":closed_lock_with_key: `un/mpall <membre(s)>` \n" +
                                ":shield: `un/resetnames` \n" +
                                ":shield: `un/delete <nombre>` \n"
                );
            }
        } catch (SpeedyException ignored) {
        } catch(BlacklistException e){
            speedy.sendMess(channel, e.getMessage());
        }
    }
}
