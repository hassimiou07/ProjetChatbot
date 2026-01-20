import jdk.jshell.execution.Util;

import java.util.ArrayList;
import java.util.Scanner;

public class Chatbot {

    private static final String MESSAGE_IGNORANCE = "Je ne sais pas.";
    private static final String MESSAGE_APPRENTISSAGE = "Je vais te l'apprendre.";
    private static final String MESSAGE_BIENVENUE = "J'attends tes questions de culture générale.";
    private static final String MESSAGE_QUITTER = "Au revoir.";
    private static final String MESSAGE_INVITATION = "Je t'écoute.";
    private static final String MESSAGE_CONFIRMATION = "Très bien, c'est noté.";

    private static Index indexThemes; // index pour trouver rapidement les réponses à partir des mots NON outils de la question
    private static Index indexFormes; // index pour trouver rapidement les formes de réponse possibles à partir des mots-outils de la question

    static private ArrayList<String> motsOutils; // vecteur trié des mots outils
    static private ArrayList<String> reponses; // vecteur des réponses
    private static ArrayList<String> formesReponses; //vecteur des formes de réponses
    private static Thesaurus thesaurus; //thésaurus

    public static void main(String[] args) {

        // initialisation du vecteur des mots outils
        motsOutils = Utilitaire.lireMotsOutils("mots-outils.txt");
        // tri du vecteur des mots outils
        Utilitaire.trierChaines(motsOutils);

        // initialisation du vecteur des réponses
//        reponses = Utilitaire.lireReponses("reponses.txt");
        reponses = Utilitaire.lireReponses("mini_reponses.txt");
        indexThemes = Utilitaire.constructionIndexReponses(reponses,motsOutils);


        formesReponses = Utilitaire.constructionTableFormes(reponses, motsOutils);

        // initialisation du thésaurus (partie 2)
        //thesaurus = ...

        // construction de l'index pour retrouver rapidement les réponses sur leurs thématiques
        //indexThemes = ...
        //indexThemes.afficher();

        // construction de la table des formes de réponses
        //formesReponses = ...
        //System.out.println(formesReponses);

        // initialisation du vecteur des questions/réponses idéales
        ArrayList<String> questionsReponses = Utilitaire.lireQuestionsReponses("questions-reponses.txt");
        //ArrayList<String> questionsReponses = Utilitaire.lireQuestionsReponses("mini_questions-reponses.txt");

        // construction de l'index pour retrouver rapidement les formes possibles de réponses à partir des mots outils de la question
        //indexFormes = ...
        //indexFormes.afficher();

        String reponse = "";
        String entreeUtilisateur = ""; // la dernière entrée de l'utilisateur


        Scanner lecteur = new Scanner(System.in);
        System.out.println();
        System.out.print("> ");
        System.out.println(MESSAGE_BIENVENUE);

        do { // on attend des questions
            System.out.print("> ");
            entreeUtilisateur = lecteur.nextLine();
            if (entreeUtilisateur.compareTo(MESSAGE_QUITTER) != 0) { //tant que l'utilisateur ne veut pas arrêter
                reponse = repondre(entreeUtilisateur);
                System.out.println("> " + reponse);
            }
        } while (entreeUtilisateur.compareToIgnoreCase(MESSAGE_QUITTER) != 0);


    }


    static private String repondre(String question) {
        ArrayList<Integer> reponsesCandidates = Utilitaire.constructionReponsesCandidates(question,indexThemes,motsOutils);
        //ArrayList<Integer> reponsesSelectionnees;
        if (reponsesCandidates.isEmpty()){
            return MESSAGE_IGNORANCE;
        }
        int i = 0;
        String resultat = "";
        while(i < reponsesCandidates.size()){
            resultat += reponses.get(reponsesCandidates.get(i)) + "\n";
            i++;
        }
        return resultat;
    }

    // partie 2
    static private String repondreEnContexte(String question, String questionPrecedente) {
        return "";
    }


}