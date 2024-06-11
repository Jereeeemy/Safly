package java.collisions;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.abs;
import static java.lang.System.in;

public class Carte {
    private Graph graph_vol;
    private ArrayList<Aeroport> liste_aeroports;
    private ArrayList<Vol> liste_vols;
    private static int nb_aeroports;
    private static int nb_vols;
    public static int temps_collision = 15;
    public static int rayon_terre = 6371;
    /**
     * Constructeur par défaut pour initialiser la carte avec les aéroports et les vols.
     * @throws IOException en cas d'erreur de lecture de fichier.
     * @throws ExceptionNoFlight en cas d'absence de fichiers de vol disponibles.
     */
    public Carte() throws IOException, ExceptionNoFlight, ExceptionOrientation {
        graph_vol = new SingleGraph("Collisions.Carte");
        nb_aeroports = 0;
        nb_vols = 0;
        liste_aeroports = this.LireAeroports();
        liste_vols = this.LireVols();
    }

    /**
     * Constructeur pour initialiser la carte avec les aéroports et les vols depuis un fichier spécifique pour les vols.
     * @param fichier_vol Nom du fichier contenant les informations des vols.
     * @param fichier_aeroport Nom du fichier contenant les informations des aéroports.
     * @throws IOException en cas d'erreur de lecture de fichier.
     * @throws ExceptionNoFlight en cas d'absence de fichiers de vol disponibles.
     */
    public Carte(File fichier_aeroport,File fichier_vol) throws IOException, ExceptionNoFlight, ExceptionOrientation {
        graph_vol = new SingleGraph("Collisions.Carte"+fichier_vol.toString());
        nb_aeroports = 0;
        nb_vols = 0;
        liste_aeroports = this.LireAeroports(fichier_aeroport);
        liste_vols = this.LireVols(fichier_vol);
    }

    public Carte(File fichier_aeroport) throws IOException, ExceptionNoFlight, ExceptionOrientation {
        nb_aeroports = 0;
        nb_vols = 0;
        liste_aeroports = this.LireAeroports(fichier_aeroport);
    }

    // Getters pour les attributs de la classe Collisions.Carte.
    public Graph getGraph_vol() {
        return this.graph_vol;
    }

    public int getNb_aeroports() {
        return this.nb_aeroports;
    }

    public int getNb_vols() {
        return this.nb_vols;
    }

    public ArrayList<Aeroport> getListe_aeroports() {
        return liste_aeroports;
    }


    // Setters pour les listes des aéroports et des vols
    public void setListe_aeroports(File fichier_aeroport) throws ExceptionOrientation, IOException {
        this.liste_aeroports = this.LireAeroports(fichier_aeroport);
    }

    public void setListe_vols(File fichier_vols) throws ExceptionNoFlight, IOException {
        this.liste_vols = this.LireVols(fichier_vols);
    }


    /**
     * Calcule la valeur de la coordonnée (latitude ou longitude) en degrés.
     * @param nom_ville Nom de la ville dont on calcule les coordonnées
     * @param degree Degré de la coordonnée.
     * @param minute Minute de la coordonnée.
     * @param seconde Seconde de la coordonnée.
     * @param direction Direction de la coordonnée (N/S/E/O).
     * @return La valeur de la coordonnée en degrés.
     */
    private double CalculValeur(String nom_ville, String degree, String minute, String seconde, String direction) throws ExceptionOrientation {
        double coord = Double.parseDouble(degree)+Double.parseDouble(minute)/60.0+Double.parseDouble(seconde)/3600.0;
        direction=direction.toUpperCase();
        if (direction.equals("O")|| direction.equals("S")) {
            // Si on est à l'ouest ou au sud, la coordonnée sera négatif
            coord*=-1;
        }
        else if (!(direction.equals("E") || direction.equals("N"))) {
            // Si la direction n'est pas une de celles proposées, on renvoie une Exception
            throw new ExceptionOrientation(direction,nom_ville);
        }
        return coord;
    }

