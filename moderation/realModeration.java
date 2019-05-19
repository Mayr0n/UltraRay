package moderation;

import main.speedy;
import net.dv8tion.jda.core.entities.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static main.speedy.sendMess;

public class realModeration {
    public void kick(Message mess, Guild server){
        List<Member> l = mess.getMentionedMembers();
        if (l.size() == 1) {
            server.getController().kick(l.get(0), "Auf wiedersehen !").queue();
            sendMess(mess,l.get(0).getEffectiveName() + " a été kick");

            File registerKicks = new File(speedy.getServerFolder(server) + "moderation/kicks.txt");
            speedy.testFileExist(registerKicks);
            try {
                FileWriter fw = new FileWriter(registerKicks, true);
                PrintWriter pw = new PrintWriter(fw);
                pw.write(l.get(0).getUser().getName() + "#" + l.get(0).getUser().getDiscriminator() + " /// " + speedy.getDate());
            } catch(IOException e){
                e.printStackTrace();
            }
        } else {
            sendMess(mess,"Mais qui faut-il kick ? Syntaxe : `ur/kick <Membre>`");
        }
    }
    public void censure(Guild server, Message mess, User u){
        String contenuMess = mess.getContentDisplay();
        String[] mots = contenuMess.split(" ");
        File censorWords = new File("data/modGen/censure.txt");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(censorWords), StandardCharsets.UTF_8));
            String line = reader.readLine();
            Boolean censored = false;

            while (line != null && !censored) {
                for(String mot : mots){
                    if(mot.equalsIgnoreCase(line)){
                        mess.delete().queue();
                        sendMess(mess,"Hey " + u.getName() + " ! restes poli s'il te plaît ! '" + line + "' est censuré");
                        censored = true;
                        File userInfr = new File(speedy.getServerFolder(server) + "moderation/infractions.txt");
                        speedy.testFileExist(userInfr);
                        registerInfr(u, userInfr, contenuMess);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
    public void closeChannels(Guild server){
        List<Channel> channels = server.getChannels();
        for(Channel channel : channels){
            if(channel.getType().equals(ChannelType.TEXT)){
                channel.getManager().setSlowmode(120).queue();
            }
        }

    }
    public void openChannels(Guild server){
        List<Channel> channels = server.getChannels();
        for(Channel channel : channels){
            if(channel.getType().equals(ChannelType.TEXT)) {
                channel.getManager().setSlowmode(0).queue();
            }
        }

    }
    private void registerInfr(User u, File file, String contenuMess){
        try {
            FileWriter writer = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(u.getName() + " (" + u.getId() + ") /// date : " + speedy.getDate() + " /// contenu du message : " + contenuMess);
            bw.newLine();
            bw.close();
            writer.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
