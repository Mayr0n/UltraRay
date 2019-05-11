package moderation;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;
import java.util.List;

import static main.speedy.sendMess;

public class realModeration {
    public void kick(Message mess, Guild server){
        List<Member> l = mess.getMentionedMembers();

        if (l.size() == 1) {
            server.getController().kick(l.get(0), "Auf wiedersehen !").queue();
            sendMess(mess,l.get(0).getEffectiveName() + " a été kick");
        } else {
            sendMess(mess,"Mais qui faut-il kick ? Syntaxe : `ur/kick <Membre>`");
        }
    }
    public void censure(Message mess, User u){
        String contenuMess = mess.getContentDisplay();
        File censorWords = new File("data/moderation/censure.txt");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(censorWords), StandardCharsets.UTF_8));
            String line = reader.readLine();

            while (line != null) {
                if (contenuMess.contains(line)) {
                    mess.delete().queue();
                    sendMess(mess,"Hey " + u.getName() + " ! restes poli s'il te plaît ! '" + line + "' est censuré");
                    line = null;
                    File userInfr = new File("data/moderation/infractions.txt");
                    registerInfr(u, userInfr, contenuMess);
                } else {
                    line = reader.readLine();
                }
            }
            reader.close();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }
    private void registerInfr(User u, File file, String contenuMess){
        try {
            FileWriter writer = new FileWriter(file, true);
            PrintWriter pw = new PrintWriter(writer);
            java.util.GregorianCalendar calendar = new GregorianCalendar();
            java.util.Date time  = calendar.getTime();
            pw.println(u.getName() + " (" + u.getId() + ") /// date : " + time + " /// contenu du message : " + contenuMess);
            pw.close();
            writer.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
