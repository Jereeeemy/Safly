package tests;

import collisions.Carte;
import collisions.ExceptionNoFlight;
import collisions.ExceptionOrientation;

import coloration.Couleur.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import coloration.Couleur.*;

import coloration.WelshPowell;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;




public class TestCollisionAvecChoix {
    public static Carte test;

    static {
        try {
            test = new Carte();
        }
        catch (IOException | ExceptionNoFlight | ExceptionOrientation e) {
            System.out.println(e.getMessage());
        }
        catch (InputMismatchException e) {
            System.out.println("Saisie incorrecte");
        }

    }

    public static void main(String[] args) {
        //MenuPrincipal fenetrePrincipale = new MenuPrincipal();
        //fenetrePrincipale.setVisible(true);

        WelshPowell algowelsh = new WelshPowell(test.getGraph_vol());
        algowelsh.colorierNoeudsWelsh(2);
        int conflits = algowelsh.CompterConflits(test.getGraph_vol());
        test.getGraph_vol().display();

    }





}