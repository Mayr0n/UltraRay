package ur.nyroma.main;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class speedy {
    public static void sendMess(MessageChannel channel, String contenu){
        channel.sendMessage(contenu).queue();
    }
    public static void deleteMess(Message mess){
        mess.delete().queue();
    }
    public static void sendPrivMess(Message mess, String id, String message){
        mess.getGuild().getMemberById(id).getUser().openPrivateChannel().queue(
                (channel) -> channel.sendMessage(message).queue()
        );
    }
    public static void sendPrivMess(Guild server, String id, String message){
        server.getMemberById(id).getUser().openPrivateChannel().queue(
                (channel) -> channel.sendMessage(message).queue()
        );
    }
    public static void addMessEmote(Message mess, String code){ mess.addReaction(code).queue(); }
    public static String espace() {
        return "\n" + "\n";
    }
    public static String idMay = "301715312603168769";
    public static java.util.Date getDate(){
        return new GregorianCalendar().getTime();
    }
    public static boolean testFileExist(File file){
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            return true;
        }
    }
    public static void testFolderExist(File file){
        if(!file.exists()){
            file.mkdir();
        }
    }
    public static String getServerFolder(Guild server){
        return "data/servers/" + server.getId() + " (" + server.getName() + ")/";
    }
    public static TextChannel getChannelByName(Guild server, String name){
        TextChannel c = server.getTextChannels().get(0);
        for(TextChannel ch : server.getTextChannels()){
            if(ch.getName().equals(name)){
                c = ch;
            }
        }
        return c;
    }
    public static boolean fileHas(File file, String txt, boolean precise){
        boolean found = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            List<String> lines = reader.lines().collect(Collectors.toList());
            reader.close();
            if(precise){
                for(String word : lines){
                    if(word.equalsIgnoreCase(txt)){
                        found = true;
                    }
                }
            } else {
                if(lines.contains(txt)){
                    found = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ET. MERDE.");
        }
        return found;
    }
    public static List<String> getFileContent(File file){
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while(line != null){
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch(IOException | NullPointerException e){
            e.printStackTrace();
        }
        return lines;
    }
    public static boolean writeInFile(File file, String txt, boolean erase){
        testFileExist(file);
        try {
            FileWriter fw;
            if (erase) {
                fw = new FileWriter(file, false);
            } else {
                fw = new FileWriter(file, true);
            }
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(txt);
            bw.close();
            fw.close();
            return true;
        } catch(IOException e){
            e.printStackTrace();
            System.out.println("ET. MERDE.");
            return false;
        }
    }

}
