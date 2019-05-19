package main;

import net.dv8tion.jda.core.entities.Guild;

import java.io.File;

public class setupFolders {

    public void setupFolders(Guild server){
        File folderGen = new File("data/servers/");
        File[] folders = folderGen.listFiles();

        for(File folder : folders){
            if(folder.getName().contains(server.getId())){
                folder.renameTo(new File("data/servers/" + server.getId() +  " (" + server.getName() + ")/"));
            }
        }

        File gen = new File("data/servers/" + server.getId() + " (" + server.getName() + ")/");
        speedy.testFolderExist(gen);
        speedy.testFolderExist(new File(gen.getPath() + "/moderation/"));
        speedy.testFolderExist(new File(gen.getPath() + "/moderation/cooldowns/"));
        speedy.testFolderExist(new File(gen.getPath() + "/games/"));
        speedy.testFolderExist(new File(gen.getPath() + "/games/pp"));
        speedy.testFolderExist(new File(gen.getPath() + "/games/mathsplay"));
        speedy.testFolderExist(new File(gen.getPath() + "/leaderboards/"));
        speedy.testFolderExist(new File(gen.getPath() + "/leaderboards/pp"));
    }
}
