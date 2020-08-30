package xyz.nyroma.entities;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import xyz.nyroma.caches.ServerInfoCache;
import xyz.nyroma.main.MainUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class ServerInfo {
    private long serverID;
    private String serverName;
    private List<String> bannedWords = new ArrayList<>();
    private List<Joke> jokes = new ArrayList<>();
    private List<Long> bounds = new ArrayList<>(); //ID des membres bounds

    public ServerInfo(Guild server){
        this.serverID = server.getIdLong();
        this.serverName = server.getName();
        ServerInfoCache.add(this);
    }

    public void addBannedWord(String word){
        this.bannedWords.add(word);
    }

    public boolean removeBannedWord(String word){
        return this.bannedWords.remove(word);
    }

    public List<String> getBannedWords() {
        return this.bannedWords;
    }

    public void addJoke(Joke joke){
        this.jokes.add(joke);
    }

    public boolean removeJoke(Joke joke){
        return this.jokes.remove(joke);
    }

    public Optional<Joke> getJoke(String title){
        for(Joke joke : this.jokes){
            if(joke.getTitle().equals(title)){
                return Optional.of(joke);
            }
        }
        return Optional.empty();
    }

    public void addBound(long id){
        this.bounds.add(id);
    }

    public boolean removeBound(long id){
        return this.bounds.remove(id);
    }

    public String getServerName() {
        return serverName;
    }

    public List<Joke> getJokes() {
        return jokes;
    }

    public List<Long> getBounds() {
        return bounds;
    }

    public long getServerID() {
        return serverID;
    }

    public void setServerID(long serverID) {
        this.serverID = serverID;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void annoyBound(Message mess) {
        MessageChannel channel = mess.getChannel();
        switch (new Random().nextInt(10)+1) {
            case 1:
                MainUtils.sendMess(channel, "Mais carrément");
                break;
            case 2:
                MainUtils.sendMess(channel, "Chut");
                break;
            case 3:
                MainUtils.sendMess(channel, "je t'aime :3");
                break;
            case 4:
                MainUtils.sendMess(channel, "C'est chiant");
                break;
            case 5:
                MainUtils.sendMess(channel, "Mais tg");
                break;
            case 6:
                MainUtils.deleteMess(mess);
                MainUtils.sendMess(channel, "Oups ! Supprimé... C'était surement pas intéressant toute façon");
                break;
            case 7:
                MainUtils.sendMess(channel, "ok");
                break;
            case 8:
                MainUtils.sendMess(channel, "OMG JE VIENS DE GAGNEZ 1 MILLION D'EURO SUR BRAVOLOTO");
                break;
        }
    }

    public void spamBound(List<Member> mentionned, MessageChannel channel) {
        if (mentionned.size() == 1) {
            Thread spam = new Thread(() -> {
                for (int i = 0; i <= 20; i++) {
                    MainUtils.sendMess(channel, "<@" + mentionned.get(0).getUser().getId() + ">");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                MainUtils.sendMess(channel, ":)");
            });
            spam.setDaemon(true);
            spam.start();
        } else {
            MainUtils.sendMess(channel, "Il faut mentionner une seule personne ! Syntaxe : `ur/spam @pseudo`");
        }
    }
}
