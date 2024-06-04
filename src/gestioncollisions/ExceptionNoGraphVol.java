package gestioncollisions;

/**
 * Exception levée lorsque aucun fichier graph-test n'est disponible dans le répertoire.
 */
public class ExceptionNoGraphVol extends Exception {
    public ExceptionNoGraphVol() {
        super("Il n'y a aucun fichier graph-test disponible");
    }
}
