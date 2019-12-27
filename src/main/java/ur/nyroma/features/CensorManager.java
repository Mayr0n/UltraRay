package ur.nyroma.features;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import ur.nyroma.main.speedy;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static ur.nyroma.main.speedy.*;

public class CensorManager {
    private File censFile;

    public CensorManager(Guild server) {
        File folder = new File(speedy.getServerFolder(server) + "mod/");
        this.censFile = new File(speedy.getServerFolder(server) + "mod/censure.txt");
        speedy.testFolderExist(folder);
        speedy.testFileExist(this.censFile);
    }

    public void test(Message mess) {
        String[] mots = mess.getContentRaw().split(" ");
        Guild server = mess.getGuild();
        User u = mess.getAuthor();
        for(String mot : mots){
            if(speedy.fileHas(this.censFile, mot, true) && !mess.getMember().hasPermission(Permission.KICK_MEMBERS)){
                speedy.deleteMess(mess);
                logMess(mess);
                sendPrivMess(server, u.getId(), "Hey " + u.getName() + " ! restes poli s'il te plaît ! \"" + mot + "\" est censuré");
            }
        }
    }

    private void logMess(Message mess) {
        String txt = ">>> \"" + mess.getContentRaw() + "\" de " + mess.getAuthor().getName() + "#" + mess.getAuthor().getDiscriminator() +
                ", Salon : " + mess.getChannel().getName();
        sendMess(speedy.getChannelByName(mess.getGuild(), "infractions_auto"), txt);
    }

    public boolean add(String mot) {
        return writeInFile(this.censFile, mot + "\n", false);
    }

    public boolean remove(String mot) {
        boolean changed = false;
        try {
            List<String> words = speedy.getFileContent(this.censFile);
            censFile.delete();
            censFile.createNewFile();
            for (String word : words) {
                if (!word.equalsIgnoreCase(mot)) {
                    writeInFile(this.censFile, word + "\n", false);
                } else {
                    changed = true;
                }
            }
        } catch(IOException e){
            return false;
        }
        return changed;
    }

    public String getList(){
        StringBuilder sb = new StringBuilder();
        sb.append("**Mots bannis :** \n");
        for (String s : speedy.getFileContent(this.censFile)) {
            sb.append(s).append("\n");
        }
        return sb.toString();
    }
}
