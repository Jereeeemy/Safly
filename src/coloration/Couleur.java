package coloration;

class Couleur {
    private String couleur;
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

    public void incrementCount() {
        this.count++;
    }
}

