package main;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

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
                       "`ur/pp info : permet d'avoir des informations sur le jeu de `pingpong` d'Ultra Ray" + espace() +
                       "`ur/mathsplayinfo : permet d'avoir des informations sur le jeu `mathsplay` d'Ultra Ray" + espace() +
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
                   new moderation.troll().bound(member, mess, server);
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
       }
    }

    private boolean isStaff(Member member){
        return member.hasPermission(Permission.KICK_MEMBERS);
    }
}
