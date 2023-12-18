package application;

import phase2.*;


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
	 * @throws NullPointerException Si la méthode lance une NullPointerException
	 * @throws IllegalArgumentException Si la ville est nulle
	 */
	public void retirerZoneRechargeMenu(Ville ville) {
		try {
			// Vérifier si la ville n'est pas nulle
			if (ville != null) {
				retirerRecharge(ville); // Appeler la méthode retirerRecharge pour retirer la zone de recharge de la ville
			} else {
				System.out.println("Ville non trouvée. Veuillez réessayer."); // Afficher un message si la ville n'a pas été trouvée
			}
		} catch (NullPointerException e) {
			// Gérer spécifiquement une éventuelle NullPointerException
			System.out.println("NullPointerException lors du retrait de la zone de recharge : " + e.getMessage());
		} catch (IllegalArgumentException e) {
			// Gérer spécifiquement une éventuelle IllegalArgumentException
			System.out.println("IllegalArgumentException lors du retrait de la zone de recharge : " + e.getMessage());
		}
	}
}
