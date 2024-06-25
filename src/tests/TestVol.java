package tests;

import collisions.Aeroport;
import collisions.Vol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Classe pour tester la classe Vol.
 */
public class TestVol {

    @Test
    public void testConstructeur() {
        Aeroport depart = new Aeroport("CDG", "Paris", 2.55, 49.01);
        Aeroport arrivee = new Aeroport("LHR", "Londres", 0.4543, 51.4700);

        Vol vol = new Vol("AF123", depart, arrivee, 14, 30, 90);

        assertEquals("AF123", vol.getCode());
        assertEquals(depart, vol.getDepart());
        assertEquals(arrivee, vol.getArrivee());
        assertEquals(14, vol.getHeure_depart(), 0.001);
        assertEquals(30, vol.getMinute_depart(), 0.001);
        assertEquals(16, vol.getHeure_arrivee(), 0.001);
        assertEquals(0, vol.getMinute_arrivee(), 0.001); // 15h60 est égal à 16h00
        assertEquals(90, vol.getTemps_vol(), 0.001);
    }

    @Test
    public void testSetters() {
        Aeroport depart = new Aeroport("CDG", "Paris", 2.55, 49.01);
        Aeroport arrivee = new Aeroport("LHR", "Londres", 0.4543, 51.4700);

        Vol vol = new Vol("AF123", depart, arrivee, 14, 30, 90);

        Aeroport nouveauDepart = new Aeroport("JFK", "New York", -73.7781, 40.6413);
        vol.setDepart(nouveauDepart);
        assertEquals(nouveauDepart, vol.getDepart());

        Aeroport nouvelleArrivee = new Aeroport("SFO", "San Francisco", -122.3790, 37.6213);
        vol.setArrivee(nouvelleArrivee);
        assertEquals(nouvelleArrivee, vol.getArrivee());

        vol.setHeure_depart(16);
        vol.setMinute_depart(45);
        vol.setTemps_vol(120);

        assertEquals(16, vol.getHeure_depart(), 0.001);
        assertEquals(45, vol.getMinute_depart(), 0.001);
        assertEquals(120, vol.getTemps_vol(), 0.001);

        double[] nouvellesHeures = vol.AdditionHeure_Minutes(16, 45, 120);
        vol.setHeure_arrivee(nouvellesHeures[0]);
        vol.setMinute_arrivee(nouvellesHeures[1]);

        assertEquals(18, vol.getHeure_arrivee(), 0.001);
        assertEquals(45, vol.getMinute_arrivee(), 0.001);
    }

    @Test
    public void testAdditionHeure_Minutes() {
        Vol vol = new Vol("AF123", null, null, 14, 30, 90);

        double[] resultat = vol.AdditionHeure_Minutes(14, 30, 90);
        assertEquals(16, resultat[0], 0.001);
        assertEquals(0, resultat[1], 0.001);

        resultat = vol.AdditionHeure_Minutes(23, 45, 90);
        assertEquals(25, resultat[0], 0.001);
        assertEquals(15, resultat[1], 0.001);
    }
}