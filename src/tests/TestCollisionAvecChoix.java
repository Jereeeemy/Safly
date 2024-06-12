package tests;

import applicationihm.MenuPrincipal;
import collisions.Carte;
import collisions.Collision;
import collisions.ExceptionNoFlight;
import collisions.ExceptionOrientation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import welshpowell.WelshPowell;

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
        MenuPrincipal fenetrePrincipale = new MenuPrincipal();
        fenetrePrincipale.setVisible(true);
        ArrayList<Collision> collisions = test.RechercheCollision();
        System.out.println(collisions.size());
        System.out.println(collisions);


        test.getGraph_vol().display();

        WelshPowell algowelsh = new WelshPowell(test.getGraph_vol());
        System.out.println(algowelsh.getNoeuds());
    }
}