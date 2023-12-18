package test;

import phase2.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
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
        communaute.ajouterRoute("VilleA", "VilleB");
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
        communaute.ajouterRoute("VilleA", "VilleB");
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
        assertTrue(communaute.trouverVilleParNom("VilleA").getZoneDeRecharge());
    }

    @Test
    public void testAjouterZoneRechargeMenu_VilleSansZonesDeRecharge_AjouteZonesRecharge() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute("VilleA", "VilleB");
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
        communaute.ajouterRoute("VilleA", "VilleB");

        // Act
        communaute.resoudreAutomatiquement();

        // Assert
        assertTrue(communaute.score() > 0);
    }

    @Test
    public void testAfficherVillesAvecOuSansRecharge_VillesRechargeesAvecSource() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        ZoneRecharge zoneRechargeA = new ZoneRecharge(villeA);
        communaute.ajouterRecharge(zoneRechargeA);
        // Redirect System.out to capture printed output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // Act
        communaute.afficherVillesAvecOuSansRecharge();
        // Assert
        assertTrue(outContent.toString().contains("Villes rechargées avec leurs propres zone de recharge"));
        assertTrue(outContent.toString().contains("- " + villeA.getNom()));
        // Reset System.out
        System.setOut(System.out);
    }

    @Test
    public void testAfficherVillesAvecOuSansRecharge_VillesRechargeesSansSource() {
        // Arrange
        Ville villeB = new Ville("VilleB");
        ZoneRecharge zoneRechargeB = new ZoneRecharge(villeB);
        communaute.ajouterRecharge(zoneRechargeB);
        // Redirect System.out to capture printed output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // Act
        communaute.afficherVillesAvecOuSansRecharge();
        // Assert
        assertTrue(outContent.toString().contains("Villes rechargées sans leurs propres zone de recharge"));
        assertTrue(outContent.toString().contains("- " + villeB.getNom()));
        // Reset System.out
        System.setOut(System.out);
    }

    @Test
    public void testAfficherVillesAvecOuSansRecharge_VillesNonRechargees() {
        // Arrange
        Ville villeC = new Ville("VilleC");
        communaute.ajouterVille(villeC);
        // Redirect System.out to capture printed output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        // Act
        communaute.afficherVillesAvecOuSansRecharge();
        // Assert
        assertTrue(outContent.toString().contains("Villes non rechargées"));
        assertTrue(outContent.toString().contains("- " + villeC.getNom()));
        // Reset System.out
        System.setOut(System.out);
    }

    @Test
    public void testGetVillesAvecSourceRecharge_ListeNonVide_RetourneListe() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        communaute.ajouterRecharge(new ZoneRecharge(villeA));
        // Act
        List<ZoneRecharge> villesAvecSourceRecharge = communaute.getVillesAvecSourceRecharge();
        // Assert
        assertNotNull(villesAvecSourceRecharge);
        // assertFalse(villesAvecSourceRecharge.isEmpty());
        // assertTrue(villesAvecSourceRecharge.stream().anyMatch(zoneRecharge -> zoneRecharge.getVille().equals(villeA)));
    }

    @Test
    public void testGetVillesAvecSourceRecharge_ListeVide_RetourneListeVide() {
        // Act
        List<ZoneRecharge> villesAvecSourceRecharge = communaute.getVillesAvecSourceRecharge();
        // Assert
        assertNotNull(villesAvecSourceRecharge);
        assertTrue(villesAvecSourceRecharge.isEmpty());
    }

    @Test
    public void testGetVillesAvecSourceRecharge_ListeNull_RetourneNull() {
        // Arrange
        communaute.setZonesRecharge(null);
        // Act
        List<ZoneRecharge> villesAvecSourceRecharge = communaute.getVillesAvecSourceRecharge();
        // Assert
        assertNull(villesAvecSourceRecharge);
    }

    @Test
    public void testGetVillesRechargeesSansSource_ListeNonVide_RetourneListe() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterRecharge(new ZoneRecharge(villeA));
        communaute.ajouterRecharge(new ZoneRecharge(villeB));
        // Act
        List<ZoneRecharge> villesRechargeesSansSource = communaute.getVillesRechargeesSansSource();
        // Assert
        assertNotNull(villesRechargeesSansSource);
        assertFalse(villesRechargeesSansSource.isEmpty());
        assertTrue(villesRechargeesSansSource.stream().noneMatch(ZoneRecharge::estSourceRecharge));
    }

    @Test
    public void testGetVillesRechargeesSansSource_ListeVide_RetourneListeVide() {
        // Act
        List<ZoneRecharge> villesRechargeesSansSource = communaute.getVillesRechargeesSansSource();
        // Assert
        assertNotNull(villesRechargeesSansSource);
        assertTrue(villesRechargeesSansSource.isEmpty());
    }

    @Test
    public void testGetVillesSansZoneRecharge_ListeNonVide_RetourneListe() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        // Act
        List<Ville> villesSansZoneRecharge = communaute.getVillesSansZoneRecharge();
        // Assert
        assertNotNull(villesSansZoneRecharge);
        assertFalse(villesSansZoneRecharge.isEmpty());
        assertTrue(villesSansZoneRecharge.contains(villeA));
        assertTrue(villesSansZoneRecharge.contains(villeB));
    }

    @Test
    public void testGetVillesSansZoneRecharge_ListeVide_RetourneListeVide() {
        // Act
        List<Ville> villesSansZoneRecharge = communaute.getVillesSansZoneRecharge();
        // Assert
        assertNotNull(villesSansZoneRecharge);
        assertTrue(villesSansZoneRecharge.isEmpty());
    }

    @Test
    public void testPeutRetirerRecharge_VilleSansZoneDeRechargeEtVoisinSansZoneDeRecharge_RetourneFalse() {
        // Arrange
        Ville villeB = new Ville("VilleB");
        Ville voisin = new Ville("Voisin");
        communaute.ajouterVille(villeB);
        communaute.ajouterVille(voisin);
        // Act
        boolean peutRetirer = communaute.peutRetirerRecharge(villeB);
        // Assert
        assertFalse(peutRetirer);
    }

    @Test
    public void testAjouterVille_VilleNull_LanceIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> communaute.ajouterVille(null));
    }

    @Test
    void contientRecharge_VilleAvecZoneRecharge_RetourneTrue() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        communaute.ajouterVille(villeA);
        communaute.recharge(villeA.getNom());
        // Act
        boolean result = communaute.contientRecharge(villeA);
        // Assert
        assertTrue(result);
    }

    @Test
    void contientRecharge_VilleNull_LanceIllegalArgumentException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> communaute.contientRecharge(null));
    }

    @Test
    public void testScore_CommunauteAvecZonesDeRecharge_CalculScoreCorrect() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        communaute.ajouterVille(villeA);
        communaute.recharge("villeA");
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
    public void testIsProblematicCity_VilleNonProblematique_NonMarqueeCommeProblematique() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        // Act
        boolean result = communaute.isProblematicCity(villeA);
        // Assert
        assertFalse(result);
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
    public void testEstRelieeAvecBorne_VilleRelieeAvecBorne_RetourneTrue() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute("VilleA", "VilleB");
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
    public void testRespecteContrainte_VilleRespecteContrainte_Vrai() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRecharge(new ZoneRecharge(villeA));
        communaute.ajouterRoute("VilleA", "VilleB");
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

    @Test
    public void testRespecteContrainte_VilleNeRespectePasContrainte_Faux() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute("VilleA", "VilleB");
        // Act
        boolean result = communaute.respecteContrainte(villeA);
        // Assert
        assertFalse(result);
    }

    @Test
    void testAjouterRecharge() {
        ZoneRecharge zoneRecharge = new ZoneRecharge(new Ville("VilleA"));
        communaute.ajouterRecharge(zoneRecharge);
        assertTrue(communaute.getZonesRecharge().contains(zoneRecharge));
    }

    @Test
    void testContientRecharge() {
        Ville villeA = new Ville("VilleA");
        communaute.ajouterRecharge(new ZoneRecharge(villeA));
        assertTrue(communaute.contientRecharge(villeA));
    }
    
    @Test
    void testIsProblematicCity() {
        Ville problematicVille = new Ville("ProblematicVille");
        communaute.markAsProblematicCity(problematicVille);
        assertTrue(communaute.isProblematicCity(problematicVille));
    }

    @Test
    void testEstRelieeAvecBorne() {
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterRecharge(new ZoneRecharge(villeA));
        communaute.ajouterRecharge(new ZoneRecharge(villeB));
        assertFalse(communaute.estRelieeAvecBorne(villeA));
        assertFalse(communaute.estRelieeAvecBorne(villeB));
    }
    
    @Test
    public void testAjouterVille() {
    	communaute.ajouterVille(new Ville("Paris"));
        assertEquals(1, communaute.getVillesSansZoneRecharge().size());
    }

    @Test
    public void testAjouterRoute() {
    	communaute.ajouterVille(new Ville("Paris"));
    	communaute.ajouterVille(new Ville("Lyon"));
        assertFalse(communaute.contientRoute(new Ville("Paris"),new Ville("Lyon")));
    }

    @Test
    public void testRecharge() {
    	communaute.ajouterVille(new Ville("Paris"));
    	communaute.recharge("Paris");
        assertTrue(communaute.trouverVilleParNom("Paris").getZoneDeRecharge());
    }

    @Test
    public void testContientRechargeAvecVilleNull() {
        assertThrows(IllegalArgumentException.class,()->communaute.contientRecharge(null));
    }

    @Test
    public void testGenererSolutionInitialeAvecVilles() {
    	communaute.ajouterVille(new Ville("Paris"));
    	communaute.ajouterVille(new Ville("Lyon"));
    	communaute.genererSolutionInitiale();
        assertEquals(2, communaute.getZonesRecharge().size());
    }

    @Test
    public void testResoudreAutomatiquementAvecCommunauteVide() {
        assertDoesNotThrow(communaute::resoudreAutomatiquement);
    }

    @Test
    public void testChoisirVilleOptimaleAvecCommunauteVide() {
        assertNull(communaute.choisirVilleOptimale());
    }

    @Test
    public void testEstRelieeAvecBorneAvecVilleNull() {
        assertThrows(IllegalArgumentException.class, () -> communaute.estRelieeAvecBorne(null));
    }
}
