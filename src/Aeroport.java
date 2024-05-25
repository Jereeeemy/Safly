/**
 * Classe représentant un aéroport avec son code, la ville où il est situé, et ses coordonnées.
 */
public class Aeroport {
    String code;
    String ville;
    double x;
    double y;

    /**
     * Constructeur pour créer un aéroport.
     * @param code Code de l'aéroport.
     * @param ville Ville où se trouve l'aéroport.
     * @param x Coordonnée x de l'aéroport.
     * @param y Coordonnée y de l'aéroport.
     */
    public Aeroport(String code, String ville, double x, double y) {
        this.code = code;
        this.ville = ville;
        this.x = x;
        this.y = y;
    }

    // Getters et setters pour les attributs de la classe Aeroport.

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public double getx() {
        return x;
    }

    public void setx(double x) {
        this.x = x;
    }

    public double gety() {
        return y;
    }

    public void sety(double y) {
        this.y = y;
    }
}
