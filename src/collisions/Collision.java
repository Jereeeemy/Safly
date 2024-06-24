package collisions;

/**
 * Classe représentant une collision entre un vol et un autre.
 */
public class Collision {
    /**
     * 1er vol entrant en collision
     */
    Vol vol1;

    /**
     * 1er vol entrant en collision
     */
    Vol vol2;

    /**
     * Constructeur pour créer une collision.
     * @param vol1 Premier vol subissant la collision.
     * @param vol2 Deuxième vol subissant la collision.
     */
    public Collision(Vol vol1, Vol vol2) {
        this.vol1 = vol1;
        this.vol2 = vol2;
    }

    public Vol getVol1() {
        return vol1;
    }

    public void setVol1(Vol vol1) {
        this.vol1 = vol1;
    }

    public Vol getVol2() {
        return vol2;
    }

    public void setVol2(Vol vol2) {
        this.vol2 = vol2;
    }
}
