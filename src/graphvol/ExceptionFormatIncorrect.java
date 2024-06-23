package graphvol;

/**
 * Exception levée lorsque aucun fichier graph-test n'est disponible dans le répertoire.
 */
public class ExceptionFormatIncorrect extends Exception {
    public ExceptionFormatIncorrect() {
        super("Le fichier n'est pas au bon format");
    }
}
