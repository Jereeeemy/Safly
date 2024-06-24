package coloration;

/**
 * Class représentant une Couleur
 */
public class Couleur {
    /**
     * Couleur représentée
     */
    private String couleur;

    /**
     * Nombre de fois où la couleur est utilisée dans un graph
     */
    private int count;

    public Couleur(String color) {
        this.couleur = color;
        this.count = 1;
    }

    public Couleur() {
    }

    public String getCouleur() {
        return couleur;
    }

    public int getCount() {
        return count;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    /**
     * Ajoute 1 au compte d'utilisation de la couleur
     */
    public void incrementCount() {
        this.count++;
    }
}

