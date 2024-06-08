package java.collisions;

/**
 * Exception levée lorsque l'orientation n'est ni "N" ni "S" ni "O" ni "E"
 */
public class ExceptionOrientation extends Exception {
    public ExceptionOrientation(String orientation, String nom_ville) {
        super("Orientation invalide dans le fichier pour "+ nom_ville + " : \"" + orientation + "\" n'est pas une orientation valide");
    }
}