    /**
     * Lit les aéroports à partir d'un fichier par défaut et les ajoute à la liste des aéroports.
     * @return La liste des aéroports.
     * @throws IOException en cas d'erreur de lecture de fichier.
     */
    private ArrayList<Aeroport> LireAeroports() throws IOException, ExceptionOrientation {
        String nomfichier= "data/aeroports.txt";
        BufferedReader lecteur = new BufferedReader(new FileReader(nomfichier));
        String line;
        ArrayList<Aeroport> aeroports = new ArrayList<>();
        while ((line = lecteur.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, ";");
            String code=(st.nextToken());//Code de l'aéroport
            String nom_ville=(st.nextToken());//Nom de la ville où est présent l'aéroport

            double latitude=(CalculValeur(nom_ville, st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken()));//Latitude de l'aéroport

            double longitude=(CalculValeur(nom_ville, st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken()));//Longitude de l'aéroport

            // Conversion des coordonnées en coordonnées cartésiennes
            double y = rayon_terre*Math.cos(latitude*((Math.PI)/180))*Math.cos(longitude*((Math.PI)/180));
            double x = rayon_terre*Math.cos(latitude*((Math.PI)/180))*Math.sin(longitude*((Math.PI)/180));

            Aeroport aeroport = new Aeroport(code,nom_ville,x,y);
            aeroports.add(aeroport);
            nb_aeroports++;//Incrémentation du compteur d'aéroports
        }
        lecteur.close();
        return aeroports;
    }

    /**
     * Lit les aéroports à partir d'un fichier passé en parametre et les ajoute à la liste des aéroports.
     * @return La liste des aéroports.
     * @throws IOException en cas d'erreur de lecture de fichier.
     */
    private ArrayList<Aeroport> LireAeroports(File fichier_aeroports) throws IOException, ExceptionOrientation {
        BufferedReader lecteur = new BufferedReader(new FileReader(fichier_aeroports));
        String line;
        ArrayList<Aeroport> aeroports = new ArrayList<>();
        while ((line = lecteur.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line, ";");
            String code=(st.nextToken());//Code de l'aéroport
            String nom_ville=(st.nextToken());//Nom de la ville où est présent l'aéroport

            double latitude=(CalculValeur(nom_ville, st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken()));//Latitude de l'aéroport

            double longitude=(CalculValeur(nom_ville, st.nextToken(),st.nextToken(),st.nextToken(),st.nextToken()));//Longitude de l'aéroport

            // Conversion des coordonnées en coordonnées cartésiennes
            double y = rayon_terre*Math.cos(latitude*((Math.PI)/180))*Math.cos(longitude*((Math.PI)/180));
            double x = rayon_terre*Math.cos(latitude*((Math.PI)/180))*Math.sin(longitude*((Math.PI)/180));

            Aeroport aeroport = new Aeroport(code,nom_ville,x,y,latitude,longitude);
            aeroports.add(aeroport);
            nb_aeroports++;//Incrémentation du compteur d'aéroports
        }
        lecteur.close();
        return aeroports;
    }







    /**
     * Trouve un aéroport par son code.
     * @param code Code de l'aéroport.
     * @return L'aéroport correspondant au code.
     */
    private Aeroport TrouverAeroportParCode(String code){
        Aeroport resultat=null;
        for (Aeroport aeroport : this.liste_aeroports) {
            if (aeroport.code.equals(code)){
                resultat=aeroport;
            }
        }
        return resultat;
    }


    /**
     * Permet à l'utilisateur de choisir un fichier de vol parmi ceux disponibles dans le répertoire "data".
     * @return Le chemin absolu du fichier de vol choisi.
     * @throws ExceptionNoFlight en cas d'absence de fichiers de vol disponibles.
     */
    private File ChoixVol() throws ExceptionNoFlight {
        Scanner scanner = new Scanner(in);
        File repertoire = new File("data");//Dossier contenant tous les fichiers nécessaires
        File[] files = repertoire.listFiles((dir, name) -> name.endsWith(".csv") && name.startsWith("vol"));// Liste contenant tous les fichiers de vols (qui commencent par "vol-test" et du type ".csv"

        if (files == null || files.length == 0) {// Vérifie qu'il y a bien au moins un fichier de vol dans le répertoire
            throw new ExceptionNoFlight();
        }

        System.out.println("Liste fichiers de vols disponibles :");
        for (int i = 0; i < files.length; i++) {
            System.out.println((i) + ". "+ files[i].getName());
        }


        int choix;
        while (true) {//Tant que l'utilisateur n'a pas entrée quelque chose de proposé, on boucle à l'infinie
            System.out.print("Veuillez saisir le numéro du fichier souhaité : ");
            try{// Test si l'utilisateur a bien entré un entier
                choix = scanner.nextInt();
                if (choix >= 0 && choix < files.length) {break;}// Test si l'utilisateur a bien entré une des propositions
                else{
                    System.out.println("Saisie incorrecte, entrez un numéro valide");
                }
            }
            catch (InputMismatchException e) {
                System.out.println("Saisie incorrecte, entrez un entier");
                scanner.next();
            }
        }

        return files[choix];

    }

