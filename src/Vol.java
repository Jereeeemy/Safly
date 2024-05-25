public class Vol {
    String code;
    Aeroport depart;
    Aeroport arrivee;
    double heure_depart;
    double minute_depart;
    double heure_arrivee;
    double minute_arrivee;
    double temps_vol;

    /**
     * Constructeur pour créer un vol.
     * En plus des paramètres, on calcule également l'heure et la minute d'arrivée en additionnant le départ et le temps de vol, puis on les met en attributs
     * @param code Code du vol.
     * @param depart Aéroport de départ.
     * @param arrivee Aéroport d'arrivée.
     * @param heure_depart Heure de départ.
     * @param minute_depart Minute de départ.
     * @param temps_vol Temps global du vol en minutes.
     *
     *
     */
    public Vol(String code, Aeroport depart, Aeroport arrivee, double heure_depart, double minute_depart, double temps_vol) {
        this.code = code;
        this.depart = depart;
        this.arrivee = arrivee;
        this.heure_depart = heure_depart;
        this.minute_depart = minute_depart;
        this.heure_arrivee = AdditionHeure_Minutes(heure_depart,minute_depart,temps_vol)[0];
        this.minute_arrivee = AdditionHeure_Minutes(heure_depart,minute_depart,temps_vol)[1];
        this.temps_vol = temps_vol;
    }

    public Aeroport getDepart() {
        return depart;
    }

    public void setDepart(Aeroport depart) {
        this.depart = depart;
    }

    public Aeroport getArrivee() {
        return arrivee;
    }

    public void setArrivee(Aeroport arrivee) {
        this.arrivee = arrivee;
    }

    public double getHeure_depart() {
        return heure_depart;
    }

    public void setHeure_depart(double heure_depart) {
        this.heure_depart = heure_depart;
    }

    public double getMinute_depart() {
        return minute_depart;
    }

    public void setMinute_depart(double minute_depart) {
        this.minute_depart = minute_depart;
    }

    public double getHeure_arrivee() {
        return heure_arrivee;
    }

    public void setHeure_arrivee(double heure_arrivee) {
        this.heure_arrivee = heure_arrivee;
    }

    public double getMinute_arrivee() {
        return minute_arrivee;
    }

    public void setMinute_arrivee(double minute_arrivee) {
        this.minute_arrivee = minute_arrivee;
    }

    public double getTemps_vol() {
        return temps_vol;
    }

    public void setTemps_vol(double temps_vol) {
        this.temps_vol = temps_vol;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Fait la somme d'une Heure et d'une Minute avec un temps donné en minutes
     * @param heure1 Heure initiale.
     * @param minute1 Minute initiale.
     * @param temps_a_aditionner Temps à rajouter en minute
     * @return Renvoie un tableau contenant à l'indice 0 la nouvelle heure et à l'indice 1, la nouvelle minute
     */
    public double[] AdditionHeure_Minutes(double heure1, double minute1, double temps_a_aditionner){
        double[] resultat = new double[2];
        minute1=minute1+ temps_a_aditionner;
        while (minute1>=60){// Si on a dépassé les 59 minutes, on convertit ce dépassement en heures
            minute1=minute1-60;
            heure1=heure1+1;
        }
        resultat[0]=heure1;
        resultat[1]=minute1;
        return resultat;
    }
}
