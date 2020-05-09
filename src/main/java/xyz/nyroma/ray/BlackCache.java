package xyz.nyroma.ray;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.entities.Guild;
import xyz.nyroma.main.speedy;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class BlackCache {
    public static Hashtable<BlacklistManager, String> blms = new Hashtable<>(); //blm, ID

    public static void setup(){
        File folder = new File("data/blacklists/");
        speedy.testFolderExist(folder);
        try {
            for (File file : folder.listFiles()) {
                if (file.getName().length() > 5) {
                    if (file.getName().substring(file.getName().length() - 5).equals(".json")) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.setPrettyPrinting().create();
                        BlacklistManager blm = gson.fromJson(new FileReader(file), BlacklistManager.class);
                        blms.put(blm, blm.getServerID());
                    }
                }
            }
        } catch (NullPointerException e){
            System.out.println("Il n'existe aucun BlacklistManager.");
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
        for(BlacklistManager blm : blms.keySet()){
            try {
                File file = new File("data/blacklists/" + blm.getServerID() + ".json");
                speedy.testFileExist(file);
                GsonBuilder builder = new GsonBuilder();
                Gson g = builder.setPrettyPrinting().create();
                String json = g.toJson(blm);
                FileWriter fw = new FileWriter(file);
                fw.write(json);
                fw.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void add(BlacklistManager blm, String serverID){
        blms.put(blm, serverID);
        serialize();
    }

    public void remove(BlacklistManager blm){
        blms.remove(blm);
    }

    public BlacklistManager get(Guild server) throws BlacklistException {
        for(BlacklistManager blm : blms.keySet()){
            if(blms.get(blm).equals(server.getId())){
                return blm;
            }
        }
        throw new BlacklistException("Ce serveur n'a pas de BlacklistManager.");
    }

}
