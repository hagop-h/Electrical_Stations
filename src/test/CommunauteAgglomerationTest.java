package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import phase2.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
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
        assertTrue(communaute.trouverVilleParNom("VilleA").getzoneDeRecharge());
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


 
    @Test
    public void testResoudreAutomatiquement_CommunauteAvecVillesOptimales_ResolutionOptimale() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRecharge(new ZoneRecharge(villeA));
        communaute.ajouterRecharge(new ZoneRecharge(villeB));
        communaute.ajouterRoute(villeA, villeB);

        // Act
        communaute.resoudreAutomatiquement();

        // Assert
        assertTrue(communaute.score() > 0);
    }

    @Test
    public void testChoisirVilleOptimale_CommunauteSansVillesOptimales_RetourneNull() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        
        // Expected result
        Ville expected = new Ville("VilleA");
        expected.setzoneDeRechargeTrue();

        // Act
        Ville result = communaute.choisirVilleOptimale();

        // Assert
        assertEquals(expected.getNom(), result.getNom());
    }



    @Test
    public void testIsProblematicCity_VilleProblematique_MarqueCommeProblematique() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        communaute.markAsProblematicCity(villeA);

        // Act
        boolean result = communaute.isProblematicCity(villeA);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testEstRelieeAvecBorne_VilleRelieeAvecBorne_Vrai() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRecharge(new ZoneRecharge(villeB));
        communaute.ajouterRoute(villeA, villeB);

        // Act
        boolean result = communaute.estRelieeAvecBorne(villeA);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testEstRelieeAvecBorne_VilleNonRelieeAvecBorne_Faux() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);

        // Act
        boolean result = communaute.estRelieeAvecBorne(villeA);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testRespecteContrainte_VilleRespecteContrainte_Vrai() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRecharge(new ZoneRecharge(villeA));
        communaute.ajouterRoute(villeA, villeB);

        // Act
        boolean result = communaute.respecteContrainte(villeA);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testRespecteContrainte_VilleNeRespectePasContrainte_Faux() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute(villeA, villeB);

        // Act
        boolean result = communaute.respecteContrainte(villeA);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsProblematicCity_VilleNonProblematique_NonMarqueeCommeProblematique() {
        // Arrange
        Ville villeA = new Ville("VilleA");

        // Act
        boolean result = communaute.isProblematicCity(villeA);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testMarkAsProblematicCity_VilleNull_AfficheMessageErreur() {
        // Arrange
        Ville villeA = null;

        // Act
        try {
        communaute.markAsProblematicCity(villeA);
        }
        catch(IllegalArgumentException e){
        	 // Assert
            assertTrue(e.getMessage().contains("La ville ne peut pas être null."));
        }
    }       


    @Test
    public void testScore_CommunauteAvecZonesDeRecharge_CalculScoreCorrect() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        communaute.ajouterVille(villeA);
        communaute.recharge("villeA");;

        // Act
        int result = communaute.score();

        // Assert
        assertEquals(1, result);
    }

    @Test
    public void testScore_CommunauteSansZonesDeRecharge_RetourneZero() {
        // Act
        int result = communaute.score();

        // Assert
        assertEquals(0, result);
    }
    @Test
    public void testMarkAsProblematicCity_VilleProblematique_MarqueeCommeProblematique() {
        // Arrange
        Ville villeA = new Ville("VilleA");

        // Act
        communaute.markAsProblematicCity(villeA);

        // Assert
        assertTrue(communaute.isProblematicCity(villeA));
    }

    @Test
    public void testEstRelieeAvecBorne_VilleRelieeAvecBorne_RetourneTrue() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute(villeA, villeB);
        communaute.ajouterRecharge(new ZoneRecharge(villeB));

        // Act
        boolean result = communaute.estRelieeAvecBorne(villeA);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testEstRelieeAvecBorne_VilleNonRelieeAvecBorne_RetourneFalse() {
        // Arrange
        Ville villeA = new Ville("VilleA");

        // Act
        boolean result = communaute.estRelieeAvecBorne(villeA);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testRespecteContrainte_VilleRespecteContrainte_RetourneTrue() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        communaute.ajouterVille(villeA);
        communaute.ajouterRecharge(new ZoneRecharge(villeA));

        // Act
        boolean result = communaute.respecteContrainte(villeA);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testRespecteContrainte_VilleNeRespectePasContrainte_RetourneFalse() {
        // Arrange
        Ville villeA = new Ville("VilleA");

        // Act
        boolean result = communaute.respecteContrainte(villeA);

        // Assert
        assertFalse(result);
    }
}
