package projet_java;

public class Main {
    public static void main(String[] args) {
        CommunauteAgglomeration communaute = new CommunauteAgglomeration();

        // Configuration manuelle des zones de recharge
        communaute.configurerCommunaute();
        communaute.gererMenuZonesRecharge();

    }
}
