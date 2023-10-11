import main.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import java.util.Scanner;

public class CommunauteAgglomerationTest {
    private CommunauteAgglomeration communaute;

    @Before
    public void setUp() {
        communaute = new CommunauteAgglomeration();
    }

    @Test
    public void testAjouterVille() {
        Ville ville = new Ville(NomVille.Nom.A);
        communaute.ajouterVille(ville);
        assertTrue(communaute.getVilles().contains(ville));
    }

    @Test
    public void testAjouterRoute() {
        Ville villeA = new Ville(NomVille.Nom.A);
        Ville villeB = new Ville(NomVille.Nom.B);
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute(villeA, villeB);
        assertTrue(communaute.getRoutes().size() == 1);
    }

    @Test
    public void testMettreAJourVillesAvecRecharge() {
        Ville villeA = new Ville(NomVille.Nom.A);
        Ville villeB = new Ville(NomVille.Nom.B);
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.mettreAJourVillesAvecRecharge();
        assertTrue(communaute.getParkings().size() == 2);
    }

    @Test
    public void testTrouverVilleParNom() {
        Ville villeA = new Ville(NomVille.Nom.A);
        communaute.ajouterVille(villeA);
        Ville result = communaute.trouverVilleParNom(NomVille.Nom.A.name());
        assertEquals(villeA, result);
    }

    @Test
    public void testEstRelieeAvecBorne() {
        Ville villeA = new Ville(NomVille.Nom.A);
        Ville villeB = new Ville(NomVille.Nom.B);
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute(villeA, villeB);
        assertFalse(communaute.estRelieeAvecBorne(villeA));
    }

    @Test
    public void testRespecteContrainte() {
        Ville villeA = new Ville(NomVille.Nom.A);
        Ville villeB = new Ville(NomVille.Nom.B);
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute(villeA, villeB);
        // Créez un mock de Scanner avec Mockito pour simuler une entrée utilisateur
        Scanner scannerMock = Mockito.mock(Scanner.class);
        Mockito.when(scannerMock.nextLine()).thenReturn("A"); // Simulez la saisie de "A" comme nom de la ville
        communaute.ajouterZoneRechargeMenu(scannerMock); // Ajoutez une zone de recharge
        assertTrue(communaute.respecteContrainte(villeA));
    }

    @Test
    public void testPeutRetirerZoneRecharge() {
        Ville villeA = new Ville(NomVille.Nom.A);
        Ville villeB = new Ville(NomVille.Nom.B);
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute(villeA, villeB);
        assertTrue(communaute.peutRetirerZoneRecharge(villeA));
    }

    @Test
    public void testContientZoneRecharge() {
        Ville villeA = new Ville(NomVille.Nom.A);
        communaute.ajouterVille(villeA);
        // Créez un mock de Scanner avec Mockito pour simuler une entrée utilisateur
        Scanner scannerMock = Mockito.mock(Scanner.class);
        Mockito.when(scannerMock.nextLine()).thenReturn("A"); // Simulez la saisie de "A" comme nom de la ville
        communaute.ajouterZoneRechargeMenu(scannerMock); // Ajoutez une zone de recharge
        assertTrue(communaute.contientZoneRecharge(villeA));
    }

    @Test
    public void testRetirerZoneRecharge() {
        Ville villeA = new Ville(NomVille.Nom.A);
        communaute.ajouterVille(villeA);
        // Créez un mock de Scanner avec Mockito pour simuler une entrée utilisateur
        Scanner scannerMock = Mockito.mock(Scanner.class);
        Mockito.when(scannerMock.nextLine()).thenReturn("A"); // Simulez la saisie de "A" comme nom de la ville
        communaute.ajouterZoneRechargeMenu(scannerMock); // Ajoutez une zone de recharge
        communaute.retirerZoneRecharge(villeA);
        assertFalse(communaute.contientZoneRecharge(villeA));
    }

    @Test
    public void testAjouterZoneRechargeMenu() {
        // Créez un mock de Scanner avec Mockito pour simuler une entrée utilisateur
        Scanner scannerMock = Mockito.mock(Scanner.class);
        // Simulez la saisie de "A" comme nom de la ville
        Mockito.when(scannerMock.nextLine()).thenReturn("A");
        Ville villeA = new Ville(NomVille.Nom.A);
        communaute.ajouterVille(villeA);
        // Appelez la méthode en passant le scanner simulé
        communaute.ajouterZoneRechargeMenu(scannerMock);
        assertTrue(communaute.contientZoneRecharge(villeA));
    }

    @Test
    public void testRetirerZonesRechargeConnectees() {
        Ville villeA = new Ville(NomVille.Nom.A);
        Ville villeB = new Ville(NomVille.Nom.B);
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute(villeA, villeB);
        // Créez un mock de Scanner avec Mockito pour simuler une entrée utilisateur
        Scanner scannerMock = Mockito.mock(Scanner.class);
        Mockito.when(scannerMock.nextLine()).thenReturn("A"); // Simulez la saisie de "A" comme nom de la ville
        communaute.ajouterZoneRechargeMenu(scannerMock); // Ajoutez une zone de recharge
        // Appelez la méthode pour retirer les zones de recharge connectées
        communaute.retirerZonesRechargeConnectees(villeA);
        assertFalse(communaute.contientZoneRecharge(villeB));
    }

    // Les autres tests ...
}
