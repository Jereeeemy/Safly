package gestioncollisions;

/**
 * Exception levée lorsque aucun fichier de vol n'est disponible dans le répertoire.
 */
public class ExceptionNoFlight extends Exception {
    public ExceptionNoFlight() {
        super("Il n'y a aucun fichier de vol disponible");
    }
}
