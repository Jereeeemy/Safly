package applicationihm;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * La classe GraphInfoTable gère un tableau affichant des informations sur un graphe.
 */
public class GraphInfoTable {
    /**
     * Tableau pour afficher les informations du graph
     */
    private JTable tableauInfoGraphe;

    /**
     * Modèle de données pour le tableau
     */
    private DefaultTableModel model;

    /**
     * Constructeur de la classe GraphInfoTable.
     * Initialise le tableau avec des valeurs par défaut.
     */
    public GraphInfoTable() {
        initializeTable();
    }

    /**
     * Initialise le tableau avec des valeurs par défaut.
     */
    private void initializeTable() {
        model = new DefaultTableModel(
                new Object[][]{
                        {"Informations : ", null},
                        {null, null},
                        {"Noeuds :", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                        {"Arêtes :", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                        {"Degré Moyen :", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                        {"Nb Composantes :", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                        {"Diamètre", " " + " " + 0}, // Valeurs initiales
                        {"", null},
                        {"Nb conflits :", " " + " " + 0}
                },
                new String[]{"Informations Graphe :", null}
        );

        tableauInfoGraphe = new JTable(model);

        // Partie à changer avec les statistiques obtenues de la coloration
        // updateButton.addActionListener(e -> updateTableData(10, 20, 2.5, 3, 15)); // Exemples de nouvelles valeurs
    }

    /**
     * Met à jour les données du tableau avec les nouvelles valeurs fournies.
     *
     * @param noeuds      Le nombre de noeuds dans le graphe
     * @param aretes      Le nombre d'arêtes dans le graphe
     * @param degre       Le degré moyen des noeuds dans le graphe
     * @param composantes Le nombre de composantes connexes dans le graphe
     * @param diametre    Le diamètre du graphe
     */
    public void updateTableData(int noeuds, int aretes, double degre, int composantes, int diametre) {
        model.setValueAt(" " + " " + noeuds, 2, 1);
        model.setValueAt(" " + " " + aretes, 4, 1);
        model.setValueAt(" " + " " + degre, 6, 1);
        model.setValueAt(" " + " " + composantes, 8, 1);
        model.setValueAt(" " + " " + diametre, 10, 1);
    }
}