    /**
     * Lit les vols à partir d'un fichier choisi par l'utilisateur.
     * @return La liste des vols.
     * @throws IOException en cas d'erreur de lecture de fichier.
     * @throws ExceptionNoFlight en cas d'absence de fichiers de vol disponibles.
     * @throws InputMismatchException en cas d'erreur de saisie de l'utilisateur.
     */
    private ArrayList<Vol> LireVols() throws IOException, ExceptionNoFlight, InputMismatchException {
        ArrayList<Vol> vols = new ArrayList<>();
        File nomfichier= ChoixVol();//le fichier est choisi grâce à la méthode ChoixVol()
        BufferedReader lecteur = new BufferedReader(new FileReader(nomfichier));
        String line;
        while ((line = lecteur.readLine()) != null) {
            Vol vol = LectureVol(line);

            this.getGraph_vol().addNode(vol.code);//On met le code du vol en tant qu'id du noeud
            vols.add(vol);
            nb_vols++;//Incrémentation du compteur de vols
        }
        lecteur.close();
        return vols;
    }

    /**
     * Lit les vols à partir d'un fichier spécifique passé en paramètre.
     * @param fichier Nom du fichier contenant les informations des vols.
     * @return La liste des vols.
     * @throws IOException en cas d'erreur de lecture de fichier.
     * @throws InputMismatchException en cas d'erreur de format du fichier.
     */
    private ArrayList<Vol> LireVols(File fichier) throws IOException, ExceptionNoFlight, InputMismatchException {
        ArrayList<Vol> vols = new ArrayList<>();
        BufferedReader lecteur = new BufferedReader(new FileReader(fichier));
        String line;
        while ((line = lecteur.readLine()) != null) {
            Vol vol = LectureVol(line);

            this.getGraph_vol().addNode(vol.code);//On met le code du vol en tant qu'id du noeud
            vols.add(vol);
            nb_vols++;//Incrémentation du compteur de vols
        }
        lecteur.close();
        return vols;
    }

    /**
     * Lit un vol à partir d'une ligne de texte.
     * @param line Ligne de texte contenant les informations du vol.
     * @return Le vol créé à partir de la ligne de texte.
     */
    private Vol LectureVol(String line) {
        Vol vol;
        StringTokenizer st = new StringTokenizer(line, ";");

        String code = st.nextToken();
        Aeroport depart = TrouverAeroportParCode(st.nextToken());
        Aeroport arrivee = TrouverAeroportParCode(st.nextToken());
        int heure_depart = Integer.parseInt(st.nextToken());
        int minute_depart = Integer.parseInt(st.nextToken());
        int temps_vol = Integer.parseInt(st.nextToken());

        vol = new Vol(code,depart,arrivee,heure_depart,minute_depart,temps_vol);

        return vol;
    }







    /**
     * Calcule le temps avant qu'un vol atteigne un point spécifique (x, y) sur sa trajectoire.
     *
     * @param vol Le vol pour lequel le calcul est effectué.
     * @param x La coordonnée x du point d'intersection potentiel.
     * @param y La coordonnée y du point d'intersection potentiel.
     * @return Le temps, en minutes, avant que le vol atteigne le point d'intersection.
     */
    private double TempsAvantCollision(Vol vol,double x,double y) {

        // Distance entre le point de départ et le point d'intersection
        double d_avant_collision = Math.sqrt(Math.pow((x - vol.depart.x),2) + Math.pow((y - vol.depart.y),2));
        // Distance totale entre le point de départ et le point d'arrivée
        double d_total = Math.sqrt(Math.pow((vol.arrivee.x - vol.depart.x),2) + Math.pow((vol.arrivee.y - vol.depart.y),2));

        // Proportion de la distance totale parcourue avant d'atteindre le point d'intersection
        double proportion_avant_collision = d_avant_collision/d_total;

        // Temps avant collision en multipliant la proportion par le temps total du vol
        return proportion_avant_collision*vol.temps_vol;
    }

