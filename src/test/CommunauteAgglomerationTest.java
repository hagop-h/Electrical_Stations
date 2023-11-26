package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import phase2.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Scanner;

public class CommunauteAgglomerationTest {
    private CommunauteAgglomeration communaute;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        communaute = new CommunauteAgglomeration();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(System.out);
    }

    // Fonction utilitaire pour capturer la sortie standard
    private String systemOut() {
        return outContent.toString();
    }

    @Test
    public void testLireEntier_EntrerEntier_RetourneEntier() {
        // Arrange
        Scanner scanner = new Scanner("42\n");
        // Act
        int result = communaute.lireEntier(scanner);
        // Assert
        assertEquals(42, result);
    }

    @Test
    public void testLireEntier_EntrerNonEntier_RetourneEntier() {
        // Arrange
        Scanner scanner = new Scanner("abc\n123\n");
        // Act
        int result = communaute.lireEntier(scanner);
        // Assert
        assertEquals(123, result);
    }

    @Test
    public void testTrouverVilleParNom_VilleExistante_RetourneVille() {
        // Arrange
        Ville ville = new Ville("Paris");
        communaute.ajouterVille(ville);
        // Act
        Ville result = communaute.trouverVilleParNom("Paris");
        // Assert
        assertEquals(ville, result);
    }

    @Test
    public void testTrouverVilleParNom_VilleInexistante_RetourneNull() {
        // Arrange
        CommunauteAgglomeration communaute = new CommunauteAgglomeration();
        Ville ville = new Ville("Paris");
        communaute.ajouterVille(ville);
        // Act
        Ville result = communaute.trouverVilleParNom("Londres");
        // Assert
        assertNull(result);
    }

    @Test
    public void testTrouverSolutionManuelle_AjouterZoneRecharge() {
        // Arrange
        InputStream fakeInput = new ByteArrayInputStream("1\nVilleA\n3\n".getBytes());
        System.setIn(fakeInput);

        // Act
        communaute.trouverSolutionManuelle();

        // Assert
        assertTrue(communaute.contientRecharge(new Ville("VilleA")));
    }

    @Test
    public void testTrouverSolutionManuelle_RetirerZoneRecharge() {
        // Arrange
        InputStream fakeInput = new ByteArrayInputStream("2\nVilleA\n3\n".getBytes());
        System.setIn(fakeInput);
        // Simuler l'ajout d'une zone de recharge via le menu
        communaute.ajouterZoneRechargeMenu(new Scanner(System.in));
        // Act
        communaute.trouverSolutionManuelle();
        // Assert
        assertFalse(communaute.contientRecharge(new Ville("VilleA")));
    }

    @Test
    public void testAjouterRoute_VillesExistantes_AjouteRoute() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        // Act
        communaute.ajouterRoute("VilleA", "VilleB");
        // Assert
        assertTrue(communaute.contientRoute(villeA, villeB));
    }

    @Test
    public void testAjouterRoute_UneVilleInexistante_AfficheMessageErreur() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        communaute.ajouterVille(villeA);
        // Act
        communaute.ajouterRoute("VilleA", "VilleB");
        // Assert
        assertTrue(systemOut().contains("Villes non trouvées. Veuillez réessayer."));
    }

    @Test
    public void testAjouterRoute_ListeVillesNull_AfficheMessageErreur() {
        // Arrange
        communaute.setVilles(null);
        // Act
        communaute.ajouterRoute("VilleA", "VilleB");
        // Assert
        assertTrue(systemOut().contains("Erreur lors de l'ajout de la route. Veuillez vérifier la configuration de la communauté d'agglomération."));
    }

    @Test
    public void testAjouterRoute_MethodeAjouterRouteNonInitialisee_AfficheMessageErreur() {
        // Arrange
        communaute.setVilles(new HashSet<>()); // Assurer que la liste de villes n'est pas nulle mais vide
        // Act
        communaute.ajouterRoute("VilleA", "VilleB");
        // Assert
        assertTrue(systemOut().contains("Erreur lors de l'ajout de la route. Veuillez vérifier la configuration de la communauté d'agglomération."));
    }

    @Test
    public void testAjusterRechargeConnectees_VilleAvecZonesDeRecharge_AucuneZoneRechargeAjoutee() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        communaute.ajouterVille(villeA);
        communaute.ajouterRecharge(new ZoneRecharge(villeA));
        // Act
        communaute.ajusterRechargeConnectees(villeA);
        // Assert
        assertTrue(communaute.contientRecharge(villeA));
    }

    @Test
    public void testAjusterRechargeConnectees_VilleSansZonesDeRecharge_AjouteZonesRecharge() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute(villeA, villeB);
        // Act
        communaute.ajusterRechargeConnectees(villeA);
        // Assert
        assertTrue(communaute.contientRecharge(villeB));
    }

    @Test
    public void testAjusterRechargeConnectees_VilleSansZonesDeRecharge_AjouteZonesRechargeInversement() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute(villeA, villeB);
        // Act
        communaute.ajusterRechargeConnectees(villeB);
        // Assert
        assertTrue(communaute.contientRecharge(villeA));
    }

    @Test
    public void testAjusterRechargeConnectees_ListeRoutesNull_AfficheMessageErreur() {
        // Arrange
        CommunauteAgglomeration communaute = new CommunauteAgglomeration();
        Ville villeA = new Ville("VilleA");
        communaute.ajouterVille(villeA);
        communaute.setRoutes(null);
        // Act
        communaute.ajusterRechargeConnectees(villeA);
        // Assert
        assertTrue(systemOut().contains("Erreur lors de l'ajustement des zones de recharge. Veuillez vérifier la configuration de la communauté d'agglomération."));
    }

    @Test
    public void testAjouterZoneRechargeMenu_VilleAvecZonesDeRecharge_AucuneZoneRechargeAjoutee() {
        // Arrange
        CommunauteAgglomeration communaute = new CommunauteAgglomeration();
        Ville villeA = new Ville("VilleA");
        communaute.ajouterVille(villeA);
        communaute.ajouterRecharge(new ZoneRecharge(villeA));
        // Act
        communaute.ajouterZoneRechargeMenu(new Scanner("VilleA\n"));
        // Assert
        assertTrue(communaute.getZonesRecharge().contains(new ZoneRecharge(villeA)));
    }

    @Test
    public void testAjouterZoneRechargeMenu_VilleSansZonesDeRecharge_AjouteZonesRecharge() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute(villeA, villeB);
        // Act
        communaute.ajouterZoneRechargeMenu(new Scanner("VilleA\n"));
        // Assert
        assertTrue(communaute.contientRecharge(villeB));
    }


    // Les autres tests ...
}
