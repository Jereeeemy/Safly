package tests;


import graphvol.CreateurGraph;
import graphvol.ExceptionNoGraphVol;

import dsature.DsaturAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TestGraphVol {
    public static void main(String[] args) throws IOException, ExceptionNoGraphVol {
        CreateurGraph test = new CreateurGraph();
        File chemin = test.ChoixGraph();
        test = new CreateurGraph(chemin);

        // Demander Ã  l'utilisateur d'entrer la valeur de Kmax
        Scanner scanner = new Scanner(System.in);
        System.out.print("Veuillez entrer la valeur de Kmax : ");
        int kmax = scanner.nextInt();

        // Appliquer l'algorithme DSATUR avec la valeur de Kmax choisie
        DsaturAlgorithm dsatur = new DsaturAlgorithm();
        dsatur.colorAndDisplayGraph(test.getGraph(), kmax);
    }
}