    /**
     * Trouve le point d'intersection entre deux vols, s'il existe, et détermine si une collision est possible.
     *
     * @param vol1 Le premier vol.
     * @param vol2 Le second vol.
     * @return Un tableau de double contenant les coordonnées x et y du point d'intersection,
     *         et un indicateur de collision (1 pour collision potentielle, 2 pour collision spéciale, 0 pour pas de collision).
     *         Pour le cas 1, il faudra par la suite calculer le croisement se fait dans l'intervalle choisi
     *         Pour le cas 2, les 2 vols se croiseront forcément
     */
    private double[] TrouverPointIntersection(Vol vol1, Vol vol2) {
        double[] reponse = new double[3];

        // Calcul du dénominateur pour déterminer s'il y a une intersection
        double denominateur = (vol1.depart.x - vol1.arrivee.x) * (vol2.depart.y - vol2.arrivee.y) - (vol1.depart.y - vol1.arrivee.y) * (vol2.depart.x - vol2.arrivee.x);
        double intersectionX;
        double intersectionY;

        // Différences de temps de départ et d'arrivée entre les vols
        double diff_dec = abs((vol2.heure_depart * 60 + vol2.minute_depart) - (vol1.heure_depart * 60 + vol1.minute_depart));
        double diff_att = abs((vol2.heure_arrivee * 60 + vol2.minute_arrivee) - (vol1.heure_arrivee * 60 + vol1.minute_arrivee));

        // Vérifications diverses pour déterminer le type de collision possible
        boolean aeroports_inverses = vol1.depart.x == vol2.arrivee.x && vol1.depart.y == vol2.arrivee.y && vol1.arrivee.x == vol2.depart.x && vol1.arrivee.y == vol2.depart.y;// L'aéroport de départ de l'un est l'aéroport d'arrivée de l'autre et inversement
        boolean meme_depart = vol1.depart.x == vol2.depart.x && vol1.depart.y == vol2.depart.y;// Les vols partent du même aéroport
        boolean meme_arrivee = vol1.arrivee.x == vol2.arrivee.x && vol1.arrivee.y == vol2.arrivee.y;// Les vols arrivent au même aéroport

        boolean v1_part_avant_v2 = (vol1.heure_depart * 60 + vol1.minute_depart) <= (vol2.heure_depart * 60 + vol2.minute_depart);// Le vol 1 décolle avant le vol 2
        boolean v2_part_avant_v1 = (vol2.heure_depart * 60 + vol2.minute_depart) <= (vol1.heure_depart * 60 + vol1.minute_depart);// Le vol 2 décolle avant le vol 1

        boolean v2_part_avant_v1_arrive = (vol2.heure_depart * 60 + vol2.minute_depart) <= (vol1.heure_arrivee * 60 + vol1.minute_arrivee);// Le vol 2 décolle avant que le vol 1 n'atterrisse
        boolean v1_part_avant_v2_arrive = (vol1.heure_depart * 60 + vol1.minute_depart) <= (vol2.heure_arrivee * 60 + vol2.minute_arrivee);// Le vol 1 décolle avant que le vol 2 n'atterrisse

        boolean v1dec_meme_v2dec = (vol1.heure_depart * 60 + vol1.minute_depart) == (vol2.heure_depart * 60 + vol2.minute_depart);// Les vols décollent en même temps

        boolean v2_arrive_avant_v1 = (vol1.heure_arrivee * 60 + vol1.minute_arrivee) >= (vol2.heure_arrivee * 60 + vol2.minute_arrivee);// Le vol 2 atterrie avant le vol 1
        boolean v1_arrive_avant_v2 = (vol2.heure_arrivee * 60 + vol2.minute_arrivee) >= (vol1.heure_arrivee * 60 + vol1.minute_arrivee);// Le vol 1 atterrie avant le vol 2

        if (denominateur != 0) { // Les vols se croisent
            double t = ((vol1.depart.x - vol2.depart.x) * (vol2.depart.y - vol2.arrivee.y) - (vol1.depart.y - vol2.depart.y) * (vol2.depart.x - vol2.arrivee.x)) / denominateur;
            double u = ((vol1.depart.x - vol2.depart.x) * (vol1.depart.y - vol1.arrivee.y) - (vol1.depart.y - vol2.depart.y) * (vol1.depart.x - vol1.arrivee.x)) / denominateur;

            //Arrondie des 2 valeurs pour contrebalancer les imprécisions de calculs
            double t_arrondi = (double) (Math.round(t * 1000000000)) / 1000000000;
            double u_arrondi = (double) (Math.round(u * 1000000000)) / 1000000000;

            if (t_arrondi >= 0.0 && t_arrondi <= 1 && u_arrondi >= 0 && u_arrondi <= 1) {
                //Calcul des coordonnées du point d'intersection
                intersectionX = vol1.depart.x + t * (vol1.arrivee.x - vol1.depart.x);
                intersectionY = vol1.depart.y + t * (vol1.arrivee.y - vol1.depart.y);
                reponse[0] = intersectionX;
                reponse[1] = intersectionY;
                reponse[2] = 1.00; // Collisions.Collision potentielle détectée
            }
        }
        else if (aeroports_inverses) { // Les vols sont colinéaires et se dirigent en sens opposés
            if ((v1_part_avant_v2 && v2_part_avant_v1_arrive) || (v2_part_avant_v1 && v1_part_avant_v2_arrive) || v1dec_meme_v2dec) {
                reponse[2] = 2.00; // Collision spéciale détectée
            }
        }
        else if (meme_depart && meme_arrivee) { // Les vols sont colinéaires et vont dans la même direction
            if ((v1_part_avant_v2 && v2_arrive_avant_v1) || (v2_part_avant_v1 && v1_arrive_avant_v2) || diff_dec < temps_collision || diff_att < temps_collision) {
                reponse[2] = 2.00; // Collision spéciale détectée
            }
        }

        return reponse;
    }

