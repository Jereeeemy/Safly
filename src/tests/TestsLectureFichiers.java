package tests;

import collisions.ExceptionNoFlight;
import collisions.ExceptionOrientation;
import org.junit.jupiter.api.Test;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import collisions.Carte;
import collisions.Vol;

import static org.junit.jupiter.api.Assertions.*;

public class TestsLectureFichiers {

    private static final String CHEMIN_TEST = "src/test/resources/";

    // Méthode pour créer un fichier de vols valide
    private void creerFichierVolsValide(String nomFichier) {
        String contenu = "AF000090;MRS;BES;7;33;81\n" +
                "AF000132;LYS;BOD;7;34;47\n";

        creerFichier(nomFichier, contenu);
    }

    // Méthode pour créer un fichier de vols avec erreurs
    private void creerFichierVolsAvecErreurs(String nomFichier) {
        String contenu = "AF000090;MRS;BES;7;33;81\n" +
                "AF000132;LYS;BOD;7;34\n" +  // Erreur: minuteDepart manquant
                "XYZ123;ABC;DEF;12;45;60\n";  // Erreur: aéroports non valides

        creerFichier(nomFichier, contenu);
    }

    // Méthode pour créer un fichier de vols vide
    private void creerFichierVolsVide(String nomFichier) {
        String contenu = "";

        creerFichier(nomFichier, contenu);
    }

    // Méthode générique pour créer un fichier avec un contenu donné
    private void creerFichier(String nomFichier, String contenu) {
        String chemin = CHEMIN_TEST + nomFichier;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(chemin))) {
            writer.write(contenu);
            System.out.println("Fichier créé : " + chemin);
        } catch (IOException e) {
            System.err.println("Erreur lors de la création du fichier : " + e.getMessage());
        }
    }

    @Test
    public void testLireVolsFichierValide() throws IOException, ExceptionNoFlight, ExceptionOrientation {
        creerFichierVolsValide("vols_valide.csv");
        File fichier = new File(CHEMIN_TEST + "vols_valide.csv");
        Carte map = new Carte();  // Carte à utiliser pour les tests
        ArrayList<Vol> vols = map.LireVols(fichier);

        // Vérifier que la liste n'est pas vide et contient le bon nombre de vols
        assertFalse(vols.isEmpty());
        assertEquals(2, vols.size());  // Remplacer par le nombre de vols attendu
    }

    @Test
    public void testLireVolsFichierAvecErreurs() throws IOException, ExceptionNoFlight, ExceptionOrientation {
        creerFichierVolsAvecErreurs("vols_avec_erreurs.csv");
        File fichier = new File(CHEMIN_TEST + "vols_avec_erreurs.csv");
        Carte map = new Carte();  // Carte à utiliser pour les tests
        ArrayList<Vol> vols = map.LireVols(fichier);

        // Vérifier que la liste ne contient que les vols valides
        assertEquals(2, vols.size());  // Remplacer par le nombre de vols valides attendu
    }

    @Test
    public void testLireVolsFichierVide() throws ExceptionOrientation, ExceptionNoFlight, IOException {
        creerFichierVolsVide("vols_vide.csv");
        File fichier = new File(CHEMIN_TEST + "vols_vide.csv");
        Carte map = new Carte();  // Carte à utiliser pour les tests

        // Vérifier qu'une IOException est levée avec le bon message
        IOException exception = assertThrows(IOException.class, () -> map.LireVols(fichier));
        assertTrue(exception.getMessage().contains("Le fichier de vols est vide"));
    }
}