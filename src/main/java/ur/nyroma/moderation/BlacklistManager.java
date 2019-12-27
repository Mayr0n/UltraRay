package ur.nyroma.moderation;

import ur.nyroma.main.speedy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlacklistManager {

    private File dataFolder = new File("data/");
    private File blacklist = new File("data/blacklist.txt");

    public BlacklistManager(){
        if(!dataFolder.exists()){
            dataFolder.mkdir();
        }
        if(!this.blacklist.exists()){
            try {
                this.blacklist.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasWord(String word){
        return speedy.fileHas(blacklist, word, false);
    }

    public boolean addWord(String word){
        return speedy.writeInFile(blacklist, word, false);
    }

    public boolean removeWord(String word){
        List<String> words = speedy.getFileContent(blacklist);
        List<String> newWords = new ArrayList<>();
        for(String w : words){
            if(!w.equals(word)){
                newWords.add(w);
            }
        }
        return speedy.writeInFile(blacklist, word, true);
    }


}
