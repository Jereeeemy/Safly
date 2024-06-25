package tests;

import collisions.Aeroport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Classe pour tester la classe Aeroport.
 */
public class TestAeroport {

    @Test
    public void testConstructeurCartesien() {
        Aeroport aeroport1 = new Aeroport("CDG", "Paris", 2.55, 49.01);

        assertEquals("CDG", aeroport1.getCode());
        assertEquals("Paris", aeroport1.getVille());
        assertEquals(2.55, aeroport1.getx(), 0.001);
        assertEquals(49.01, aeroport1.gety(), 0.001);
    }

    @Test
    public void testConstructeurGeographique() {
        Aeroport aeroport2 = new Aeroport("LHR", "Londres", 0.4543, 51.4700, 51.4700, -0.4543);

        assertEquals("LHR", aeroport2.getCode());
        assertEquals("Londres", aeroport2.getVille());
        assertEquals(0.4543, aeroport2.getx(), 0.001);
        assertEquals(51.4700, aeroport2.gety(), 0.001);
        assertEquals(51.4700, aeroport2.getLatitude(), 0.001);
        assertEquals(-0.4543, aeroport2.getLongitude(), 0.001);
    }

    @Test
    public void testSetters() {
        Aeroport aeroport1 = new Aeroport("CDG", "Paris", 2.55, 49.01);

        aeroport1.setCode("JFK");
        aeroport1.setVille("New York");
        aeroport1.setx(-73.7781);
        aeroport1.sety(40.6413);

        assertEquals("JFK", aeroport1.getCode());
        assertEquals("New York", aeroport1.getVille());
        assertEquals(-73.7781, aeroport1.getx(), 0.001);
        assertEquals(40.6413, aeroport1.gety(), 0.001);
    }
}