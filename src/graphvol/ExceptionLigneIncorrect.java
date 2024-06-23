package graphvol;

/**
 * Exception levée lorsque aucun fichier graph-test n'est disponible dans le répertoire.
 */
public class ExceptionLigneIncorrect extends Exception {
    public ExceptionLigneIncorrect(String ligne) {
        super("La ligne " + ligne + " n'est pas au bon format, elle n'a pas été prise en compte");
    }
}
