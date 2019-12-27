package ur.nyroma.features;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import ur.nyroma.main.speedy;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class JokesManager {
    private File jokeFolder = new File("data/jokes/");

    public JokesManager(){
        if (!jokeFolder.exists()) {
            jokeFolder.mkdirs();
        }
    }

    public boolean add(List<String> args){
        File jf = new File("data/jokes/" + args.get(0) + ".txt");
        try {
            jf.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return speedy.writeInFile(jf, getJoke(args), true);
    }

    public boolean remove(String name){
        File[] jokes = jokeFolder.listFiles();
        boolean exist = false;
        for(File f : jokes){
            if(f.getName().equals(name + ".txt")){
                exist = f.delete();
            }
        }
        return exist;
    }

    public String getListJokes(){
        File[] jokes = jokeFolder.listFiles();
        StringBuilder sb = new StringBuilder();
        for(File j : jokes){
            sb.append(j.getName()).append("\n");
        }
        return sb.toString();
    }

    public void tell(MessageChannel channel){
        File[] jokes = jokeFolder.listFiles();
        File joke = jokes[new Random().nextInt(jokes.length)];
        for (String s : speedy.getFileContent(joke)) {
            speedy.sendMess(channel, s);
        }
    }

    private String getJoke(List<String> toCut){
        StringBuilder sb = new StringBuilder();
        for(int i = 1 ; i < toCut.size() ; i++){
            sb.append(toCut.get(i)).append(" ");
        }
        return sb.toString();
    }
}
