package xyz.nyroma.caches;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import xyz.nyroma.entities.ServerInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ServerInfoCache {
    private static List<ServerInfo> sis = new ArrayList<>();
    private static File infosFolder;

    public static void setup(File mainFolder){
        sis.clear();
        if(!mainFolder.exists()){
            mainFolder.mkdirs();
        }
        infosFolder = new File(mainFolder.getPath() + "/servers/");
        if(!infosFolder.exists()){
            infosFolder.mkdirs();
        }
        File[] infosFiles = infosFolder.listFiles();
        if(infosFiles != null && infosFiles.length > 0){
            for(File file : infosFiles){
                try {
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.setPrettyPrinting().create();
                    sis.add(gson.fromJson(new FileReader(file), ServerInfo.class));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
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

    public static void serialize() {
        for(ServerInfo info : sis){
            try {
                File infoFile = new File(infosFolder + "/" + info.getServerID() + ".json");
                infoFile.delete();
                infoFile.createNewFile();

                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.setPrettyPrinting().create();
                String s = gson.toJson(info);
                FileWriter fw = new FileWriter(infoFile);
                fw.write(s);
                fw.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void add(ServerInfo serverInfo){
        sis.add(serverInfo);
    }

    public static void remove(ServerInfo serverInfo){
        sis.remove(serverInfo);
    }

    public static Optional<ServerInfo> get(long serverID){
        for(ServerInfo info : sis){
            if(info.getServerID() == serverID){
                return Optional.of(info);
            }
        }
        return Optional.empty();
    }

    public static Optional<ServerInfo> get(String serverName){
        for(ServerInfo info : sis){
            if(info.getServerName().equals(serverName)){
                return Optional.of(info);
            }
        }
        return Optional.empty();
    }

}
