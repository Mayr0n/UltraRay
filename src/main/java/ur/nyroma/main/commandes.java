package ur.nyroma.main;

import fr.may.processus.SentenceBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import ur.nyroma.features.CensorManager;
import ur.nyroma.features.JokesManager;
import ur.nyroma.jeux.mathsplay;
import ur.nyroma.jeux.pingpong;
import ur.nyroma.moderation.BlacklistManager;
import ur.nyroma.moderation.FeaturesManager;
import ur.nyroma.moderation.TrollingManager;
import ur.nyroma.moderation.ModerationManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ur.nyroma.main.speedy.*;

public class commandes {
    public static String prefix = "ur/";
    private static FeaturesManager fm = new FeaturesManager();
    private static TrollingManager tm = new TrollingManager();
    private static ModerationManager modm = new ModerationManager();

    public commandes(Message mess) {
        String txt = mess.getContentRaw();
        String cmd = txt.split(" ")[0].substring(3);
        String[] args = txt.split(" ");
        MessageChannel channel = mess.getChannel();
        Member member = mess.getMember();
        Guild server = mess.getGuild();
        boolean shutdown = false;
        boolean executed = false;
        List<String> normalCommands = Arrays.asList("help", "joke", "pp", "dab", "mathsPlay", "talk", "fbi", "compteur");
        List<String> staffCommands = Arrays.asList("resetnames", "bound", "unbound", "spam", "censure", "delete"); //intervertir renameAll et resetAllNames
        List<String> ownerCommands = Arrays.asList("renameall", "closeall", "openall", "sendMpAll");
        List<String> mayCommands = Arrays.asList("sendMp", "shutdown", "bl");
        List<Member> mentionned = mess.getMentionedMembers();

        for (int i = 0; i < normalCommands.size(); i++) {
            if (cmd.equals(normalCommands.get(i))) {
                switch (i) {
                    case 0:
                        if (member.isOwner()) {
                            sendMess(channel,
                                    ">>> **__Commandes Propriétaire__** :closed_lock_with_key:" + espace() +
                                            "**ur/renameall** *<mot(s)>*: permet de renommer tous les membres du serveur..." + espace() +
                                            "**ur/closeall** : permet de fermer tous les salons du serveur en cas de problèmes" + espace() +
                                            "**ur/openall** : counter la commande précédente, ATTENTION : toutes les permissions seront à refaire !" + espace() +
                                            "**ur/sendmpall** : permet d'envoyer un mp à tout le monde, en cas de problèmes" + espace() +
                                            "**ur/bl** *<add:remove>* : permet de modifier la blacklist de mots"
                            );
                        }
                        if (member.hasPermission(Permission.KICK_MEMBERS)) {
                            sendMess(channel, ">>> **__Commandes Admin__** :shield:" + espace() +
                                    "**ur/joke** *<add:remove>* : permet de modifier la liste des blagues qu'a le bot en réserve" + espace() +
                                    "**ur/resetnames** : Réinitialise les pseudos de tous les membres du serveur" + espace() +
                                    "**ur/bound** *<nom>* et **ur/unbound** : Permet de \"bound\" un membre, afin de l'embêter :)" + espace() +
                                    "**ur/spam** : Comme la précédente commande, permet d'embêter un membre en le spammant :)" + espace() +
                                    "**ur/censure** *<add:remove>* : permet de modifier les mots censurés par le bot" + espace() +
                                    "**ur/delete** <nombredemessages> : permet de supprimer x messages"
                            );
                        }
                        sendMess(channel, ">>> **__Commandes Générales__** :ok_hand:" + espace() +
                                "**ur/joke** permet à UltraRay de raconter une blague qu'il a en réserve" + espace() +
                                "**[WIP] ur/pp** *<info: new :stop>* : permet d'avoir des informations sur le jeu de \"pingpong\" d'Ultra Ray [WIP]" + espace() +
                                "**ur/dab** et **ur/fbi** : bon. Pas besoin de dire à quoi ils servent..." + espace() +
                                "**[WIP] ur/mathsplayinfo** : permet d'avoir des informations sur le jeu \"mathsplay\" d'Ultra Ray [WIP]" + espace() +
                                "**ur/compteur** : en cas de côte à combien, ça peut servir"
                        );
                        break;
                    case 1:
                        JokesManager jm = new JokesManager();
                        if (getSubCommand(txt).equals("")) {
                            jm.tell(channel);
                        } else if(member.hasPermission(Permission.KICK_MEMBERS)){
                            switch (getSubCommand(txt)) {
                                case "add":
                                    executed = jm.add(getArgs(txt, true));
                                    break;
                                case "remove":
                                    executed = jm.remove(getArgs(txt, true).get(0));
                                    break;
                                case "list":
                                    speedy.sendMess(channel,jm.getListJokes());
                                    executed = true;
                                    break;
                                default: speedy.sendMess(channel,
                                        ">>> **Erreur syntaxe** ! Syntaxe :" + espace() +
                                                ">>> **ur/joke add** *<titre> <blague>*" + espace() +
                                                ">>> **ur/joke remove** *<titre>*"
                                        );
                            }
                        }
                        break;
                    case 2:
                        new pingpong().pingpong(mess); //à refaire
                        break;
                    case 3:
                        sendMess(channel, "https://tenor.com/view/dab-dab-problem-dab-dance-gif-5412848");
                        break;
                    case 4:
                        new mathsplay().mathsplay(member, mess); //à refaire
                        break;
                    case 5:
                        if (channel.getId().equalsIgnoreCase("632900693086437427")) {
                            speedy.sendMess(channel, new SentenceBuilder().build());
                        } else {
                            speedy.sendMess(channel, "Mauvais salon !");
                        }
                        break;
                    case 6:
                        speedy.sendMess(channel, "https://tenor.com/view/fbi-raid-swat-gif-11500735");
                        break;
                    case 7:
                        Thread t = new Thread(fm.compteur(channel));
                        t.setDaemon(true);
                        t.start();
                }
            }
        }
        for (int i = 0; i < staffCommands.size(); i++) {
            if (cmd.equals(staffCommands.get(i)) && member.hasPermission(Permission.KICK_MEMBERS)) {
                switch (i) {
                    case 0:
                        fm.resetAllNames(server);
                        break;
                    case 1:
                        executed = tm.bound(mentionned, member.getUser().getId(), server);
                        testExecute(executed, channel);
                        break;
                    case 2:
                        if(mentionned.size() != 0){
                            for(Member m : mentionned){
                                tm.unbound(server, m.getUser().getId());
                                speedy.sendMess(channel, m.getEffectiveName() + " a été unbound.");
                            }
                        }
                        break;
                    case 3:
                        tm.spam(mentionned, channel);
                        break;
                    case 4:
                        CensorManager cm = new CensorManager(server);
                        switch(getSubCommand(txt)){
                            case "add":
                                executed = cm.add(getArgs(txt, true).get(0));
                                break;
                            case "remove":
                                executed = cm.remove(getArgs(txt, true).get(0));
                                break;
                            case "list":
                                speedy.sendMess(channel, cm.getList());
                                executed = true;
                                break;
                            default: speedy.sendMess(channel,
                                    ">>> **Erreur syntaxe** ! Syntaxe :" + espace() +
                                            ">>> **ur/censor add** *<mot>*" + espace() +
                                            ">>> **ur/censor remove** *<mot>*"
                            );
                        }
                        testExecute(executed, channel);
                        break;
                    case 5:
                        modm.delete(channel, Integer.parseInt(args[1]));
                        break;
                }
            }
        }
        for (int i = 0; i < ownerCommands.size(); i++) {
            if (cmd.equals(ownerCommands.get(i)) && member.isOwner()) {
                switch (i) {
                    case 0:
                        fm.renameAll(mess);
                        break;
                    case 1:
                        modm.closeChannels(server);
                        break;
                    case 2:
                        modm.openChannels(server);
                        break;
                    case 3:
                        modm.sendMPall(mess);
                        break;
                }
                sendPrivMess(mess, member.getUser().getId(), "Terminé !");
            }
        }
        for (int i = 0; i < mayCommands.size(); i++) {
            if (cmd.equals(mayCommands.get(i)) && member.getUser().getId().equals(idMay)) {
                switch (i) {
                    case 0:
                        new FeaturesManager().sendMP(mess);
                        break;
                    case 1:
                        speedy.sendMess(channel, "Bip boup.. boup.... bip..........");
                        server.getJDA().shutdown();
                        shutdown = true;
                        break;
                    case 2:
                        String[] w = args;
                        if (w.length == 3) {
                            switch (w[1]) {
                                case "add":
                                    new BlacklistManager().addWord(w[2]);
                                    speedy.sendMess(channel, "Le mot `" + w[2] + "` a été ajouté à la blacklist.");
                                    break;
                                case "remove":
                                    new BlacklistManager().removeWord(w[2]);
                                    speedy.sendMess(channel, "Le mot `" + w[2] + "` a été retiré de la blacklist.");
                                    break;
                            }
                        }
                }
            }
        }
        speedy.deleteMess(mess);
        if (shutdown) {
            System.exit(0);
        }
    }

    private void testExecute(boolean executed, MessageChannel channel){
        if(executed){
            speedy.sendMess(channel, "[:white_check_mark:] Commande exécutée !");
        } else {
            speedy.sendMess(channel, "[:x:] L'éxecution de la commande a échoué.");
        }
    }

    private String getSubCommand(String txt) {
        String[] words = txt.split(" ");
        if (words.length > 1) {
            return txt.split(" ")[1];
        } else {
            return "";
        }
    }

    private List<String> getArgs(String txt, boolean hasSub) {
        String[] words = txt.split(" ");
        List<String> args = new ArrayList<>();
        int max = 1;
        if (hasSub) max = 2;
        if (words.length > max) {
            for (int i = max; i < words.length; i++) {
                args.add(words[i]);
            }
        } else {
            args.add("");
        }
        return args;
    }
}
