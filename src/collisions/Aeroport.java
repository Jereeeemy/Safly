package collisions;


/**
 * Classe représentant un aéroport avec son code, la ville où il est situé, et ses coordonnées.
 */
public class Aeroport {
    /**
     * Code unique de l'Aéroport
     */
    String code;

    /**
     * Ville où se situe l'Aéroport
     */
    String ville;

    /**
     * 1re valeur des coordonnées cartésiennes de l'aéroport
     */
    double x;

    /**
     * 2e valeur des coordonnées cartésiennes de l'aéroport
     */
    double y;

    /**
     * 1re valeur des coordonnées géographiques de l'aéroport
     */
    double longitude;

    /**
     * 2e valeur des coordonnées géographiques de l'aéroport
     */
    double latitude;

    /**
     * Constructeur pour créer un aéroport avec uniquement les coordonnées cartésiennes.
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

    /**
     * Constructeur pour créer un aéroport avec les coordonnées cartésiennes et géographiques.
     * @param code Code de l'aéroport.
     * @param ville Ville où se trouve l'aéroport.
     * @param x Coordonnée x de l'aéroport.
     * @param y Coordonnée y de l'aéroport.
     */
    public Aeroport(String code, String ville, double x, double y, double latitude, double longitude) {
        this.code = code;
        this.ville = ville;
        this.x = x;
        this.y = y;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}