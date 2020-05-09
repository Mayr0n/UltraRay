package xyz.nyroma.ray;

import java.util.ArrayList;

public class BlacklistManager {
    public String serverID; // /!\ ID et non getName
    public ArrayList<String> words = new ArrayList<>();

    public BlacklistManager(String serverID){
        this.serverID = serverID;
        new BlackCache().add(this, getServerID());
    }

    public boolean addWord(String word) throws BlacklistException {
        if(this.words.contains(word)){
            throw new BlacklistException("Ce mot est déjà blacklisté !");
        } else {
            return this.words.add(word);
        }
    }

    public boolean removeWord(String word){
        return this.words.remove(word);
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public String getServerID() {
        return serverID;
    }


}
