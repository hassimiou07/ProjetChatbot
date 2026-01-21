
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Utilitaire {

    private static final int NBMOTS_FORME = 5; // nombre maximal de mots-outils pris en compte pour les formes dans l'étape 2

    static public ArrayList<String> lireMotsOutils(String nomFichier) {
        //{}=>{résultat = le vecteur des mots outils construit à partir du fichier nomFichier}
        ArrayList<String> motsOutils = new ArrayList<>();
        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                motsOutils.add(ligne);
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return motsOutils;
    }

    static public ArrayList<String> lireReponses(String nomFichier) {
        //{}=>{résultat = le vecteur des réponses construit à partir du fichier nomFichier}
        ArrayList<String> reponses = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                reponses.add(ligne);
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return reponses;
    }

    static public ArrayList<String> lireQuestionsReponses(String nomFichier) {
        //{}=>{résultat = le vecteur des questions/réponses construit à partir du fichier nomFichier}
        ArrayList<String> questionsReponses = new ArrayList<>();

        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                questionsReponses.add(ligne);
            }

            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questionsReponses;
    }

    public static void ecrireFichier(String nomFichier, String chaineAEcrire) {
        //{}=>{la chaîne  chaineAEcrire est écrite après saut de ligne à la suite du fichier nomFichier}
        // true = mode append ? écrit à la suite sans effacer ce qui existe
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier, true))) {
            writer.newLine();
            writer.write(chaineAEcrire);// ajoute un retour à la ligne
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static private ArrayList<String> decoupeEnMots(String contenu) {
        //{}=>{résultat = le vecteur des mots de la chaîne contenu après pré-traitements divers}
        String chaine = contenu.toLowerCase();
        chaine = chaine.replace('\n', ' ');
        chaine = chaine.replace('?', ' ');
        chaine = chaine.replace('-', ' ');
        chaine = chaine.replace('\'', ' ');
        chaine = chaine.replace('.', ' ');
        chaine = chaine.replace(',', ' ');
        chaine = chaine.replace(':', ' ');
        chaine = chaine.replace(';', ' ');
        chaine = chaine.replace('\'', ' ');
        chaine = chaine.replace('"', ' ');
        chaine = chaine.replace('', ' ');
        chaine = chaine.replace('', ' ');
        chaine = chaine.replace("'", " ");
        chaine = chaine.replace('(', ' ');
        chaine = chaine.replace(')', ' ');
        chaine = chaine.replace('«', ' ');
        chaine = chaine.replace('-', ' ');
        chaine = chaine.replace('’', ' ');
        chaine = chaine.replace('-', ' ');
        chaine = chaine.replace('»', ' ');




        String[] tabchaine = chaine.split("\\s");
        ArrayList<String> resultat = new ArrayList<>();

        for (int i = 0; i < tabchaine.length; ++i) {
            if (!tabchaine[i].equals("")) {
                resultat.add(tabchaine[i]);
            }
        }

        return resultat;
    }


    static private boolean existeChaine(ArrayList<String> mots, String mot) {
        //{}=>  {recherche séquentielle de mot dans mots
        // résultat =  true si trouvé et false sinon }
        boolean trouve = false;

        for (int i = 0; i < mots.size() && !trouve; i++) {
            if (mots.get(i).equals(mot)) {
                trouve = true; // La condition !trouve deviendra fausse, la boucle s'arrêtera
            }
        }

        return trouve;
    }


    static private boolean existeChaineDicho(ArrayList<String> lesChaines, String chaine) {
        //{lesChaines (triée dans l'ordre lexicographique)}=>  {recherche dichotomique de chaine dans lesChaines
        // résultat =  true si trouvé et false sinon }
        if (lesChaines.isEmpty()) {
            return false;
        } else if (lesChaines.getLast().compareTo(chaine) < 0) {
            return false;
        } else {
            int inf = 0, sup = lesChaines.size() - 1;
            while (inf < sup) {
                int m = (inf + sup) / 2;
                if (lesChaines.get(m).compareTo(chaine) >= 0) {
                    sup = m;
                } else {
                    inf = m + 1;
                }
            }
            return lesChaines.get(sup).equals(chaine);
        }
    }

    static public boolean entierementInclus(ArrayList<String> mots, String question) {
        //{mots est trié dans l'ordre lexicographique}=>
        // résultat = true si tous les mots de questions sont dans mots, false sinon
        // remarque : utilise decoupeEnMots et existeChaineDicho}
        ArrayList<String> motquestion = decoupeEnMots(question);
        int i = 0;
        while ( i < motquestion.size() && existeChaineDicho(mots,motquestion.get(i))){
            i++;
        }
        return i == motquestion.size();
    }


    static private int rechercherChaine(ArrayList<String> lesChaines, String chaine) {
        // {}=>{résultat = l'indice de chaine dans lesChaines si trouvé et -1 sinon }
        int indiceResultat = -1;
        boolean trouve = false;

        for (int i = 0; i < lesChaines.size() && !trouve; i++) {
            if (lesChaines.get(i).equals(chaine)) {
                indiceResultat = i;
                trouve = true;
            }
        }

        return indiceResultat;
    }


    static public void integrerNouvelleQuestionReponse(String question, String reponse, ArrayList<String> formes, Index indexFormes, ArrayList<String> motsOutils,Thesaurus thesaurus) {
        //{la forme de reponse n'existe pas ou n'est pas associée à question dans indexFormes}=>{la forme de reponse est ajoutée à la fin de formes si elle n'y est pas déjà
        // et indexFormes est mis à jour pour tenir compte de cette nouvelle question-réponse
        // remarque 1 : utilise calculForme, rechercherChaine, decoupeEnMots, existeChaineDicho, ajouterSortieAEntree, rechercherSortiePourEntree
        // remarque 2 : seuls les NBMOTS_FORME premiers mots-outils de la question sont pris en compte}

        String formeDeLaReponse = calculForme(reponse, motsOutils, thesaurus);
        int indiceForme = rechercherChaine(formes, formeDeLaReponse);
        if (indiceForme == -1) {
            formes.add(formeDeLaReponse);
            indiceForme = formes.size() - 1;
        }
        ArrayList<String> motsQuestion = decoupeEnMots(question);
        int nbMotsOutilsTrouves = 0;
        int i = 0;
        while (i < motsQuestion.size() && nbMotsOutilsTrouves < NBMOTS_FORME) {
            String motOriginal = motsQuestion.get(i).toLowerCase();
            String motATraiter;
            if (estUnNombre(motOriginal)) {
                motATraiter = "NUM";
            } else {
                motATraiter = thesaurus.rechercherSortiePourEntree(motOriginal);
            }
            if (existeChaineDicho(motsOutils, motATraiter)) {
                String entreeIndex = motATraiter + "_" + nbMotsOutilsTrouves;
                indexFormes.ajouterSortieAEntree(entreeIndex, indiceForme);
                nbMotsOutilsTrouves++;
            }
            i++;
        }
    }



    static public void IntegrerNouvelleReponse(String reponse, ArrayList<String> reponses, Index indexContenu, ArrayList<String> motsOutils,Thesaurus thesaurus) {
        //{reponse n'est pas présent dans reponses}=>{reponse est ajoutée à la fin de reponses et indexContenu est mis à jour pour tenir compte de cette nouvelle réponse
        // remarque : utilise decoupeEnMots, existeChaineDicho, ajouterSortieAEntree, rechercherSortiePourEntree
        reponses.add(reponse);
        ArrayList<String> mots = decoupeEnMots(reponse);
        int i = 0;
        while (i < mots.size()) {
            String motCanonique = thesaurus.rechercherSortiePourEntree(mots.get(i));
            if (!existeChaineDicho(motsOutils, motCanonique)) {
                indexContenu.ajouterSortieAEntree(motCanonique, reponses.size()-1);
            }
            i++;
        }
    }

    static public Index constructionIndexReponses(ArrayList<String> reponses, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = un index dont les entrées sont les mots des réponses (reponses) absents de motsOutils.
        // et les sorties sont les indices (dans reponses) des réponses les contenant.
        // remarque : utilise existeChaineDicho, decoupeEnMots et ajouterSortieAEntree }
        Index index = new Index();
        int i = 0;
        while (i < reponses.size()) {
            ArrayList<String> phrasedecoupe = decoupeEnMots(reponses.get(i));
            int j = 0;
            while (j < phrasedecoupe.size()) {
                String motCanonique = thesaurus.rechercherSortiePourEntree(phrasedecoupe.get(j));
                if (!existeChaineDicho(motsOutils, motCanonique)) {
                    index.ajouterSortieAEntree(motCanonique, i);
                }
                j++;
            }
            i++;
        }
        return index;
    }


    static void trierChaines(ArrayList<String> v) {
        //{}=>{v est trié dans l'ordre lexicographique }
        if (v.size() <= 1) {
            return;
        }
        int milieu = v.size() / 2;

        ArrayList<String> gauche = new ArrayList<>();
        ArrayList<String> droite = new ArrayList<>();

        for (int i = 0; i < milieu; i++) {
            gauche.add(v.get(i));
        }
        for (int i = milieu; i < v.size(); i++) {
            droite.add(v.get(i));
        }

        trierChaines(gauche);
        trierChaines(droite);

        fusionner(v, gauche, droite);
    }

    static void fusionner(ArrayList<String> v,
                          ArrayList<String> gauche,
                          ArrayList<String> droite) {
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < gauche.size() && j < droite.size()) {
            if (gauche.get(i).compareTo(droite.get(j)) <= 0) {
                v.set(k, gauche.get(i));
                i++;
            } else {
                v.set(k, droite.get(j));
                j++;
            }
            k++;
        }

        while (i < gauche.size()) {
            v.set(k, gauche.get(i));
            i++;
            k++;
        }

        while (j < droite.size()) {
            v.set(k, droite.get(j));
            j++;
            k++;
        }
    }


    static ArrayList<Integer> maxOccurences(ArrayList<Integer> v, int seuil) {
        //{v trié} => {résultat = vecteur des entiers dont le nombre d'occurences
        // est maximal et au moins égal au seuil. Si le nombre d'occurences maximal est inférieur au seuil , un vecteur vide est retourné.
        // Par exemple, si V est [3,4,5,5,5,6,6,8,8,8,12,16,16,20]
        // si seuil<=3 alors le résultat est [5,8].
        // si le seuil>3 alors le résultat est []}
        int i = 0,max = 0;
        ArrayList<Integer> vecteurentier = new ArrayList<>();
        while( i < v.size()){
            int valeurActuelle = v.get(i);
            int compt = 0;
            while(i < v.size() && v.get(i) == valeurActuelle){
                compt++;
                i++;
            }
            if (compt > max) {
                max = compt;
                vecteurentier.clear();
                vecteurentier.add(valeurActuelle);
            } else if (compt == max && compt > 0) {
                vecteurentier.add(valeurActuelle);
            }
        }
        if (max < seuil) {
            return new ArrayList<>();
        }
        return vecteurentier;
    }

    static ArrayList<Integer> fusion(ArrayList<Integer> v1, ArrayList<Integer> v2) {
        //{v1 et v2 triés}=>{résultat = vecteur trié fusionnant v1 et v2 sans supprimer les répétitions
        // par exemple si v1 est [4,8,8,10,25] et v2 est [5,8,9,25]
        // le résultat est [4,5,8,8,8,9,10,25,25]}
        ArrayList<Integer> res = new ArrayList<>();
        int i1 = 0;
        int i2 = 0;

        while (i1 < v1.size() && i2 < v2.size()) {
            if (v1.get(i1) <= v2.get(i2)) {
                res.add(v1.get(i1));
                i1++;
            } else {
                res.add(v2.get(i2));
                i2++;
            }
        }
        while (i1 < v1.size()) {
            res.add(v1.get(i1));
            i1++;
        }
        while (i2 < v2.size()) {
            res.add(v2.get(i2));
            i2++;
        }
        return res;
    }


    static String calculForme(String chaine, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = la concaténation des NBMOTS_FORME premiers mots-outils de chaine séparés par des blancs
        // remarque 1 : utilise decoupeMots et existeChaineDicho
        // remarque 2 : la limitation de la taille des formes permet d'accepter des réponses terminant par des précisions }
        ArrayList<String> mots = decoupeEnMots(chaine);
        String forme = "";
        int nbMots = 0;
        for (int i = 0; i < mots.size() && nbMots < NBMOTS_FORME; i++) {
            String motOriginal = mots.get(i);
            String motATraiter;
            if (estUnNombre(motOriginal)) {
                motATraiter = "NUM";
            } else {
                motATraiter = thesaurus.rechercherSortiePourEntree(motOriginal);
            }
            if (existeChaineDicho(motsOutils, motATraiter)) {
                if (!forme.equals("")) {
                    forme += " ";
                }
                forme += motATraiter;
                nbMots++;
            }
        }

        return forme;
    }


    static public ArrayList<String> constructionTableFormes(ArrayList<String> reponses, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = le vecteur de toutes les formes de réponses dans reponses.
        // remarque : utilise calculForme et existeChaine }
        ArrayList<String> formes = new ArrayList<>();
        for (int i = 0; i < reponses.size(); i++) {
            String f = calculForme(reponses.get(i), motsOutils,thesaurus);
            if (rechercherChaine(formes, f) == -1) {
                formes.add(f);
            }
        }
        return formes;
    }

    static public Index constructionIndexFormes(ArrayList<String> questionsReponses, ArrayList<String> formes, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        // {}=>{résultat = un index dont les entrées sont les "mots-outils positionnés" des questions (ex: "qui_0")
        // et les sorties sont les indices (dans formes) des formes de réponses répondant aux questions.
        // remarque 1 : utilise calculForme, rechercherChaine, decoupeEnMots, existeChaineDicho et ajouterSortieAEntree
        // remarque 2 : utilisez les méthodes indexOf et substring de String pour décomposer la question-réponse en question et réponse
        // remarque 3 : seuls les NBMOTS_FORME premiers mots-outils de la question sont pris en compte}
        Index index = new Index();
        for (int i = 0; i < questionsReponses.size(); i++) {
            String ligne = questionsReponses.get(i);
            int indSeparateur = ligne.indexOf("?");
            if (indSeparateur != -1) {
                String question = ligne.substring(0, indSeparateur);
                String reponseStr = ligne.substring(indSeparateur + 1);
                // CHANGEMENT : on passe le thésaurus à calculForme pour la réponse
                String formeDeLaReponse = calculForme(reponseStr, motsOutils, thesaurus);
                int indiceForme = rechercherChaine(formes, formeDeLaReponse);
                if (indiceForme != -1) {
                    ArrayList<String> motsQuestion = decoupeEnMots(question);
                    int nbMotsOutilsTrouves = 0;
                    for (int j = 0; j < motsQuestion.size() && nbMotsOutilsTrouves < NBMOTS_FORME; j++) {
                        String mot = motsQuestion.get(j).toLowerCase();
                        String motATraiter;
                        if (estUnNombre(mot)) {
                            motATraiter = "NUM";
                        } else {
                            motATraiter = thesaurus.rechercherSortiePourEntree(mot);
                        }
                        if (existeChaineDicho(motsOutils, motATraiter)) {
                            String entree = motATraiter + "_" + nbMotsOutilsTrouves;
                            index.ajouterSortieAEntree(entree, indiceForme);
                            nbMotsOutilsTrouves++;
                        }
                    }
                }
            }
        }
        return index;
    }

    static public ArrayList<Integer> constructionReponsesCandidates(String question, Index IndexReponses, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = vecteur des identifiants de réponses contenant l'ensemble des mots non outils de la question.
        // remarque 1 : utilise decoupeEnMots, existeChaineDicho, rechercherSorties, fusion et maxOccurences
        // remarque 2 : maxOccurences est appelé en passant le nombre de mots non outils de la question comme valeur de seuil.
        // remarque 3 : on aurait pu calculer directement une intersection au lieu d'une fusion et se passer de maxOccurences mais on
        // souhaite pouvoir garder la possibilité d'assouplir par la suite la contrainte sur la présence de l'intégralité
        // des mots de la question dans la réponse }
        ArrayList<String> questiondecoupe = decoupeEnMots(question);
        ArrayList<Integer> vecteurdereponses = new ArrayList<>();
        int i = 0,compt = 0;
        while(i < questiondecoupe.size()){
            String motCanonique = thesaurus.rechercherSortiePourEntree(questiondecoupe.get(i));
            if (!existeChaineDicho(motsOutils, motCanonique)){
                compt++;
                ArrayList<Integer> sorties = IndexReponses.rechercherSorties(motCanonique);
                if (sorties != null) {
                    vecteurdereponses = fusion(vecteurdereponses, sorties);
                }
            }
            i++;
        }
        return maxOccurences(vecteurdereponses,compt);
    }


    static public boolean estUnNombre(String s) {
        //{s est non vide}=>{résultat = true si s ne contient que des caractères représentant des chiffres (>='0'&<='9') et false sinon}
        int i = 0;
        while (i < s.length() && Character.isDigit(s.charAt(i))) {
            i++;
        }
        return i == s.length();
    }

    static public ArrayList<Integer> selectionReponsesCandidates(String question, ArrayList<Integer> candidates, Index IndexFormes,
                                                                 ArrayList<String> reponses, ArrayList<String> formesReponses, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = vecteur des identifiants de réponses (parmi les candidates) dont la forme est cohérente
        // avec la question.
        // remarque 1 : utilise decoupeEnMots, existeChaineDicho, rechercherSorties, fusion, maxOccurences, calculForme
        // remarque 2 : l'algorithme procède en 2 temps. D'abord il trouve les formes de réponses qui répondent à la question.
        // puis ajoute au résultat l'identifiant des réponses candidates qui respectent au moins une de ces formes.
        // remarque 3 : pour trouver les formes de réponses qui répondent à la question, on utilise l'index des formes, et on sélectionne
        // en appelant maxOccurences (avec seuil = nombre des mots-outils de la question) celles associées dans l'index à tous les mots-outils de la question.
        // remarque 4 : seuls les NBMOTS_FORME premiers mots-outils de la question sont pris en compte}
        ArrayList<String> motsQuestion = decoupeEnMots(question);
        ArrayList<Integer> formesPossiblesFusionnees = new ArrayList<>();
        int nbMotsOutilsQuestion = 0;

        for (int i = 0; i < motsQuestion.size() && nbMotsOutilsQuestion < NBMOTS_FORME; i++) {
            String motOriginal = motsQuestion.get(i);
            String motATraiter;
            if (estUnNombre(motOriginal)) {
                motATraiter = "NUM";
            } else {
                motATraiter = thesaurus.rechercherSortiePourEntree(motOriginal);
            }

            if (existeChaineDicho(motsOutils, motATraiter)) {
                String entree = motATraiter + "_" + nbMotsOutilsQuestion; // CHANGEMENT
                ArrayList<Integer> sorties = IndexFormes.rechercherSorties(entree);
                formesPossiblesFusionnees = fusion(formesPossiblesFusionnees, sorties);
                nbMotsOutilsQuestion++;
            }
        }
        ArrayList<Integer> formesSelectionnees = maxOccurences(formesPossiblesFusionnees, nbMotsOutilsQuestion);
        ArrayList<Integer> resultat = new ArrayList<>();
        for (Integer idReponse : candidates) {
            String texteReponse = reponses.get(idReponse);
            String formeDeCetteReponse = calculForme(texteReponse, motsOutils, thesaurus);
            int indiceFormeRep = rechercherChaine(formesReponses, formeDeCetteReponse);
            if (formesSelectionnees.contains(indiceFormeRep)) {
                resultat.add(idReponse);
            }
        }

        return resultat;
    }


    static public boolean reponseExiste(String reponse, Index indexReponses, ArrayList<String> reponses, ArrayList<String> motsOutils, Thesaurus thesaurus){
        //{}→{résultat = true si la reponse est présente dans reponses et false sinon.
        // Remarque 1 : utilise decoupeEnMots, rechercherSortiePourEntree, existeChaineDicho, rechercherSorties, fusion, maxOccurences
        // remarque 2 : Le vecteur reponses n'est pas trié. Afin d'éviter le coûteux parcours séquentiel du
        // vecteur, on utilise indexRéponses pour trouver les réponses contenant tous les mots non-outils de la
        // reponse, puis on vérifie si l'une d'entre elle est identique à reponse.}

        ArrayList<String> motsReponse = decoupeEnMots(reponse);
        ArrayList<Integer> candidats = new ArrayList<>();
        int nbMotsNonOutils = 0;

        int i = 0;
        while (i < motsReponse.size()) {
            String mot = motsReponse.get(i);
            String motCanonique = thesaurus.rechercherSortiePourEntree(mot);
            if (!existeChaineDicho(motsOutils, motCanonique)) {
                nbMotsNonOutils++;
                ArrayList<Integer> sorties = indexReponses.rechercherSorties(motCanonique);
                if (!sorties.isEmpty()) {
                    candidats = fusion(candidats, sorties);
                }
            }
            i++;
        }

        ArrayList<Integer> reponsesPossibles = maxOccurences(candidats, nbMotsNonOutils);
        int j = 0;
        while (j < reponsesPossibles.size() && !reponses.get(reponsesPossibles.get(j)).equalsIgnoreCase(reponse)) {
            j++;
        }
        return j < reponsesPossibles.size();
    }


    static public boolean formeQuestionReponseExiste(String question, String reponse, Index indexFormes, ArrayList<String> formesReponses, ArrayList<String> motsOutils, Thesaurus thesaurus) {
        //{}=>{résultat = * true si la forme de reponse est présente dans formesReponses
        // et qu'elle est accessible à partir des mots de la question en utilisant indexFormes.
        //                * false sinon.
        // remarque 1 : utilise decoupeEnMots, rechercherSortiePourEntree, existeChaineDicho, rechercherSorties, fusion, maxOccurences, calculForme
        // remarque 2 : Le vecteur formesReponses n'est pas trié. Afin d'éviter le coûteux parcours séquentiel du
        // vecteur, et afin de vérifier l'accessibilité à partir des mots de la question en utilisant indexFormes,
        // on utilise indexFormes pour trouver les formes indexées par les mots-outils de la
        // question, puis on vérifie si l'une de ces formes est identique à la forme de reponse.
        // remarque 3 : seuls les NBMOTS_FORME premiers mots-outils de question sont pris en compte}

        String formeReponseCherchee = calculForme(reponse, motsOutils, thesaurus);
        int indiceFormeCherchee = rechercherChaine(formesReponses, formeReponseCherchee);
        if (indiceFormeCherchee == -1) {
            return false;
        }
        ArrayList<String> motsQuestion = decoupeEnMots(question);
        ArrayList<Integer> formesPossiblesFusionnees = new ArrayList<>();
        int nbMotsOutilsQuestion = 0;

        int i = 0;
        while (i < motsQuestion.size() && nbMotsOutilsQuestion < NBMOTS_FORME) {
            String mot = motsQuestion.get(i).toLowerCase();
            String motATraiter;
            if (estUnNombre(mot)) {
                motATraiter = "NUM";
            } else {
                motATraiter = thesaurus.rechercherSortiePourEntree(mot);
            }
            if (existeChaineDicho(motsOutils, motATraiter)) {
                String entree = motATraiter + "_" + nbMotsOutilsQuestion;
                ArrayList<Integer> sorties = indexFormes.rechercherSorties(entree);
                formesPossiblesFusionnees = fusion(formesPossiblesFusionnees, sorties);
                nbMotsOutilsQuestion++;
            }
            i++;
        }
        ArrayList<Integer> formesSelectionnees = maxOccurences(formesPossiblesFusionnees, nbMotsOutilsQuestion);
        int j = 0;
        while (j < formesSelectionnees.size() && !formesSelectionnees.get(j).equals(indiceFormeCherchee)) {
            j++;
        }
        return j < formesSelectionnees.size();
    }
}