package test;

import phase2.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * Classe de tests unitaires pour la classe CommunauteAgglomeration
 */
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
    void testContientRecharge_AvecVilleAbsente_RetourneFalse() {
        // Arrange
        Ville villeA = new Ville("A");
        Ville villeB = new Ville("B");
        communaute.ajouterVille(villeA);
        // Act
        boolean result = communaute.contientRecharge(villeB);
        // Assert
        assertFalse(result);
    }

    @Test
    void contientRecharge_VilleNull_LanceIllegalArgumentException() {
        // Assert
        assertThrows(IllegalArgumentException.class, () -> communaute.contientRecharge(null));
    }

    @Test
    public void testScore_CommunauteAvecZonesDeRecharge_CalculScoreCorrect() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        communaute.ajouterVille(villeA);
        communaute.recharge("VilleA");
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
    public void testEstRelieeAvecBorneAvecVille_RetourneNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> communaute.estRelieeAvecBorne(null));
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
    void testTrierSommetsParDegree_CommunauteVide_RetourneListeVide() {
        // Act
        List<Ville> result = communaute.trierSommetsParDegree();
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void testTrierSommetsParDegree_CommunauteAvecVilles_RetourneListeTrie() {
        // Arrange
        Ville villeA = new Ville("A");
        Ville villeB = new Ville("B");
        // Ajouter des villes à votreObjet ou au graphe
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        // Act
        List<Ville> result = communaute.trierSommetsParDegree();
        // Assert
        assertEquals(2, result.size());
        assertEquals("B", result.get(0).getNom());
        assertEquals("A", result.get(1).getNom());
    }

    @Test
    void testAjouterRecharge_AvecZoneRechargeExistante_DevraitAjouterLaRecharge() {
        // Arrange
        Ville villeA = new Ville("VilleA");
        ZoneRecharge zoneRecharge = new ZoneRecharge(villeA);
        communaute.ajouterVille(villeA);
        // Act
        communaute.ajouterRecharge(zoneRecharge);
        // Assert
        assertTrue(communaute.getZonesRecharge().contains(zoneRecharge),
                "La liste des zones de recharge devrait contenir la zone ajoutée.");
    }

    @Test
    void testAjouterRecharge_ZoneRechargeNonNull_AjouteRecharge() {
        // Arrange
        ZoneRecharge zoneRecharge = new ZoneRecharge(new Ville("A"));
        // Act
        communaute.ajouterRecharge(zoneRecharge);
        // Assert
        List<ZoneRecharge> zonesRecharge = communaute.getZonesRecharge();
        assertTrue(zonesRecharge.contains(zoneRecharge), "La liste des zones de recharge devrait contenir la zone ajoutée.");
        assertEquals(1, zonesRecharge.size(), "La taille de la liste des zones de recharge devrait être égale à 1.");
    }

    @Test
    void testAjouterVille_VilleNonNull_AjouteVilleACommunaute() {
        // Arrange
        Ville villeA = new Ville("A");
        // Act
        communaute.ajouterVille(villeA);
        // Assert
        assertTrue(communaute.getVilles().contains(villeA), "La ville devrait être ajoutée à la communauté.");
    }

    @Test
    void testAjouterVille_VilleNull_LanceException() {
        // Arrange
        Ville villeNull = null;
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> communaute.ajouterVille(villeNull),
                "Devrait lancer une IllegalArgumentException lorsque la ville est null.");
        assertTrue(communaute.getVilles().isEmpty(), "Aucune ville ne devrait être ajoutée à la communauté.");
    }

    @Test
    public void testAjouterRoute_CommunauteSansRoute_EntreDeuxVilles() {
        // Act
    	communaute.ajouterVille(new Ville("A"));
    	communaute.ajouterVille(new Ville("B"));
        // Assert
        assertFalse(communaute.contientRoute(new Ville("A"),new Ville("B")));
    }

    @Test
    void testRecharge_VilleInexistante_AfficheMessage() {
        // Act
        communaute.recharge("C"); // Tenter d'ajouter une zone de recharge à une ville inexistante
        // Assert
        assertTrue(communaute.getZonesRecharge().isEmpty(),
                "La liste des zones de recharge devrait être vide si la ville est inexistante.");
    }

    @Test
    public void testContientRechargeAvecVilleNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,()->communaute.contientRecharge(null));
    }

    @Test
    void testRetirerRecharge_VilleSansRecharge_AfficheMessageErreur() {
        // Arrange
        Ville villeB = new Ville("B");
        communaute.ajouterVille(villeB);
        // Act & Assert
        assertDoesNotThrow(() -> communaute.retirerRecharge(villeB));
    }

    @Test
    void testRetirerRecharge_VilleAvecRechargeEtContrainteVoisinsNonRespectee_RetablitEtAfficheMessageErreur() {
        // Arrange
        Ville villeC = new Ville("C");
        Ville villeD = new Ville("D");
        communaute.ajouterVille(villeC);
        communaute.ajouterVille(villeD);
        communaute.recharge("C");
        communaute.ajouterRoute("C", "D");
        // Act
        communaute.retirerRecharge(villeC);
        // Assert
        assertTrue(villeC.getZoneDeRecharge());
    }

    @Test
    void testPeutRetirerRecharge_VilleAvecRecharge_RetourneTrue() {
        // Arrange
        Ville villeA = new Ville("A");
        communaute.ajouterVille(villeA);
        communaute.recharge("A");
        // Act
        boolean result = communaute.peutRetirerRecharge(villeA);
        // Assert
        assertTrue(result);
    }

    @Test
    void testPeutRetirerRecharge_VilleSansRechargeEtVoisinAvecRecharge_RetourneTrue() {
        // Arrange
        Ville villeA = new Ville("A");
        Ville villeB = new Ville("B");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.recharge("B");
        communaute.ajouterRoute("A", "B");
        // Act
        boolean result = communaute.peutRetirerRecharge(villeA);
        // Assert
        assertTrue(result);
    }

    @Test
    void testPeutRetirerRecharge_VilleSansRechargeEtVoisinSansRecharge_RetourneFalse() {
        // Arrange
        Ville villeA = new Ville("A");
        Ville villeB = new Ville("B");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute("A", "B");
        // Act
        boolean result = communaute.peutRetirerRecharge(villeA);
        // Assert
        assertFalse(result);
    }

    @Test
    void testContrainteVoisins_VilleAvecVoisinsAvecRecharge_RetourneTrue() {
        // Arrange
        Ville villeA = new Ville("A");
        Ville villeB = new Ville("B");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.recharge("A");
        communaute.ajouterRoute("A", "B");
        // Act
        boolean result = communaute.contrainteVoisins(villeA);
        // Assert
        assertTrue(result);
    }

    @Test
    void testContrainteVoisins_VilleAvecVoisinsSansRecharge_RetourneFalse() {
        // Arrange
        Ville villeA = new Ville("A");
        Ville villeB = new Ville("B");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute("A", "B");
        // Act
        boolean result = communaute.contrainteVoisins(villeA);
        // Assert
        assertFalse(result);
    }

    @Test
    void testContrainteVoisinsHelper_VilleAvecVoisinsAvecRecharge_RetourneTrue() {
        // Arrange
        Ville villeA = new Ville("A");
        Ville villeB = new Ville("B");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.recharge("A");
        communaute.ajouterRoute("A", "B");
        // Act
        boolean result = communaute.contrainteVoisinsHelper(villeA, new HashSet<>());
        // Assert
        assertTrue(result);
    }

    @Test
    void testContrainteVoisinsHelper_VilleSansVoisinAvecRecharge_RetourneTrue() {
        // Arrange
        Ville villeA = new Ville("A");
        communaute.ajouterVille(villeA);
        communaute.recharge("A");
        // Act
        boolean result = communaute.contrainteVoisinsHelper(villeA, new HashSet<>());
        // Assert
        assertTrue(result);
    }

    @Test
    void testContrainteVoisinsHelper_VilleAvecVoisinsSansRecharge_RetourneFalse() {
        // Arrange
        Ville villeA = new Ville("A");
        Ville villeB = new Ville("B");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute("A", "B");
        // Act
        boolean result = communaute.contrainteVoisinsHelper(villeA, new HashSet<>());
        // Assert
        assertFalse(result);
    }

    @Test
    void testGenererSolutionInitiale_CommunauteAvecVilles_GenereSolutionCorrecte() {
        // Arrange
        Ville villeA = new Ville("A");
        Ville villeB = new Ville("B");
        // Ajouter des villes à votre objet ou au graphe
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        // Act
        communaute.genererSolutionInitiale();
        // Assert
        for (Ville ville : communaute.getVilles()) {
            assertTrue(communaute.contientRecharge(ville));
        }
        assertEquals(communaute.getVilles().size(), communaute.getZonesRecharge().size());
        for (ZoneRecharge zoneRecharge : communaute.getZonesRecharge()) {
            assertEquals(zoneRecharge.getVille(), communaute.trouverVilleParNom(zoneRecharge.getVille().getNom()));
        }
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
    public void testResoudreAutomatiquement_RetourneCommunauteVide() {
        // Act & Assert
        assertDoesNotThrow(communaute::resoudreAutomatiquement);
    }

    @Test
    void testResoudreAutomatiquement_CommunauteAvecVilles_RetourneResultatAttendu() {
        int scoreInitial = communaute.score();
        List<ZoneRecharge> zonesRechargeInitiales = new ArrayList<>(communaute.getZonesRecharge());
        // Act
        communaute.resoudreAutomatiquement();
        // Assert
        assertTrue(communaute.score() <= scoreInitial);
        assertEquals(zonesRechargeInitiales.size(), communaute.getZonesRecharge().size());
        for (ZoneRecharge zoneRecharge : communaute.getZonesRecharge()) {
            assertNotNull(zoneRecharge.getVille());
            assertTrue(communaute.getVilles().contains(zoneRecharge.getVille()));
        }
    }
}
