package tests;

import graphvol.CreateurGraph;
import graphvol.ExceptionNoGraphVol;

import java.io.File;
import java.io.IOException;



public class TestGraphVol {
    public static void main(String[] args) throws IOException, ExceptionNoGraphVol {
        CreateurGraph test = new CreateurGraph();
        File chemin = test.ChoixGraph();
        test = new CreateurGraph(chemin);
        test.graph.display();
    }
}
