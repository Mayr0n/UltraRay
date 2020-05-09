package xyz.nyroma.gelp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.entities.Guild;
import xyz.nyroma.main.speedy;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JokeCache {
    public static List<Joke> jokes = new ArrayList<>();

    public static void setup(){
        File folder = new File("data/jokes/");
        speedy.testFolderExist(folder);
        try {
            for (File file : folder.listFiles()) {
                if (file.getName().length() > 5) {
                    if (file.getName().substring(file.getName().length() - 5).equals(".json")) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.setPrettyPrinting().create();
                        Joke j = gson.fromJson(new FileReader(file), Joke.class);
                        jokes.add(j);
                    }
                }
            }
        } catch (NullPointerException e){
            System.out.println("Il n'existe aucune Joke. Triste.");
        } catch (IOException e){
            System.out.println("Problème de fichiers.");
        }

        Thread th = new Thread(() -> {
            while(true){
                try {
                    Thread.sleep(60*1000);
                    System.out.println("Serialization...");
                    serialize();
                    System.out.println("Terminée !");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        th.setDaemon(true);
        th.start();
    }

    public static void serialize(){
        for(Joke j : jokes){
            try {
                File file = new File("data/trolls/" + j.getTitle() + ".json");
                speedy.testFileExist(file);
                GsonBuilder builder = new GsonBuilder();
                Gson g = builder.setPrettyPrinting().create();
                String json = g.toJson(j);
                FileWriter fw = new FileWriter(file);
                fw.write(json);
                fw.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void add(Joke j){
        jokes.add(j);
        serialize();
    }

    public boolean remove(Joke j){
        return jokes.remove(j);
    }

    public Joke get(String title) throws JokeException {
        for(Joke j : jokes){
            if(j.getTitle().equals(title)){
                return j;
            }
        }
        throw new JokeException("Il n'y a pas de joke avec ce titre.");
    }

    public List<Joke> getJokes() {
        return jokes;
    }
}
