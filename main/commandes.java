package main;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static main.speedy.espace;
import static main.speedy.sendMess;

public class commandes {
    public void doCommande(Message mess, Guild server, Member member){
       String contenuMess = mess.getContentDisplay();
       String[] mots = contenuMess.split(" ");

       switch(mots[0]){
           case "ur/help":
               sendMess(mess, "`ur/say : Pour que @Ultra Ray dise votre message`" + espace() +
                       "`ur/joke : PARCE QUE L'HUMOUR C'EST DROLE`" + espace() +
                       "`ur/pp info : permet d'avoir des informations sur le jeu de 'pingpong' d'Ultra Ray" + espace() +
                       "`ur/mathsplayinfo : permet d'avoir des informations sur le jeu 'mathsplay' d'Ultra Ray" + espace() +
                       "`ur/dab : DAAAAAAAAAAAAAAAAB`")
               ;
               break;
           case "ur/say":
               String[] s = contenuMess.split(" ");

               if (s.length >= 2) {

                   int i = contenuMess.length();
                   mess.delete().queue();
                   sendMess(mess, contenuMess.substring(6, i) + " ||(demandé par " + member.getUser().getName() + ")||");
               } else {
                   sendMess(mess,"Mais quel message dire ? `Syntaxe : ur/say <Message>`");
               }
               break;
           case "ur/joke":
               File jokeFolder = new File("data/jokes/");
               File[] jokes = jokeFolder.listFiles();
               if(jokes != null){
                   Random r = new Random();
                   int i = r.nextInt(jokes.length);

                   File joke = jokes[i];
                   try {
                       BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(joke), StandardCharsets.UTF_8));
                       String line = reader.readLine();
                       while(line != null){
                           sendMess(mess, line);
                           line = reader.readLine();
                       }
                       reader.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
               break;
           case "ur/kick":
               if(isStaff(member)){
                   new moderation.realModeration().kick(mess, server);
               } else {
                   sendMess(mess, "Tu dois être staff pour ça !");
               }
               break;
           case "ur/pp":
               new jeux.pingpong().pingpong(mess, member);
               break;
           case "ur/dab":
               sendMess(mess,"https://tenor.com/view/dab-dab-problem-dab-dance-gif-5412848");
               break;
           case "ur/renameall":
               if(isStaff(member)){
                   new moderation.features().renameAll(mess, server);
               } else {
                   sendMess(mess, "Tu dois être staff pour ça !");
               }
               break;
           case "ur/idsall":
               if(isStaff(member)){
                   new moderation.features().getIdsAll(mess, server);
               } else {
                   sendMess(mess, "Tu dois être staff pour ça !");
               }
               break;
           case "ur/resetallnames":
               if(isStaff(member)){
                   new moderation.features().resetAllNames(mess, server);
               } else {
                   sendMess(mess, "Tu dois être staff pour ça !");
               }
               break;
           case "ur/getid":
               if(isStaff(member)){
                   new moderation.features().getID(mess, server);
               } else {
                   sendMess(mess, "Tu dois être staff pour ça !");
               }
               break;
           case "ur/mathsplay":
               new jeux.mathsplay().mathsplay(member, mess);
               break;
           case "ur/bound":
               if(isStaff(member)){
                   new moderation.troll().bound(mess, server);
               } else {
                   sendMess(mess, "Tu dois être staff pour ça !");
               }
               break;
           case "ur/unbound":
               if(isStaff(member)){
                   new moderation.troll().unbound(mess);
               } else {
                   sendMess(mess, "Tu dois être staff pour ça !");
               }
               break;
           case "ur/spam":
               if(isStaff(member)){
                   new moderation.troll().spam(mess);
               } else {
                   sendMess(mess, "Tu dois être staff pour ça !");
               }
               break;
           case "ur/closeChannels":
                if(member.isOwner()){
                    new moderation.realModeration().closeChannels(server);
                } else {
                    sendMess(mess, "Il n'y a que le propriétaire qui peut faire ça !");
                }
               break;
           case "ur/openChannels":
               if(member.isOwner()){
                   new moderation.realModeration().openChannels(server);
               } else {
                   sendMess(mess, "Il n'y a que le propriétaire qui peut faire ça !");
               }
               break;
       }
    }
    private boolean isStaff(Member member){
        return member.hasPermission(Permission.MANAGE_ROLES);
    }
}
