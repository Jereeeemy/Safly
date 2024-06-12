package applicationihm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class GraphInfoTable {
    private JTable tableauInfoGraphe;
    private DefaultTableModel model;

    public GraphInfoTable() {
        initializeTable();
    }

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
                },
                new String[]{"Informations Graphe :", null}
        );

        tableauInfoGraphe = new JTable(model);

        // partie a changer avec les stat obtenue de la coloration
        //updateButton.addActionListener(e -> updateTableData(10, 20, 2.5, 3, 15)); // Exemples de nouvelles valeurs

    }

    // Méthode pour mettre à jour les données du tableau
    public void updateTableData(int noeuds, int aretes, double degre, int composantes, int diametre) {
        model.setValueAt(" " + " " + noeuds, 2, 1);
        model.setValueAt(" " + " " + aretes, 4, 1);
        model.setValueAt(" " + " " + degre, 6, 1);
        model.setValueAt(" " + " " + composantes, 8, 1);
        model.setValueAt(" " + " " + diametre, 10, 1);
    }
}