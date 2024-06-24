package collisions;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

import static java.lang.Math.abs;
import static java.lang.System.in;

/**
 * Class représentant la carte des vols, aéroports et collisions
 */
public class Carte {
    /**
     * Graph des vols avec les vols en nœuds et les collisions en arêtes
     */
    private Graph graph_vol;

    /**
     * Graph des aéroports avec les aéroports en nœuds et les vols en arêtes
     */
    private Graph graph_aeroport;

    /**
     * Liste contenant tous les aéroports du fichier
     */
    private ArrayList<Aeroport> liste_aeroports;

    /**
     * Liste contenant tous les vols du fichier
     */
    private ArrayList<Vol> liste_vols;

    /**
     * Nombre d'aéroports présent dans le fichier sélectionné
     */
    private static int nb_aeroports;

    /**
     * Nombre de vols présent dans le fichier sélectionné
     */
    private static int nb_vols;

    /**
     * Intervalle de temps pour lequel on considère qu'il y a un risque de collision (15 par défaut)
     */
    public static int temps_collision = 15;

    /**
     * Valeur du rayon de la terre
     */
    public static int rayon_terre = 6371;

    /**
     * Constructeur par défaut pour initialiser la carte avec les aéroports et les vols.
     * @throws IOException en cas d'erreur de lecture de fichier.
     * @throws ExceptionNoFlight en cas d'absence de fichiers de vol disponibles.
     */
    public Carte() throws IOException, ExceptionNoFlight, ExceptionOrientation {
        graph_vol = new SingleGraph("Collisions_Carte");
        graph_aeroport = new SingleGraph("Graph_Aeroport_As_Node");
        nb_aeroports = 0;
        nb_vols = 0;
        liste_aeroports = this.LireAeroports();
        liste_vols = this.LireVols();
        this.RechercheCollision();
    }

    /**
     * Constructeur pour initialiser la carte avec les aéroports et les vols depuis un fichier spécifique pour les vols.
     * @param fichier_vol Nom du fichier contenant les informations des vols.
     * @param fichier_aeroport Nom du fichier contenant les informations des aéroports.
     * @throws IOException en cas d'erreur de lecture de fichier.
     * @throws ExceptionNoFlight en cas d'absence de fichiers de vol disponibles.
     */
    public Carte(File fichier_aeroport,File fichier_vol) throws IOException, ExceptionNoFlight, ExceptionOrientation {
        graph_vol = new SingleGraph("Collisions_Carte_"+fichier_vol);
        graph_aeroport = new MultiGraph("Graph_Aeroport_As.Node_"+fichier_vol);
        nb_aeroports = 0;
        nb_vols = 0;
        liste_aeroports = this.LireAeroports(fichier_aeroport);
        liste_vols = this.LireVols(fichier_vol);
    }

    /**
     * Constructeur pour initialiser la carte avec les aéroports et les vols depuis un fichier spécifique pour soit pour les vols soit pour les aéroport (selon l'extension du fichier en paramètre).
     * @param fichier Nom du fichier contenant les informations des vols ou les informations des aéroport (selon son extension, txt pour aéroports et csv pour vols).
     * @throws IOException en cas d'erreur de lecture de fichier.
     * @throws ExceptionNoFlight en cas d'absence de fichiers de vol disponibles.
     */
    public Carte(File fichier) throws IOException, ExceptionNoFlight, ExceptionOrientation {
        graph_vol = new SingleGraph("Collisions_Carte_"+fichier);
        graph_aeroport = new MultiGraph("Graph_Aeroport_As.Node_"+fichier);
        nb_aeroports = 0;
        nb_vols = 0;
        if (fichier.getName().endsWith(".txt")) {
            liste_aeroports = this.LireAeroports(fichier);

        }
        else if (fichier.getName().endsWith(".csv")) {
            liste_vols = this.LireVols(fichier);
        }

    }

    // Getters pour les attributs de la classe Collisions.Carte.
    public Graph getGraph_vol() {
        return this.graph_vol;
    }

    public Graph getGraph_aeroport(){return this.graph_aeroport;}

    public int getNb_aeroports() {
        return this.nb_aeroports;
    }

    public int getNb_vols() {
        return this.nb_vols;
    }

    public ArrayList<Aeroport> getListe_aeroports() {
        return liste_aeroports;
    }

    public ArrayList<Vol> getListe_vols() {return liste_vols;}

    // Setters
    public void setListe_aeroports(File fichier_aeroport) throws ExceptionOrientation, IOException {
        this.liste_aeroports = this.LireAeroports(fichier_aeroport);
    }

