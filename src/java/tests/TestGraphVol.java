package java.tests;

import java.graphvol.CreateurGraph;
import java.graphvol.ExceptionNoGraphVol;

import java.io.File;
import java.io.IOException;



public class TestGraphVol {
    public static void main(String[] args) throws IOException, ExceptionNoGraphVol {
        File fichier = new File("data/graph-test3.txt");
        CreateurGraph test = new CreateurGraph(fichier);
        System.out.println(test.getGraph().getEdgeCount());
    }
}
