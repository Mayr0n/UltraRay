import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.managers.GuildController;

import java.io.*;
import java.util.List;
import java.util.Random;

public class Commandes {

    private static String idMay = "301715312603168769";

    @SuppressWarnings("Unused")
    public void testCommande(Message mess, Guild server, Member member){

       String contenuMess = mess.getContentDisplay();

       if(contenuMess.contains("help")){
           sendMess(mess, "`ur/say : Pour que @Ultra Ray dise votre message`" + espace() +
                   "`ur/joke : PARCE QUE L'HUMOUR C'EST DROLE`" + espace() +
                   "`ur/pingpong <Difficulté> : permet à un joueur de s'enregistrer pour jouer au ping pong avec @Ultra Ray#7792`" + espace() +
                   "`ur/pstop : permet aux joueurs de retirer leur enregistrement au ping pong, et donc permettre aux autres membres de jouer`" + espace() +
                   "`ur/dab : DAAAAAAAAAAAAAAAAB`")
           ;
       }
       else if(contenuMess.contains("say")){
            say(mess, member);
       }
       else if(contenuMess.contains("joke")){
            joke(mess);
       }
       else if(contenuMess.contains("kick") && member.hasPermission(Permission.KICK_MEMBERS)){
            kick(mess, server);
       }
       else if(contenuMess.contains("pp")){
            new jeux.pingpong().pingpong(mess, member);
       }
       else if(contenuMess.contains("dab")){
           sendMess(mess,"https://tenor.com/view/dab-dab-problem-dab-dance-gif-5412848");
       }
       else if(contenuMess.contains("renameall") && member.isOwner()){
            renameAll(mess, server);
       }
       else if(contenuMess.contains("idsall") && member.isOwner()){
            getIdsAll(mess, server);
       }
       else if(contenuMess.contains("resetallnames") && member.isOwner()){
           List<Member> listUser = server.getMembers();
           GuildController serverAdmin = server.getController();

           for (Member m : listUser) {
               if(!m.isOwner()) {
                   serverAdmin.setNickname(m, m.getUser().getName()).queue();
                   try {
                       Thread.sleep(200);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }

           sendMess(mess,"Terminé !");
       }
       else if(contenuMess.contains("getid")){
           String[] message = contenuMess.split(" ");

           if(message.length == 2){

               if(message[1].contains("#") && message[1].contains("@")){

                   String[] name = message[1].split("#");
                   List <Member> mentionned = server.getMembersByName(name[0], true);
                   sendMess(mess, mentionned.get(0).getUser().getId());
               } else {
                   sendMess(mess, mess.getChannel().getId());
               }

           } else {
               sendMess(mess, "[Erreur syntaxe !] Syntaxe : `ur/getid <salon ou membre#0000>");
           }
       }
       else if(contenuMess.contains("mathsplay")){
           new jeux.mathsplay().mathsplay(member, mess);
       }
    }

    private static void say(Message mess, Member m){
        String contenuMess = mess.getContentDisplay();
        String[] s = contenuMess.split(" ");

        if (s.length >= 2) {

            int i = contenuMess.length();
            mess.delete().queue();
            sendMess(mess, contenuMess.substring(6, i) + " ||(demandé par " + m.getUser().getName() + ")||");
        } else {
            sendMess(mess,"Mais quel message dire ? `Syntaxe : ur/say <Message>`");
        }
    }
    private static void joke(Message mess){
        Random r = new Random();

        int i = r.nextInt(14);

        switch (i) {

            case 1:
                sendMess(mess, "VOUS SAVEZ COMMENT LAURENT RUQUIER RETIRE SA CAPOTE ?" + espace() + "EN PETANT");
                break;
            case 2:
                sendMess(mess,"Pourquoi dit-on que Jules César était gay?" + espace() + "Car quand Vercingétorix s'est mit à genoux, il a eu la Gaulle");
                break;
            case 3:
                sendMess(mess,"C'est l'histoire d'une fleur qui court et qui se plante...");
            case 4:
                sendMess(mess,"Qu'est ce que le moyen de transport favori des humoristes ?" + espace() + "La caravanne");
                break;
            case 5:
                sendMess(mess,"Qu'est ce qui est jaune et noir ?" + espace() + "Une abeille...");
                break;
            case 6:
                sendMess(mess,
                        "Alors c'est un petit cowboy, un petit cowboy énervé, il rentre dans le cowboy, il rentre dans le saloon et il fait : " + espace() +
                                "Qui c'est qui a osé peindre mon cheval en bleu ??" + espace() +
                                "puis là t'as un énooooorme gaillard qui se lève du comptoir, il dit :" + espace() +
                                "C'est moi, pourquoi ?" + espace() +
                                "puis t'as le cowboy, il fait :" + espace() +
                                "Ah non mais c'est juste pour savoir quand vous alliez mettre la deuxième couche...");
                break;
            case 7:
                sendMess(mess,"Qu'est ce qui est jaune et qui attend ?" + espace() + "Un gilet jaune sur un rond point.");
                break;
            case 8:
                sendMess(mess,"Quel est le point commun entre un sumo et un mauvais humoriste ?" + espace() + "Ils sont tous les deux lourds...");
                break;
            case 9:
                sendMess(mess,
                        "Vous avez une idée de la blague que je vais faire ?" + espace() +
                                "Aucune idée ?" + espace() +
                                "C'est normal car c'est Johnny qui à l'idée puisque Johnny Halliday");
                break;
            case 10:
                sendMess(mess,"Tu veux une blague avec une chute ?" + espace() + "Bah c'est un mec qui court puis il tombe...");
                break;
            case 11:
                sendMess(mess,"C'est quoi le pays le plus cool du monde ?" + espace() + "Le Yémen parce que yeah men !!!!!");
                break;
            case 12:
                sendMess(mess,"Attention, il ne faut pas confondre un coca cola aux glaçons et un caca collé au caleçon.");
                break;
            case 13:
                sendMess(mess,"Comment on appelle un boomerang qui revient pas ?" + espace() + "Un bâton.");
                break;
            case 14:
                sendMess(mess,"Comment on appelle un boomerang qui revient pas ?" + espace() + "Un chat mort.");
                break;
        }
    }
    private static void kick(Message mess, Guild server){
        List<Member> l = mess.getMentionedMembers();

        if (l.size() == 1) {
            server.getController().kick(l.get(0), "Auf wiedersehen !").queue();
            sendMess(mess,l.get(0).getEffectiveName() + " a été kick");
        } else {
            sendMess(mess,"Mais qui faut-il kick ? `Syntaxe : ur/kick <Membre>`");
        }
    }
    private static void renameAll(Message mess, Guild server){
        String contenuMess = mess.getContentDisplay();
        String[] contMess = contenuMess.split(" ");

        if (contMess.length > 1) {
            contMess[0] = "";
            String newNick = String.join(" ", contMess);
            List<Member> listUser = server.getMembers();

            for (Member m : listUser) {
                GuildController serveradmin = server.getController();
                if(!m.isOwner()) {
                    serveradmin.setNickname(m, newNick).queue();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ee) {
                        System.out.print("oof");
                    }
                }
            }
            sendMess(mess,"Tout le monde a été renommé en ***" + newNick + "*** !");
        } else {

            sendMess(mess,"[Nop !] Syntaxe : ur/renameall <rename en ?>");

        }
    }
    private static void getIdsAll(Message mess, Guild server){

        File file = new File("./Servers/"+server.getId()+".txt");
        try {

        FileWriter writer = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(writer);

            if(!file.exists()){
                file.createNewFile();
            }
            List <Member> ms = server.getMembers();

            for(Member member : ms){

                String name = member.getUser().getName();
                String id = member.getUser().getId();
                bw.write(name + " : " + id);
                bw.newLine();
            }

            bw.close();
            writer.close();

            sendMess(mess, "Terminé !");

        } catch (IOException e) {
            e.printStackTrace();
            sendMess(mess, "Une erreur est survenue...");
        }
    }

    private static void sendMess(Message mess, String contenu){
        mess.getChannel().sendMessage(contenu).queue();
    }
    private static String espace() {return "\n" + "\n";}
}