    public void setListe_vols(File fichier_vols) throws ExceptionNoFlight, IOException {
        this.liste_vols = this.LireVols(fichier_vols);
    }

    public static void setTemps_collision(int temps_collision) {
        Carte.temps_collision = temps_collision;
    }

    public static int getTemps_collision() {
        return temps_collision;
    }

    public void setGraph_vol(Graph graph_vol) {
        this.graph_vol = graph_vol;
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
    public ArrayList<Aeroport> LireAeroports() throws IOException, ExceptionOrientation {
        String nomfichier = "data/aeroports.txt";
        ArrayList<Aeroport> aeroports = new ArrayList<>();

        try (BufferedReader lecteur = new BufferedReader(new FileReader(nomfichier))) {
            String line;
            // Vérifier si le fichier est vide
            if (!lecteur.ready()) {
                throw new IOException("Le fichier d'aéroports est vide : " + nomfichier);
            }
            while ((line = lecteur.readLine()) != null) {
                try {
                    StringTokenizer st = new StringTokenizer(line, ";");

                    // Vérification du nombre attendu de tokens
                    if (st.countTokens() != 10) {
                        System.err.println("Ligne ignorée - format incorrect : " + line);
                        continue;
                    }
                    // Récupération des données
                    String code = st.nextToken();
                    String nom_ville = st.nextToken();

                    // Conversion des coordonnées de degré/minute/seconde/orientation en décimal
                    double latitude = CalculValeur(nom_ville, st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken());
                    double longitude = CalculValeur(nom_ville, st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken());

                    // Conversion en coordonnées cartésiennes
                    double y = rayon_terre * Math.cos(latitude * (Math.PI / 180)) * Math.cos(longitude * (Math.PI / 180));
                    double x = rayon_terre * Math.cos(latitude * (Math.PI / 180)) * Math.sin(longitude * (Math.PI / 180));

                    Aeroport aeroport = new Aeroport(code, nom_ville, x, y);
                    aeroports.add(aeroport);
                    nb_aeroports++; // Incrémentation du compteur d'aéroports
                } catch (NoSuchElementException | NumberFormatException | ExceptionOrientation e) {
                    // Ignorer la ligne en cas d'erreur de format ou de conversion
                    System.err.println("Ligne ignorée - erreur : " + e.getMessage());
                    continue;
                }
            }
        } catch (IOException e) {
            // Propager l'exception IOException vers l'appelant
            throw new IOException("Erreur de lecture du fichier d'aéroports : " + nomfichier, e);
        }
        return aeroports;
    }

    /**
     * Lit les aéroports à partir d'un fichier passé en parametre et les ajoute à la liste des aéroports.
     * @return La liste des aéroports.
     * @throws IOException en cas d'erreur de lecture de fichier.
     */
    public ArrayList<Aeroport> LireAeroports(File fichier_aeroports) throws IOException, ExceptionOrientation {
        ArrayList<Aeroport> aeroports = new ArrayList<>();
        int erreur = 0;

        try (BufferedReader lecteur = new BufferedReader(new FileReader(fichier_aeroports))) {
            String line;

            // Vérifier si le fichier est vide
            if (!lecteur.ready()) {
                throw new IOException("Le fichier d'aéroports est vide");
            }

            while ((line = lecteur.readLine()) != null) {
                try {
                    StringTokenizer st = new StringTokenizer(line, ";");

                    // Vérification du nombre attendu de tokens
                    if (st.countTokens() != 10) {
                        System.out.println("Ligne ignorée - format incorrect : " + line);
                        erreur++;
                        if (erreur >= 20) {
                            throw new IOException("Trop d'erreurs de format dans le fichier d'aéroports");
                        }
                        continue;
                    }

                    // Récupération des données
                    String code = st.nextToken();
                    String nom_ville = st.nextToken();

                    // Conversion des coordonnées de degré/minute/seconde/orientation en décimal
                    double latitude = CalculValeur(nom_ville, st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken());
                    double longitude = CalculValeur(nom_ville, st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken());

                    // Conversion en coordonnées cartésiennes
                    double y = rayon_terre * Math.cos(latitude * (Math.PI / 180)) * Math.cos(longitude * (Math.PI / 180));
                    double x = rayon_terre * Math.cos(latitude * (Math.PI / 180)) * Math.sin(longitude * (Math.PI / 180));

                    Aeroport aeroport = new Aeroport(code, nom_ville, x, y, latitude, longitude);
                    aeroports.add(aeroport);
                    nb_aeroports++; // Incrémentation du compteur d'aéroports
                } catch (NoSuchElementException | NumberFormatException | ExceptionOrientation e) {
                    // Ignorer la ligne en cas d'erreur de format ou de conversion
                    System.out.println("Ligne ignorée - erreur : " + e.getMessage());
                    erreur++;
                    if (erreur >= 20) {
                        throw new IOException("Trop d'erreurs de format dans le fichier d'aéroports");
                    }
                    continue;
                }
            }
        } catch (IOException e) {
            // Propager l'exception IOException vers l'appelant
            throw new IOException("Erreur de lecture du fichier d'aéroports", e);
        }
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
            if (aeroport.getCode().equals(code)){
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
     * Lit les vols à partir d'un fichier choisi par l'utilisateur et construit un graph avec les vols en tant que noeuds.
     * @return La liste des vols.
     * @throws IOException en cas d'erreur de lecture de fichier.
     * @throws ExceptionNoFlight en cas d'absence de fichiers de vol disponibles.
     * @throws InputMismatchException en cas d'erreur de saisie de l'utilisateur.
     */
    private ArrayList<Vol> LireVols() throws IOException, ExceptionNoFlight, InputMismatchException {
        ArrayList<Vol> vols = new ArrayList<>();

        // Choix du fichier à partir de la méthode ChoixVol()
        File nomfichier = ChoixVol();
        try (BufferedReader lecteur = new BufferedReader(new FileReader(nomfichier))) {
            String line;
            boolean fichierVide = true; // Indicateur pour vérifier si le fichier est vide
            while ((line = lecteur.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // Si la ligne n'est pas vide, traiter le vol
                    fichierVide = false;
                    try {
                        Vol vol = LectureVol(line);
                        this.getGraph_vol().addNode(vol.getCode()); // Ajouter le code du vol en tant qu'ID du nœud
                        vols.add(vol);
                        nb_vols++; // Incrémenter le compteur de vols
                    } catch (NoSuchElementException | NumberFormatException e) {
                        System.err.println("Erreur de lecture - format incorrect : " + line);
                    }
                } else if (line.isEmpty() || line.isBlank()) {
                    // Si la ligne est vide, ignorer et continuer
                    System.err.println("Ligne vide ignorée.");
                }
            }
            // Vérifier si le fichier est vide
            if (fichierVide) {
                throw new IOException("Le fichier de vols est vide : " + nomfichier.getName());
            }
        } catch (IOException e) {
            // Propager l'exception IOException vers l'appelant
            throw new IOException("Erreur de lecture du fichier de vols : " + nomfichier.getName(), e);
        }
        return vols;
    }

    /**
     * Lit les vols à partir d'un fichier spécifique passé en paramètre et construit un graph avec les vols en tant que noeuds.
     * @param fichier Nom du fichier contenant les informations des vols.
     * @return La liste des vols.
     * @throws IOException en cas d'erreur de lecture de fichier.
     * @throws InputMismatchException en cas d'erreur de format du fichier.
     */
    public ArrayList<Vol> LireVols(File fichier) throws IOException, ExceptionNoFlight, InputMismatchException {
        ArrayList<Vol> vols = new ArrayList<>();

        try (BufferedReader lecteur = new BufferedReader(new FileReader(fichier))) {
            String line;
            boolean fichierVide = true; // Indicateur pour vérifier si le fichier est vide
            while ((line = lecteur.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    // Si la ligne n'est pas vide, traiter le vol
                    fichierVide = false;
                    try {
                        Vol vol = LectureVol(line);
                        if (vol!=null && vol.getCode()!=null && idUnique(vols, vol)){
                            this.getGraph_vol().addNode(vol.getCode()); // Ajouter le code du vol en tant qu'ID du nœud
                            vols.add(vol);
                            System.out.println(vol.getCode());
                            nb_vols++; // Incrémenter le compteur de vols
                        }
                    } catch (NoSuchElementException | NumberFormatException e) {
                        System.err.println("Erreur de lecture - format incorrect : " + line);
                    }
                } else if (line.isEmpty() || line.isBlank()){
                    // Si la ligne est vide, ignorer et continuer
                    System.err.println("Ligne vide ignorée.");
                }
            }
            // Vérifier si le fichier est vide
            if (fichierVide) {
                throw new IOException("Le fichier de vols est vide : " + fichier.getName());
            }
        } catch (IOException e) {
            // Propager l'exception IOException vers l'appelant
            throw new IOException("Erreur de lecture du fichier de vols : " + fichier.getName(), e);
        }
        return vols;
    }

    /**
     * Vérifie que l'id du vol en paramètre n'est pas déjà présent dans la liste de vols en paramètre
     * @param vols liste étudiée de vol
     * @param vol vol dont on vérifie l'id
     * @return true si l'id est bien unique, false sinon
     */
    private boolean idUnique(ArrayList<Vol> vols , Vol vol){
        boolean retour = true;
        for (Vol voltest : vols){
            if (voltest.getCode().equals(vol.getCode())){
                retour = false;
            }
        }
        return retour;
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
        if (depart == null || arrivee == null) {
            vol = null;
        }
        else{
            int heure_depart = Integer.parseInt(st.nextToken());
            int minute_depart = Integer.parseInt(st.nextToken());
            int temps_vol = Integer.parseInt(st.nextToken());
            vol = new Vol(code,depart,arrivee,heure_depart,minute_depart,temps_vol);
        }


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
        double d_avant_collision = Math.sqrt(Math.pow((x - vol.getDepart().getx()),2) + Math.pow((y - vol.getDepart().gety()),2));
        // Distance totale entre le point de départ et le point d'arrivée
        double d_total = Math.sqrt(Math.pow((vol.getArrivee().getx() - vol.getDepart().getx()),2) + Math.pow((vol.getArrivee().gety() - vol.getDepart().gety()),2));

        // Proportion de la distance totale parcourue avant d'atteindre le point d'intersection
        double proportion_avant_collision = d_avant_collision/d_total;

        // Temps avant collision en multipliant la proportion par le temps total du vol
        return proportion_avant_collision*vol.getTemps_vol();
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
        double denominateur = (vol1.getDepart().getx() - vol1.getArrivee().getx()) * (vol2.getDepart().gety() - vol2.getArrivee().gety()) - (vol1.getDepart().gety() - vol1.getArrivee().gety()) * (vol2.getDepart().getx() - vol2.getArrivee().getx());
        double intersectionX;
        double intersectionY;

        // Différences de temps de départ et d'arrivée entre les vols
        double diff_dec = abs((vol2.getHeure_depart() * 60 + vol2.getMinute_depart()) - (vol1.getHeure_depart() * 60 + vol1.getMinute_depart()));
        double diff_att = abs((vol2.getHeure_arrivee() * 60 + vol2.getMinute_arrivee()) - (vol1.getHeure_arrivee() * 60 + vol1.getMinute_arrivee()));

        // Vérifications diverses pour déterminer le type de collision possible
        boolean aeroports_inverses = vol1.getDepart().getx() == vol2.getArrivee().getx() && vol1.getDepart().gety() == vol2.getArrivee().gety() && vol1.getArrivee().getx() == vol2.getDepart().getx() && vol1.getArrivee().gety() == vol2.getDepart().gety();// L'aéroport de départ de l'un est l'aéroport d'arrivée de l'autre et inversement
        boolean meme_depart = vol1.getDepart().getx() == vol2.getDepart().getx() && vol1.getDepart().gety() == vol2.getDepart().gety();// Les vols partent du même aéroport
        boolean meme_arrivee = vol1.getArrivee().getx() == vol2.getArrivee().getx() && vol1.getArrivee().gety() == vol2.getArrivee().gety();// Les vols arrivent au même aéroport

        boolean v1_part_avant_v2 = (vol1.getHeure_depart() * 60 + vol1.getMinute_depart()) <= (vol2.getHeure_depart() * 60 + vol2.getMinute_depart());// Le vol 1 décolle avant le vol 2
        boolean v2_part_avant_v1 = (vol2.getHeure_depart() * 60 + vol2.getMinute_depart()) <= (vol1.getHeure_depart() * 60 + vol1.getMinute_depart());// Le vol 2 décolle avant le vol 1

        boolean v2_part_avant_v1_arrive = (vol2.getHeure_depart() * 60 + vol2.getMinute_depart()) <= (vol1.getHeure_arrivee() * 60 + vol1.getMinute_arrivee());// Le vol 2 décolle avant que le vol 1 n'atterrisse
        boolean v1_part_avant_v2_arrive = (vol1.getHeure_depart() * 60 + vol1.getMinute_depart()) <= (vol2.getHeure_arrivee() * 60 + vol2.getMinute_arrivee());// Le vol 1 décolle avant que le vol 2 n'atterrisse

        boolean v1dec_meme_v2dec = (vol1.getHeure_depart() * 60 + vol1.getMinute_depart()) == (vol2.getHeure_depart() * 60 + vol2.getMinute_depart());// Les vols décollent en même temps

        boolean v2_arrive_avant_v1 = (vol1.getHeure_arrivee() * 60 + vol1.getMinute_arrivee()) >= (vol2.getHeure_arrivee() * 60 + vol2.getMinute_arrivee());// Le vol 2 atterrie avant le vol 1
        boolean v1_arrive_avant_v2 = (vol2.getHeure_arrivee() * 60 + vol2.getMinute_arrivee()) >= (vol1.getHeure_arrivee() * 60 + vol1.getMinute_arrivee());// Le vol 1 atterrie avant le vol 2

        if (denominateur != 0) { // Les vols se croisent
            double t = ((vol1.getDepart().getx() - vol2.getDepart().getx()) * (vol2.getDepart().gety() - vol2.getArrivee().gety()) - (vol1.getDepart().gety() - vol2.getDepart().gety()) * (vol2.getDepart().getx() - vol2.getArrivee().getx())) / denominateur;
            double u = ((vol1.getDepart().getx() - vol2.getDepart().getx()) * (vol1.getDepart().gety() - vol1.getArrivee().gety()) - (vol1.getDepart().gety() - vol2.getDepart().gety()) * (vol1.getDepart().getx() - vol1.getArrivee().getx())) / denominateur;

            //Arrondie des 2 valeurs pour contrebalancer les imprécisions de calculs
            double t_arrondi = (double) (Math.round(t * 1000000000)) / 1000000000;
            double u_arrondi = (double) (Math.round(u * 1000000000)) / 1000000000;

            if (t_arrondi >= 0.0 && t_arrondi <= 1 && u_arrondi >= 0 && u_arrondi <= 1) {
                //Calcul des coordonnées du point d'intersection
                intersectionX = vol1.getDepart().getx() + t * (vol1.getArrivee().getx() - vol1.getDepart().getx());
                intersectionY = vol1.getDepart().gety() + t * (vol1.getArrivee().gety() - vol1.getDepart().gety());
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
     * Recherche les collisions potentielles entre les vols crée une arête quand une est trouvée.
     */
    public void RechercheCollision() {
        ArrayList<Collision> liste_collisions = new ArrayList<Collision>();
        Iterator<Vol> iterateur = this.liste_vols.iterator();
        while (iterateur.hasNext()) { // Itère sur chaque vol pour vérifier les collisions possibles
            Vol vol1 = iterateur.next();
            Iterator<Vol> iterateur2 = this.liste_vols.iterator();
            while (iterateur2.hasNext()) { // Compare le vol actuel avec tous les autres vols
                Vol vol2 = iterateur2.next();
                if (vol1.getCode().equals(vol2.getCode())){// Évite de comparer un vol avec lui-même
                    continue;
                }

                //Calcul le point d'intersection entre les 2 vols, dans le cas où leurs trajectoires se croisent
                double [] point_intersections = TrouverPointIntersection(vol1,vol2);
                double x3 = point_intersections[0];
                double y3 = point_intersections[1];

                if (point_intersections[2]==1 || point_intersections[2]==2){
                    double instant_collision_vol1=vol1.getHeure_depart()*60 + vol1.getMinute_depart() + TempsAvantCollision(vol1,x3,y3);

                    double instant_collision_vol2=vol2.getHeure_depart()*60 + vol2.getMinute_depart() + TempsAvantCollision(vol2,x3,y3);

                    // Vérifie si les temps de collision sont suffisamment proches pour considérer une collision
                    if (abs(instant_collision_vol1-instant_collision_vol2)<temps_collision || point_intersections[2]==2){//Vérifie s'il y a une collision potentielle
                        Collision collision1 = new Collision(vol1,vol2);
                        boolean collisionPresente = false;

                        // Vérifie si la collision n'est pas déjà enregistrée
                        for (Collision collision2 : liste_collisions){
                            if ((collision1.getVol1().getCode()+collision1.getVol2().getCode()).equals(collision2.getVol2().getCode()+collision2.getVol1().getCode())){
                                collisionPresente = true;
                                break;

                            }
                        }
                        if (!collisionPresente){
                            // Ajoute l'arête entre les vols dans le graphe
                            graph_vol.addEdge(vol1.getCode()+" => "+ vol2.getCode(),vol2.getCode(), vol1.getCode());

                            // Ajoute la collision à la liste
                            liste_collisions.add(collision1);
                        }
                    }

                }
            }
        }
    }
}