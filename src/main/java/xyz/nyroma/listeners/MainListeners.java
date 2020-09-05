package xyz.nyroma.listeners;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import xyz.nyroma.banks.Bank;
import xyz.nyroma.banks.BankCache;
import xyz.nyroma.banks.TransactionType;
import xyz.nyroma.caches.ServerInfoCache;
import xyz.nyroma.entities.Compteur;
import xyz.nyroma.entities.Joke;
import xyz.nyroma.entities.ServerInfo;
import xyz.nyroma.main.MainUtils;

import java.io.File;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainListeners extends ListenerAdapter {

    private Guild netServer;
    private int nbMess = 0;
    private List<Guild> servers = new ArrayList<>();

    @Override
    public void onGuildUpdateName(GuildUpdateNameEvent e) {
        Guild server = e.getGuild();
        if (ServerInfoCache.get(server.getIdLong()).isPresent()) {
            ServerInfo info = ServerInfoCache.get(server.getIdLong()).get();
            info.setServerName(server.getName());
        }
    }

    @Override
    public void onShutdown(ShutdownEvent e) {
        ServerInfoCache.serialize();
        System.exit(0);
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        if (!e.getAuthor().isBot()) {
            Message mess = e.getMessage();
            String txt = mess.getContentRaw();
            Guild server = e.getGuild();
            MessageChannel channel = mess.getChannel();
            Member member = mess.getMember();
            Bank bank = BankCache.get(member.getUser().getId()).isPresent() ?
                    BankCache.get(member.getUser().getId()).get() :
                    new Bank(member.getUser().getId());

            float xp = 0;
            switch(new Random().nextInt(4)){
                case 0:
                    xp = 0.001f;
                    break;
                case 1:
                    xp = 0.005f;
                    break;
                case 2:
                    xp = 0.0075f;
                    break;
                case 3:
                    xp = 0.01f;
                    break;
            }
            bank.add(xp, TransactionType.STATE_ADD);

            String[] args = txt.split(" ");

            if (!servers.contains(server)) {
                launchThreads(server);
                servers.add(server);
            }

            if (txt.equals("test ici") && member.getId().equals(MainUtils.idMay)) {
                if (MainUtils.getChannelByName(server, "cantine").isPresent()) {
                    MainUtils.sendMess(channel, "Salon cantine détecté.");
                    GuildChannel channel2 = MainUtils.getChannelByName(server, "cantine").get();
                    if (channel2.getPermissionOverride(server.getRoleById(server.getId())) != null) {
                        channel2.getPermissionOverride(server.getRoleById(server.getId())).delete().queue();
                    }
                    channel2.createPermissionOverride(server.getRoleById(server.getId())).setDeny(Permission.MESSAGE_WRITE).queue();
                    MainUtils.sendMess(channel, "Permission normalement changée.");
                }
            }

            ServerInfo serverInfo = ServerInfoCache.get(server.getIdLong()).isPresent() ? ServerInfoCache.get(server.getIdLong()).get() : new ServerInfo(server);

            if (server.getId().equals("400414883843145748")) {
                this.netServer = server;
            }

            if (txt.contains("@someone") && MainUtils.isStaff(e.getMember())) {
                List<Member> members = server.getMembers();
                MainUtils.sendMess(channel, "<@" +
                        members.get(new Random().nextInt(members.size())).getUser().getId() + ">"
                );
            }

            List<User> mentionned = mess.getMentionedUsers();
            if (mentionned.contains(server.getJDA().getSelfUser())) {
                mess.addReaction(":ban:603347805334929408").queue();
            }

            if (txt.length() > 3 && txt.substring(0, 3).equals("ur/")) {
                String cmd = txt.split(" ")[0].substring(3);
                if (checkCmd(mess, cmd, args, serverInfo)) {
                    mess.addReaction("✅").queueAfter(1, TimeUnit.SECONDS);
                } else {
                    mess.addReaction("❌").queueAfter(1, TimeUnit.SECONDS);
                }
            }

            if (args[0].equalsIgnoreCase("k") && args.length == 1) {
                MainUtils.sendMess(channel, "k");
            }

            for (String mot : txt.split(" ")) {
                if (mot.equalsIgnoreCase("coucou") || mot.equalsIgnoreCase("salut")) {
                    greetings(mess.getChannel());
                }
            }
            if (txt.contains("ça va") && mess.getMentionedUsers().contains(mess.getGuild().getJDA().getSelfUser())) {
                alright(mess.getChannel());
            }
            if (!e.getAuthor().equals(e.getJDA().getSelfUser())) {
                int i = args.length - 1;
                if (args[i].equals("quoi") || args[i].equals("koi") || args[i].equals("qoi") || args[i].equals("kwa")) {
                    MainUtils.sendMess(channel, "feur");
                } else if (txt.equalsIgnoreCase("carré") || args[i].equalsIgnoreCase("carré")) {
                    MainUtils.sendMess(channel, "Rectangle.");
                    MainUtils.sendMess(channel, "batard.");
                    MainUtils.addMessEmote(mess, ":haaan:575457625324519475");
                }
            }

            if (mess.getMember() != null && serverInfo.getBounds().contains(mess.getMember().getIdLong())) {
                serverInfo.annoyBound(mess);
            }

            if (mentionned.contains(server.getJDA().getSelfUser()) && MainUtils.isStaff(mess.getMember())) {
                if (txt.contains("occupe toi de ")) {
                    for (User mention : mentionned) {
                        if (!mention.isBot()) {
                            mention.openPrivateChannel().queue(
                                    (chan) -> chan.sendMessage(new MessageBuilder().append(" ").build()).addFile(new File("data/omfg.png")).queue()
                            );
                            MainUtils.sendPrivMess(mess.getGuild(), mention.getId(),
                                    "Не бойтесь снова нарушать правила, и я заставляю вас есть их, " +
                                            "не используя рот, прежде чем разорвать голову на части зубами. твои зубы.");
                            MainUtils.sendMess(mess.getChannel(), ":)");
                        }
                    }
                }
            }
            for (String w : serverInfo.getBannedWords()) {
                Pattern pattern = Pattern.compile("\\b" + w + "\\b", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(txt);

                if (matcher.matches() && !MainUtils.isStaff(e.getMember())) {
                    punish(mess, w);
                }
            }
            log(mess);

            if (txt.contains("?") && mentionned.contains(server.getJDA().getSelfUser())) {
                switch (new Random().nextInt(4)) {
                    case 0:
                        MainUtils.sendMess(channel, "ouais");
                        break;
                    case 1:
                        MainUtils.sendMess(channel, "Non.");
                        break;
                    case 2:
                        mess.delete().queue();
                        MainUtils.sendMess(channel, "mh ?");
                        break;
                    case 3:
                        MainUtils.sendMess(channel, "peut-être");
                }
            }
        }
    }

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
        User u = e.getAuthor();
        Message mess = e.getMessage();
        String txt = mess.getContentRaw();
        String[] args = txt.split(" ");
        if (!u.equals(e.getJDA().getSelfUser())) {
            Date date = new Date(6);
            StringBuilder dateSB = new StringBuilder();
            dateSB
                    .append(date.getDay()).append("/").append(date.getMonth()).append("/").append(date.getYear()).append(", ")
                    .append(date.getHours()).append(":").append(date.getMinutes()).append(":").append(date.getSeconds());
            String message =
                    u.getName() + "#" + u.getDiscriminator() + " (<@" + u.getId() + ">) \n" +
                            "Date : " + dateSB.toString() + "\n" +
                            "Message : " + mess.getContentRaw();
            TextChannel channel = MainUtils.getChannelByName(this.netServer, "mps").isPresent() ? MainUtils.getChannelByName(this.netServer, "mps").get() : this.netServer.createTextChannel("mps").complete();
            channel.getManager().setTopic("Ici seront répertoriés les mps envoyés au bot.").queue();
            MainUtils.sendMess(channel, ">>> " + message);

            if (args[0].equalsIgnoreCase("k") && args.length == 1) {
                MainUtils.sendMess(channel, "k");
            }

            for (String mot : txt.split(" ")) {
                if (mot.equalsIgnoreCase("coucou") || mot.equalsIgnoreCase("salut")) {
                    greetings(mess.getChannel());
                }
            }
        }
    }

    private void punish(Message mess, String c) {
        Guild server = mess.getGuild();
        User u = mess.getAuthor();

        MainUtils.sendPrivMess(mess, u.getId(), "Hey " + u.getName() + " ! Restes poli s'il te plaît ! " + c + " est censuré.");
        mess.delete().queue();
        TextChannel channel = MainUtils.getChannelByName(server, "infractions_auto").isPresent()
                ? MainUtils.getChannelByName(server, "infractions_auto").get() : server.createTextChannel("infraction_auto").complete();
        channel.getManager().setTopic("Ici seront répertoriées toutes les infractions détectées par le bot.").queue();
        MainUtils.sendMess(channel,
                ">>> **Auteur : **" + mess.getAuthor().getName() + "\n" +
                        "**Date : **" + mess.getTimeCreated().toString() + "\n" +
                        "**Channel : **#" + mess.getChannel().getName() + "\n" +
                        "**Contenu : **" + mess.getContentDisplay() + "\n" +
                        "**Censuré : **" + c
        );
    }

    private void log(Message mess) {
        TextChannel channel = MainUtils.getChannelByName(mess.getGuild(), "messages").isPresent()
                ? MainUtils.getChannelByName(mess.getGuild(), "messages").get() : mess.getGuild().createTextChannel("messages").complete();
        channel.getManager().setTopic("Ici seront répertoriés tous les messages envoyés sur le serveur.").queue();
        OffsetDateTime time = mess.getTimeCreated();
        StringBuilder sb = new StringBuilder();
        sb.append(">>> **Message n°").append(this.nbMess).append("\n")
                .append("Auteur : **").append(mess.getAuthor().getName()).append("#").append(mess.getAuthor().getDiscriminator()).append("\n")
                .append("**Date : **").append(time.getDayOfMonth()).append("/").append(time.getMonthValue()).append("/").append(time.getYear())
                .append(", ").append(time.getHour() + 2).append("h").append(time.getMinute()).append("min").append(time.getSecond()).append("s").append("\n")
                .append("**Channel : **#").append(mess.getChannel().getName()).append("\n")
                .append("**Contenu : ** \n*").append(mess.getContentDisplay()).append("*");
        MainUtils.sendMess(channel, sb.toString());
        this.nbMess++;
    }

    private boolean checkCmd(Message mess, String cmd, String[] args, ServerInfo serverInfo) {
        Member member = mess.getMember();
        MessageChannel channel = mess.getChannel();
        Guild server = mess.getGuild();
        System.out.println(cmd);

        if (args.length > 1) {
            if (cmd.equals("bound") && MainUtils.isStaff(member)) {
                if (mess.getMentionedMembers().size() > 0) {
                    for (Member m : mess.getMentionedMembers()) {
                        serverInfo.addBound(m.getIdLong());
                        MainUtils.sendMess(channel, m.getUser().getName() + " a été bound :)");
                        MainUtils.sendPrivMess(mess, m.getId(), "LMAO");
                        MainUtils.sendPrivMess(mess, m.getId(), "C MAREN");
                    }
                    return true;
                } else {
                    if (args[1].equals("list")) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("```Liste des bounds :\n");
                        for (long id : serverInfo.getBounds()) {
                            if (server.getMemberById(id) != null) {
                                Member m = server.getMemberById(id);
                                sb.append("- ").append(m.getUser().getName()).append("#").append(m.getUser().getDiscriminator()).append("\n");
                            }
                        }
                        sb.append("```");
                        MainUtils.sendMess(channel, sb.toString());
                        return true;
                    } else
                        MainUtils.sendMess(channel, "[:x:] Arguments invalides ! Syntaxe : `ur/bound <membre(s)>`");
                    return false;
                }
            } else if (cmd.equals("unbound") && MainUtils.isStaff(member)) {
                if (mess.getMentionedMembers().size() > 0) {
                    for (Member m : mess.getMentionedMembers()) {
                        serverInfo.removeBound(m.getIdLong());
                        MainUtils.sendMess(channel, m.getUser().getName() + " a été unbound :(");
                    }
                    return true;
                } else {
                    MainUtils.sendMess(channel, "[:x:] Arguments invalides ! Syntaxe : `ur/unbound <membre(s)>`");
                    return false;
                }
            } else if (cmd.equals("joke")) {
                switch (args[1]) {
                    case "add":
                        try {
                            String title = args[2];
                            StringBuilder sb = new StringBuilder();
                            for (int i = 3; i < args.length; i++) {
                                sb.append(args[i]).append(" ");
                            }
                            new Joke(title, sb.toString());
                            return true;
                        } catch (ArrayIndexOutOfBoundsException ee) {
                            MainUtils.sendMess(channel, "[:x:] Arguments invalides ! Syntaxe : `ur/joke add <title> <blague>`");
                            return false;
                        }
                    case "remove":
                        if (MainUtils.isStaff(member)) {
                            try {
                                String title = args[2];
                                if (serverInfo.getJoke(title).isPresent()) {
                                    if (serverInfo.removeJoke(serverInfo.getJoke(title).get())) {
                                        MainUtils.sendMess(channel, "La blague \"" + title + "\" a été supprimée.");
                                        return true;
                                    } else {
                                        MainUtils.sendMess(channel, "La blague \"" + title + "\" n'a pas pu être supprimée.");
                                        return false;
                                    }
                                } else {
                                    MainUtils.sendMess(channel, "Il n'y a pas de blague avec ce titre !");
                                    return false;
                                }
                            } catch (ArrayIndexOutOfBoundsException ee) {
                                MainUtils.sendMess(channel, "[:x:] Arguments invalides ! Syntaxe : `ur/joke <remove> <title>`");
                                return false;
                            }
                        } else {
                            MainUtils.sendMess(channel, "Vous n'avez pas les permissions nécessaires.");
                            return false;
                        }
                    default:
                        return false;
                }
            } else if (cmd.equals("resetnames") && MainUtils.isStaff(member)) {
                Thread th = new Thread(() -> {
                    for (Member m : server.getMembers()) {
                        if (!m.isOwner()) {
                            m.modifyNickname(m.getUser().getName()).queue();
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ignored) {
                        }
                    }
                });
                th.setDaemon(true);
                th.start();
                return true;
            } else if (cmd.equals("delete") && MainUtils.isStaff(member)) {
                try {
                    List<Message> messages = channel.getHistory().retrievePast(Integer.parseInt(args[1])).complete();
                    Thread delete = new Thread(() -> {
                        for (Message message : messages) {
                            message.delete().queue();
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException ignored) {
                            }
                        }
                    });
                    delete.setDaemon(true);
                    delete.start();
                    return true;
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException ee) {
                    MainUtils.sendMess(channel, "[:x:] Arguments invalides ! Syntaxe : `un/delete <nombre>`");
                    return false;
                }
            } else if (cmd.equals("mpall") && member.isOwner()) {
                StringBuilder message = new StringBuilder();
                User ua = mess.getAuthor();
                for (int i = 1; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }

                Thread send = new Thread(() -> {
                    for (Member m : server.getMembers()) {
                        User user = m.getUser();
                        if (!user.isBot() && user != server.getJDA().getSelfUser()) {
                            MainUtils.sendPrivMess(server, user.getId(), "[Par " + ua.getName() + "#" + ua.getDiscriminator() +
                                    ", du serveur **" + server.getName() + "**] : \n" + message.toString());
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ignored) {
                        }
                    }
                });
                send.setDaemon(true);
                send.start();
                return true;
            } else if (cmd.equals("renameall") && member.isOwner()) {
                Thread rename = new Thread(() -> {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    String newNick = sb.toString();
                    List<Member> listUser = server.getMembers();
                    for (Member m : listUser) {
                        if (!m.isOwner()) {
                            m.modifyNickname(newNick).queue();
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ignored) {
                            }
                        }
                    }
                    MainUtils.sendMess(channel, "Tout le monde a été renommé en ***" + newNick + "*** !");
                });
                rename.setDaemon(true);
                rename.start();
                return true;
            }
            else if (cmd.equals("bl") && MainUtils.isStaff(member)) {
                if (args.length > 2) {
                    switch (args[1]) {
                        case "add":
                            serverInfo.addBannedWord(args[2]);
                            MainUtils.sendPrivMess(mess, member.getUser().getId(), ":white_check_mark: \"" + args[2] + "\" a été ajouté à la blacklist.");
                            return true;
                        case "remove":
                            serverInfo.removeBannedWord(args[2]);
                            MainUtils.sendPrivMess(mess, member.getUser().getId(), ":white_check_mark: \"" + args[2] + "\" a été retiré à la blacklist.");
                            return true;
                        default:
                            MainUtils.sendMess(channel, "Arguments invalides ! Syntaxe : `ur/bl <add:remove> <mot>`");
                            return false;
                    }
                } else {
                    if (args[1].equals("list")) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(":shield: Mots censurés : :shield:").append("\n").append("\n");

                        for (String w : serverInfo.getBannedWords()) {
                            sb.append(":arrow_right: *").append(w).append("*").append("\n");
                        }
                        MainUtils.sendPrivMess(mess, member.getUser().getId(), sb.toString() + "\n" + ":warning: Ne les utilisez pas !");
                        return true;
                    } else {
                        MainUtils.sendMess(channel, "[:x:] Arguments invalides. Syntaxe : `ur/bl <add:remove:list>");
                        return false;
                    }
                }
            }
            else if(cmd.equals("bank")){
                List<Member> mentionned = mess.getMentionedMembers();
                if(args[1].equals("list")){
                    StringBuilder sb = new StringBuilder();
                    sb.append(">>> :money_with_wings: **Banks :**").append("\n").append("\n");
                    System.out.println(BankCache.getBanks().size());
                    for (Bank bank : BankCache.getBanks()) {
                        System.out.println(bank.getPlayer());
                        if (server.getMemberById(bank.getPlayer()) != null) {
                            Member m = server.getMemberById(bank.getPlayer());
                            sb.append(":small_blue_diamond: **").append(m.getUser().getName()).append("#").append(m.getUser().getDiscriminator()).append(" (")
                                    .append(bank.getPlayer()).append(")** : ").append(bank.getAmount()).append(" Nyr \n");
                        } else {
                            sb.append(":small_blue_diamond: **").append("fdp").append("#").append("0000").append(" (")
                                    .append(bank.getPlayer()).append(")** : ").append(bank.getAmount()).append(" Nyr \n");
                        }
                    }
                    MainUtils.sendMess(channel, sb.toString());
                    return true;
                } else if(args[1].equals("get")){
                    if(mentionned.size() == 1){
                        User u = mentionned.get(0).getUser();
                        Bank bank = BankCache.get(u.getId()).isPresent() ? BankCache.get(u.getId()).get() : new Bank(u.getId());
                        MainUtils.sendMess(channel, ">>> **__Banque :__** \n" +
                                ":key: **Propriétaire :** " + u.getName() + "#" + u.getDiscriminator() + " (" + bank.getPlayer() + ") \n" +
                                ":moneybag: **Montant :** " + bank.getAmount() + " Nyr\n" +
                                ":chart_with_upwards_trend: **Nombre de transactions :** " + bank.getTransactions().size());
                    } else {
                        MainUtils.sendMess(channel, "Il ne faut mentionner qu'une seule personne !");
                    }
                    return true;
                } else if(args[1].equals("add") && member.isOwner()){
                    if(mentionned.size() == 1){
                        if(args.length == 4){
                            User u = mentionned.get(0).getUser();
                            Bank bank = BankCache.get(u.getId()).isPresent() ? BankCache.get(u.getId()).get() : new Bank(u.getId());
                            float amount = Float.parseFloat(args[3]);
                            bank.add(amount, TransactionType.STATE_ADD);
                            MainUtils.sendMess(channel, amount + " Nyr ont été virés vers le compte de " + u.getName() + ".");
                        } else {
                            MainUtils.sendMess(channel, "**Syntaxe :** `ur/bank add <mention> <montant>`");
                        }
                    } else {
                        MainUtils.sendMess(channel, "Il ne faut mentionner qu'une seule personne !");
                    }
                    return true;
                } else if(args[1].equals("send")){
                    if(mentionned.size() == 1){
                        if(args.length == 4){
                            User u = mentionned.get(0).getUser();
                            Bank receiverBank = BankCache.get(u.getId()).isPresent() ? BankCache.get(u.getId()).get() : new Bank(u.getId());
                            Bank senderBank = BankCache.get(member.getUser().getId()).isPresent() ? BankCache.get(member.getUser().getId()).get() : new Bank(member.getUser().getId());
                            float amount = Float.parseFloat(args[3]);
                            if(senderBank.getAmount() >= amount){
                                receiverBank.add(amount, TransactionType.PLAYER_ADD);
                                senderBank.remove(amount, TransactionType.PLAYER_REMOVE);
                                MainUtils.sendMess(channel, amount + " Nyr ont été débités de votre compte.");
                                MainUtils.sendMess(channel, amount + " Nyr ont été virés vers le compte de " + u.getName() + ".");
                            } else {
                                MainUtils.sendMess(channel, "Vous n'avez pas assez d'argent dans votre banque.");
                            }
                        } else {
                            MainUtils.sendMess(channel, "**Syntaxe :** `ur/bank send <mention> <montant>`");
                        }
                    } else {
                        MainUtils.sendMess(channel, "Il ne faut mentionner qu'une seule personne !");
                    }
                    return true;
                } else if(args[1].equals("remove") && member.isOwner()){
                    if(mentionned.size() == 1){
                        if(args.length == 4){
                            User u = mentionned.get(0).getUser();
                            Bank receiverBank = BankCache.get(u.getId()).isPresent() ? BankCache.get(u.getId()).get() : new Bank(u.getId());
                            float amount = Float.parseFloat(args[3]);
                            if(receiverBank.getAmount() >= amount){
                                receiverBank.remove(amount, TransactionType.STATE_REMOVE);
                                MainUtils.sendMess(channel, amount + " Nyr ont été débités du compte de " + u.getName() + ".");
                            } else {
                                MainUtils.sendMess(channel, "Vous n'avez pas assez d'argent dans votre banque.");
                            }
                        } else {
                            MainUtils.sendMess(channel, "**Syntaxe :** `ur/bank remove <mention> <montant>`");
                        }
                    } else {
                        MainUtils.sendMess(channel, "Il ne faut mentionner qu'une seule personne !");
                    }
                    return true;
                } else {
                    MainUtils.sendMess(channel, "**Syntaxe :** `ur/bank <list:get:add:remove:send>`");
                    return false;
                }
            }
            else {
                return false;
            }
        } else if (args.length == 1) {
            if (cmd.equals("joke")) {
                Joke j = serverInfo.getJokes().get(new Random().nextInt(serverInfo.getJokes().size() - 1));
                MainUtils.sendMess(channel, ":clown: __Titre :__" + j.getTitle());
                MainUtils.sendMess(channel, j.getJoke());
                return true;
            }
            else if (cmd.equals("compteur")) {
                Thread thread = new Thread(new Compteur(channel));
                thread.setDaemon(true);
                thread.start();
                return true;
            }
            else if (cmd.equals("shutdown") && mess.getAuthor().getId().equals("301715312603168769")) {
                MainUtils.sendMess(channel, "Oof");
                server.getJDA().shutdown();
                return true;
            }
            else if (cmd.equals("help")) {
                MainUtils.sendMess(channel,
                        ":rocket: signifie que la commande est réservée à May#8071. \n" +
                                ":closed_lock_with_key: signifie que la commande est réservée au propriétaire du serveur. \n" +
                                ":shield: signifie que la commande est réservée au staff du serveur. \n" +
                                ":ok_hand: signifie que la commande est disponible pour tous les membres. \n" +
                                "\n" +
                                ":rocket: `ur/shutdown` \n" +
                                ":closed_lock_with_key: `ur/bl <add:remove:list> \n`" +
                                ":ok_hand: `ur/help` \n" +
                                ":shield: `ur/bound <membre(s)>` \n" +
                                ":shield: `ur/unbound <membre(s)>` \n" +
                                ":shield: `ur/joke remove <titre>` \n" +
                                ":ok_hand: `ur/joke add <titre> <blague>` \n" +
                                ":closed_lock_with_key: `ur/renameall <pseudo>` \n" +
                                ":closed_lock_with_key: `ur/mpall <membre(s)>` \n" +
                                ":shield: `ur/resetnames` \n" +
                                ":shield: `ur/delete <nombre>` \n"
                );
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void greetings(MessageChannel channel) {
        Random r = new Random();
        int i = r.nextInt(4);
        switch (i) {
            case 1:
                MainUtils.sendMess(channel, "Yooo");
                break;
            case 2:
                MainUtils.sendMess(channel, "Salut !");
                break;
            case 3:
                MainUtils.sendMess(channel, "Coucouuuu");
                break;
            case 4:
                MainUtils.sendMess(channel, "Wsh wsh canne à pêche");
                break;
        }
    }

    private void alright(MessageChannel channel) {
        Random r = new Random();
        int i = r.nextInt(3);
        switch (i) {
            case 1:
                MainUtils.sendMess(channel, "Super et toi ?");
                break;
            case 2:
                MainUtils.sendMess(channel, "Bien, comme toujours :D et toi ?");
                break;
            case 3:
                MainUtils.sendMess(channel, "Trkl et toi ?");
                break;
        }
    }

    private static void launchThreads(Guild server) {
        Thread cantineThread = new Thread(() -> {
            while (true) {
                if (MainUtils.getChannelByName(server, "cantine").isPresent() && server.getRoleById(server.getId()) != null) {
                    GuildChannel channel = MainUtils.getChannelByName(server, "cantine").get();
                    Role everyoneRole = server.getRoleById(server.getId());
                    if (MainListeners.cantineOpening()) {
                        if (channel.getPermissionOverride(everyoneRole) != null) {
                            channel.getPermissionOverride(everyoneRole).delete().queue();
                        }
                        channel.createPermissionOverride(everyoneRole).setAllow(Permission.MESSAGE_WRITE).queue();
                        MainUtils.sendMess(MainUtils.getChannelByName(server, "cantine").get(), "La cantine est maintenant ouverte ! :)");
                    }
                    if (MainListeners.cantineClosing()) {
                        if (channel.getPermissionOverride(everyoneRole) != null) {
                            channel.getPermissionOverride(everyoneRole).delete().queue();
                        }
                        channel.createPermissionOverride(everyoneRole).setDeny(Permission.MESSAGE_WRITE).queue();
                        MainUtils.sendMess(MainUtils.getChannelByName(server, "cantine").get(), "La cantine est maintenant fermée ! :(");
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        cantineThread.setDaemon(true);
        cantineThread.start();

        System.out.println("Thread pour le serveur " + server.getName() + " lancé.");
    }

    private static boolean cantineOpening() {
        return MainUtils.getTime().equals("11:00:00") || MainUtils.getTime().equals("19:00:00");
    }

    private static boolean cantineClosing() {
        return MainUtils.getTime().equals("14:00:00") || MainUtils.getTime().equals("22:00:00");
    }

    private static boolean everyMinute() {
        return MainUtils.getTime().split(":")[2].equals("00");
    }

    private static boolean everyHour() {
        return MainUtils.getTime().split(":")[1].equals("00") && MainUtils.getTime().split(":")[2].equals("00");
    }
}
