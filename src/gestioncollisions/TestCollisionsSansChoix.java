package gestioncollisions;


public class TestCollisionsSansChoix {
    public static Carte vol_test0;
    public static Carte vol_test1;
    public static Carte vol_test2;
    public static Carte vol_test3;
    public static Carte vol_test4;
    public static Carte vol_test5;
    public static Carte vol_test6;
    public static Carte vol_test7;
    public static Carte vol_test8;
    public static Carte vol_test9;


    /**
    public static void main(String[] args) {

        boolean erreur = false;


        try {
            vol_test0 = new gestioncollisions.Carte("D:/JAVA/SAE_JAVA_2024/data/vol-test0.csv");
            vol_test1 = new gestioncollisions.Carte("D:/JAVA/SAE_JAVA_2024/data/vol-test1.csv");
            vol_test2 = new gestioncollisions.Carte("D:/JAVA/SAE_JAVA_2024/data/vol-test2.csv");
            vol_test3 = new gestioncollisions.Carte("D:/JAVA/SAE_JAVA_2024/data/vol-test3.csv");
            vol_test4 = new gestioncollisions.Carte("D:/JAVA/SAE_JAVA_2024/data/vol-test4.csv");
            vol_test5 = new gestioncollisions.Carte("D:/JAVA/SAE_JAVA_2024/data/vol-test5.csv");
            vol_test6 = new gestioncollisions.Carte("D:/JAVA/SAE_JAVA_2024/data/vol-test6.csv");
            vol_test7 = new gestioncollisions.Carte("D:/JAVA/SAE_JAVA_2024/data/vol-test7.csv");
            vol_test8 = new gestioncollisions.Carte("D:/JAVA/SAE_JAVA_2024/data/vol-test8.csv");
            vol_test9 = new gestioncollisions.Carte("D:/JAVA/SAE_JAVA_2024/data/vol-test9.csv");

        }
        catch (IOException | gestioncollisions.ExceptionNoFlight | gestioncollisions.ExceptionOrientation e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        catch (InputMismatchException e) {
            System.out.println("Saisie incorrecte");
            System.exit(1);
        }

        try{
            assert vol_test0.RechercheCollision().size()==1;
            assert vol_test1.RechercheCollision().isEmpty();
            assert vol_test2.RechercheCollision().size()==5;
            assert vol_test3.RechercheCollision().size()==17;
            assert (vol_test4.RechercheCollision().size()==35||vol_test4.RechercheCollision().size()==36);
            assert vol_test5.RechercheCollision().size()==23;
            assert vol_test6.RechercheCollision().size()==426;
            assert vol_test7.RechercheCollision().size()==593;
            assert (vol_test8.RechercheCollision().size()==1298||vol_test8.RechercheCollision().size()==1299);
            assert vol_test9.RechercheCollision().size()==933;
        }
        catch (AssertionError e){
            erreur = true;
        }

        if (erreur){
            System.exit(1);
        }

    }**/
}
