import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Thesaurus {

    private class EntreeSortie implements Comparable<EntreeSortie> {
        private String entree; // un mot
        private String sortie; // sa forme canonique

        public EntreeSortie(String entree, String sortie) {
            this.entree = entree;
            this.sortie = sortie;
        }

        public int compareTo(EntreeSortie o) {
            return this.entree.compareTo(o.entree);
        }
    }

    private ArrayList<EntreeSortie> table;

    public Thesaurus(String nomFichier) {
        //{}=>{ constructeur créant et initialisant l'attribut table à partir du contenu du fichier dont le nom est passé en paramètre, puis triant la table
        // en utilisant la méthode compareTo d'EntreeSortie
        // remarque 1 : utilise ajouterEntreeSortie et trierEntreesSorties
        // remarque 2 : pour la lecture du fichier, inspirez-vous de lireMotsOutils de Utilitaire
        // remarque 3 : pour les traitements de la chaîne lue, utilisez les méthodes indexOf,substring de String
        table = new ArrayList<>();
        try {
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                int indseparateur = ligne.indexOf(":");
                ajouterEntreeSortie(ligne.substring(0,indseparateur).trim(),ligne.substring(indseparateur+1).trim());
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        trierEntreesSorties(table);
    }

    public void ajouterEntreeSortie(String entree, String sortie) {
        //{}=>{ajoute à la fin de la table une nouvelle EntreeSortie avec les attributs entree et sortie}
        EntreeSortie nouvelleentreesortie = new EntreeSortie(entree,sortie);
        table.add(nouvelleentreesortie);
    }


    public String rechercherSortiePourEntree(String entree) {
        // {l'attribut table du thesaurus est trié sur l'attribut entree des Entree-Sortie}=>
        // {résultat = la forme canonique associée à entree dans le thésaurus si l'entrée entree existe,
        // entree elle-même si elle n'existe pas. La recherche doit être dichotomique.
        // remarque : utilise compareTo de EntreeSortie }
        if (table.isEmpty()) {
            return entree;
        } else if (table.getLast().entree.compareTo(entree) < 0) {
            return entree;
        } else {
            int inf = 0, sup = table.size() - 1;
            while (inf < sup) {
                int m = (inf + sup) / 2;
                if (table.get(m).entree.compareTo(entree) >= 0) {
                    sup = m;
                } else {
                    inf = m + 1;
                }
            }
            if (table.get(sup).entree.compareTo(entree) == 0) {
                return table.get(sup).sortie;
            } else {
                return entree;
            }
        }
    }

    static void trierEntreesSorties(ArrayList<EntreeSortie> v) {
        //{} => {trie v sur la base de la méthode compareTo de EntreeSortie}
        int i = 0;
        int j;
        boolean onApermute = true;
        while (onApermute) {
            j = v.size() - 1;
            onApermute = false;
            while ( i < j ){
                if( v.get(j).compareTo(v.get(j-1)) < 0 ){
                    EntreeSortie temp = v.get(j);
                    v.set(j,v.get(j-1));
                    v.set(j-1,temp);
                    onApermute = true;
                }
                j--;
            }
            i++;
        }
    }
}
// Note additionnelle : lors de la création du fichier thesaurus,
// nous avons également pensé à pallier un problème rencontré lors de la saisie des prompts,
// à savoir les fautes de frappe. En effet, la plupart des gens ne relisent pas leur prompt,
// nous avons donc ajouté un ensemble de synonymes et de mots écrits avec des fautes,
// que nous avons associés aux mots corrects. Par exemple : "salut : salu".
// Pour ce faire, nous avons identifié grâce à une recherche google, les 300 fautes les plus courantes et avons essayé d’y remédier dans un souci d’optimalité.
