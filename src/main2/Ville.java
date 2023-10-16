package main2;


public class Ville {
    private String nom;
    private boolean zoneDeRecharge=false;;
    
    public Ville(String nom2) {
        this.nom = nom2;
    }
    
    public void setzoneDeRechargeTrue() {
    	zoneDeRecharge=true;
    }
    public void setzoneDeRechargeFalse() {
    	zoneDeRecharge=false;
    }
    public boolean getzoneDeRecharge() {
    	return zoneDeRecharge;
    }
    
    
    public String getNom() {
    	return nom;
    }

    @Override
    public String toString() {
        return "Ville{" +
                "nom=" + nom +
                ", zoneDeRecharge=" + zoneDeRecharge +
                '}';
    }

}