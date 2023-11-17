package test;

import main2.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class CommunauteAgglomerationTest {

    @Test
    public void testLireEntier_EntrerEntier_RetourneEntier() {
        // Arrange
        Scanner scanner = new Scanner("42\n");
        // Act
        CommunauteAgglomeration communaute = new CommunauteAgglomeration();
        int result = communaute.lireEntier(scanner);
        // Assert
        assertEquals(42, result);
    }

    @Test
    public void testLireEntier_EntrerNonEntier_RetourneEntier() {
        // Arrange
        Scanner scanner = new Scanner("abc\n123\n");
        // Act
        CommunauteAgglomeration communaute = new CommunauteAgglomeration();
        int result = communaute.lireEntier(scanner);
        // Assert
        assertEquals(123, result);
    }

    @Test
    public void testTrouverVilleParNom_VilleExistante_RetourneVille() {
        // Arrange
        CommunauteAgglomeration communaute = new CommunauteAgglomeration();
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
    public void testTrouverSolutionManuelle_AjouterEtRetirerZoneRecharge() {
        // Arrange
        CommunauteAgglomeration communaute = new CommunauteAgglomeration();
        InputStream fakeInput = new ByteArrayInputStream("1\nVille1\n2\nVille1\n3\n".getBytes());
        System.setIn(fakeInput);
        // Act
        communaute.trouverSolutionManuelle();

        // Assert
        // Ajoute une assertion appropriée pour vérifier si la solution manuelle a été effectuée correctement
        // Par exemple, tu peux vérifier que la liste des chargeurs a été modifiée conformément aux actions de l'utilisateur
    }

    @Test
    public void testAjouterRoute_VillesExistantes_AjouteRoute() {
        // Arrange
        CommunauteAgglomeration communaute = new CommunauteAgglomeration();
        communaute.ajouterVille(new Ville("Ville1"));
        communaute.ajouterVille(new Ville("Ville2"));

        // Act
        communaute.ajouterRoute("Ville1", "Ville2");

        // Assert
        // Ajoute une assertion appropriée pour vérifier si la route a été ajoutée correctement entre les deux villes
        // Par exemple, tu peux vérifier la présence de la route dans la liste des routes de la communauté
    }

    @Test
    public void testAjouterRoute_VilleInexistante_AfficheMessageErreur() {
        // Arrange
        CommunauteAgglomeration communaute = new CommunauteAgglomeration();
        communaute.ajouterVille(new Ville("Ville1"));

        // Act
        communaute.ajouterRoute("Ville1", "Ville2");

        // Assert
        // Ajoute une assertion appropriée pour vérifier si un message d'erreur est affiché
    }

    @Test
    public void testAjusterZonesRechargeConnectees_AjouteZonesRecharge() {
        // Arrange
        CommunauteAgglomeration communaute = new CommunauteAgglomeration();
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterRoute(villeA, villeB);

        // Act
        //communaute.ajusterZonesRechargeConnectees(villeA);

        // Assert
        //assertTrue(communaute.contientZoneRecharge(villeB));
    }
/*
    public void testAfficherVillesAvecOuSansRecharge_AfficheCorrectement() {
        // Arrange
        CommunauteAgglomeration communaute = new CommunauteAgglomeration();
        Ville villeA = new Ville("VilleA");
        Ville villeB = new Ville("VilleB");
        Charger chargerA = new Charger(villeA);
        Charger chargerB = new Charger(villeB);
        communaute.ajouterVille(villeA);
        communaute.ajouterVille(villeB);
        communaute.ajouterCharger(chargerA);
        communaute.ajouterCharger(chargerB);

        // Act
        // Rediriger la sortie standard pour capturer les impressions dans une chaîne
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        communaute.afficherVillesAvecOuSansRecharge();
        String output = outputStream.toString();

        // Assert
        // Ajoute des assertions appropriées pour vérifier que la sortie affiche correctement les villes avec ou sans recharge
        assertTrue(output.contains("Villes rechargées avec leurs propres zone de recharge"));
        assertTrue(output.contains("- " + villeA.getNom()));
        assertTrue(output.contains("Villes non rechargées"));
        assertTrue(output.contains("- " + villeB.getNom()));
    }
 */


    // Les autres tests ...
}
