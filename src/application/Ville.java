package application;


public class Ville {
    private String nom; // Nom d'une ville
    private boolean zoneDeRecharge = false; // Pour indiquer si une ville possède une zone de recharge

    public Ville(String nom) {
        this.nom = nom;
    }

    public void setzoneDeRechargeTrue() {
        zoneDeRecharge = true;
    }

    public void setzoneDeRechargeFalse() {
        zoneDeRecharge = false;
    }

    public void setzoneDeRecharge(boolean etatInitial) {
        zoneDeRecharge = etatInitial;
    }

    public boolean getzoneDeRecharge() {
        return zoneDeRecharge;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public String toString() {
        return "Ville {" + "nom = " + nom + ", zoneDeRecharge = " + zoneDeRecharge + '}';
    }
}
