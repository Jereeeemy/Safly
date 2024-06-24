package tests;


import coloration.WelshPowell;
import graphvol.CreateurGraph;
import graphvol.ExceptionFormatIncorrect;
import graphvol.ExceptionLigneIncorrect;
import graphvol.ExceptionNoGraphVol;

import coloration.DsaturAlgorithm;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;



public class TestGraphVol {
    public static void main(String[] args) throws IOException, ExceptionNoGraphVol, ExceptionFormatIncorrect, ExceptionLigneIncorrect {
        CreateurGraph test = new CreateurGraph();
        File chemin = test.ChoixGraph();
        test = new CreateurGraph(chemin);

        // Demander à l'utilisateur d'entrer la valeur de Kmax
        Scanner scanner = new Scanner(System.in);
        System.out.print("Veuillez entrer la valeur de Kmax : ");
        int kmax = scanner.nextInt();

        // Appliquer l'algorithme DSATUR avec la valeur de Kmax choisie
        WelshPowell algowelsh = new WelshPowell(test.getGraph());
        algowelsh.colorierNoeudsWelsh(kmax);
        int nb_conf = test.CompterConflits(test.getGraph());
        System.out.println(nb_conf);

    }
}
