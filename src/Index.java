import java.util.ArrayList;

public class Index {

    // un index est un vecteur d'EntreeIndex
    private class EntreeIndex {

        // une EntreeIndex associe un vecteur trié d'entiers (sorties) à une String (entree)
        private String entree;
        private ArrayList<Integer> sorties;

        public ArrayList<Integer> getSorties() {
            return sorties;
        }


        //constructeur
        public EntreeIndex(String entree) {
            this.entree = entree;
            sorties = new ArrayList<>();
        }


        public int rechercherSortie(Integer sortie) {
            //{}=>{recherche dichotomique de sortie dans sorties (triée dans l'ordre croissant)
            // résultat = l'indice de sortie dans sorties si trouvé, - l'indice d'insertion si non trouvé }
            int debut = 0;
            int fin = sorties.size() - 1;

            while (debut <= fin) {
                int m = (debut + fin) / 2;
                int milieu = sorties.get(m);

                if (milieu == sortie) {
                    return m;
                } else if (milieu > sortie) {
                    fin = m - 1;
                } else {
                    debut = m + 1;
                }
            }
            return -debut;
        }


        public void ajouterSortie(Integer sortie) {
            //{}=>{insère sortie à la bonne place dans sorties (triée dans l'ordre croissant)
            // remarque : utilise rechercherSortie de EntreeIndex }
            int place = rechercherSortie(sortie);
            if (place < 0) {
                sorties.add(-place, sortie);
            } else {
                System.out.println(sortie + " Est deja present");
            }
        }


        @Override
        public String toString() {
            return entree + "=>" + sorties;
        }
    }

    //Un vecteur d'EntreeIndex trié sur l'attribut entree (String) des EntreeIndex
    private ArrayList<EntreeIndex> table;

    //constructeur
    public Index() {
        table = new ArrayList<>();
    }


    public int rechercherEntree(String entree) {
        //{}=>  {recherche dichotomique de entree dans table (triée dans l'ordre lexicographique des attributs entree des EntreeIndex) }
        //résultat =  l'indice de entree dans table si trouvé et -l'indice d'insertion sinon }
        int debut = 0;
        int fin = table.size() - 1;
        while (debut <= fin) {
            int m = (debut + fin) / 2;
            EntreeIndex milieu = table.get(m);
            int cmp = milieu.entree.compareTo(entree);
            if (cmp == 0) {
                return m;
            } else if (cmp > 0) {
                fin = m - 1;
            } else {
                debut = m + 1;
            }
        }

        return -debut;
    }


    public void ajouterSortieAEntree(String entree, Integer sortie) {
        // {}=>{ajoute l'entier sortie dans les sorties associées à l'entrée entree
        // si l'entrée entree n'existe pas elle est créée.
        // ne fait rien si sortie était déjà présente dans ses sorties.
        // remarque : utilise la fonction rechercherEntree de Index et la procedure ajouterSortie de EntreeIndex}
        int place = rechercherEntree(entree);
        EntreeIndex entreeIndex;

        if (place >= 0) {
            entreeIndex = table.get(place);
        } else {
            int indexInsertion = -(place + 1);

            entreeIndex = new EntreeIndex(entree);
            table.add(indexInsertion, entreeIndex);
        }
        entreeIndex.ajouterSortie(sortie);
    }

    public ArrayList<Integer> rechercherSorties(String entree) {
        // {}=>{résultat = les sorties associées à l'entrée entree
        // si l'entrée entree n'existe pas, une ArrayList vide est retournée.
        // remarque : utilise la fonction rechercherEntree de Index}
        int in = rechercherEntree(entree);
        EntreeIndex entreeIndex;
        if (in >= 0) {
            entreeIndex = table.get(in);
            return entreeIndex.getSorties();
        }
        return new ArrayList<Integer>();
    }
    public void afficher() {
        // {}=>{affiche la table de l'index}
        for (int i = 0; i < table.size(); i++) {
            System.out.println(this.table.get(i));
        }
    }
}
