import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;


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
        ArrayList<Collision> collisions = test.RechercheCollision();
        System.out.println(collisions.size());
        System.out.println(collisions);
        test.getGraph_vol().display();


    }
}
