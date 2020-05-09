package xyz.nyroma.gelp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.entities.Guild;
import xyz.nyroma.main.speedy;
import xyz.nyroma.ray.BlacklistException;
import xyz.nyroma.ray.BlacklistManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class TrollingCache {

    private static Hashtable<TrollingManager, String> tms = new Hashtable<>();

    public static void setup(){
        File folder = new File("data/trolls/");
        speedy.testFolderExist(folder);
        try {
            for (File file : folder.listFiles()) {
                if (file.getName().length() > 5) {
                    if (file.getName().substring(file.getName().length() - 5).equals(".json")) {
                        GsonBuilder builder = new GsonBuilder();
                        Gson gson = builder.setPrettyPrinting().create();
                        TrollingManager tm = gson.fromJson(new FileReader(file), TrollingManager.class);
                        tms.put(tm, tm.getServerID());
                    }
                }
            }
        } catch (NullPointerException e){
            System.out.println("Il n'existe aucun TrollingManager.");
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
        for(TrollingManager tm : tms.keySet()){
            try {
                File file = new File("data/trolls/" + tm.getServerID() + ".json");
                speedy.testFileExist(file);
                GsonBuilder builder = new GsonBuilder();
                Gson g = builder.setPrettyPrinting().create();
                String json = g.toJson(tm);
                FileWriter fw = new FileWriter(file);
                fw.write(json);
                fw.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public void add(TrollingManager blm, String serverID){
        tms.put(blm, serverID);
        serialize();
    }

    public void remove(TrollingManager blm){
        tms.remove(blm);
    }

    public TrollingManager get(Guild server) throws TrollingException {
        for(TrollingManager tm : tms.keySet()){
            if(tms.get(tm).equals(server.getId())){
                return tm;
            }
        }
        throw new TrollingException("Ce serveur n'a pas de BlacklistManager.");
    }


}