    /**
     * Recherche les collisions potentielles entre les vols et les ajoute à une liste de collisions.
     *
     * @return Une liste de collisions potentielles entre les vols.
     */
    public ArrayList<Collision> RechercheCollision() {
        ArrayList<Collision> liste_collisions = new ArrayList<Collision>();
        Iterator<Vol> iterateur = this.liste_vols.iterator();
        while (iterateur.hasNext()) { // Itère sur chaque vol pour vérifier les collisions possibles
            Vol vol1 = iterateur.next();
            Iterator<Vol> iterateur2 = this.liste_vols.iterator();
            while (iterateur2.hasNext()) { // Compare le vol actuel avec tous les autres vols
                Vol vol2 = iterateur2.next();
                if (vol1.code.equals(vol2.code)){// Évite de comparer un vol avec lui-même
                    continue;
                }

                //Calcul le point d'intersection entre les 2 vols, dans le cas où leurs trajectoires se croisent
                double [] point_intersections = TrouverPointIntersection(vol1,vol2);
                double x3 = point_intersections[0];
                double y3 = point_intersections[1];

                if (point_intersections[2]==1 || point_intersections[2]==2){
                    double instant_collision_vol1=vol1.heure_depart*60 + vol1.minute_depart + TempsAvantCollision(vol1,x3,y3);

                    double instant_collision_vol2=vol2.heure_depart*60 + vol2.minute_depart + TempsAvantCollision(vol2,x3,y3);

                    // Vérifie si les temps de collision sont suffisamment proches pour considérer une collision
                    if (abs(instant_collision_vol1-instant_collision_vol2)<temps_collision || point_intersections[2]==2){//Vérifie s'il y a une collision potentielle
                        Collision collision1 = new Collision(vol1,vol2);
                        boolean collisionPresente = false;

                        // Vérifie si la collision n'est pas déjà enregistrée
                        for (Collision collision2 : liste_collisions){
                            if ((collision1.vol1.code+collision1.vol2.code).equals(collision2.vol2.code+collision2.vol1.code)){
                                collisionPresente = true;
                                break;

                            }
                        }
                        if (!collisionPresente){
                            // Ajoute l'arête entre les vols dans le graphe
                            graph_vol.addEdge(vol1.code+" => "+ vol2.code,vol2.code, vol1.code);

                            // Ajoute la collision à la liste
                            liste_collisions.add(collision1);
                        }
                    }

                }
            }
        }
        return liste_collisions;
    }

}