package application;

import phase2.*;
import java.io.*;
import java.util.*;
import java.util.stream.*;

/**
 * La classe CommunauteAgglomeration représente une communauté d'agglomération comprenant des villes, des routes et des zones de recharge
 * Elle utilise un graph pour représenter la connectivité entre les villes
 */
public class CommunauteAgglomeration extends phase2.CommunauteAgglomeration {
	/**
	 * Constructeur de la classe CommunauteAgglomeration
	 * Initialise les collections de villes, de routes, de chargeurs, et le graphe
	 */
    public CommunauteAgglomeration() {
		super();
    }

	/**
	 * Retire la zone de recharge d'une ville spécifiée
	 * Affiche des messages appropriés en fonction du résultat de l'opération
	 *
	 * @param ville La ville de laquelle retirer la zone de recharge
	 */
	public void retirerZoneRechargeMenu(Ville ville) {
		// Vérifier si la ville n'est pas nulle
		if (ville != null) {
			retirerRecharge(ville); // Appeler la méthode retirerRecharge pour retirer la zone de recharge de la ville
		} else {
			System.out.println("Ville non trouvée. Veuillez réessayer."); // Afficher un message si la ville n'a pas été trouvée
		}
	}
}
