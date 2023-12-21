package application;

import phase2.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.stream.Collectors;

/**
 * Classe principale de l'application, responsable de l'initialisation de l'interface utilisateur et du traitement des événements
 */
public class Main extends Application {
    // Instance de CommunauteAgglomeration pour la gestion des données de la communauté
    private final CommunauteAgglomeration communaute = new CommunauteAgglomeration();

    /**
     * Méthode principale pour lancer l'application JavaFX
     *
     * @param args Les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Méthode d'entrée de l'application JavaFX
     *
     * @param primaryStage La fenêtre principale de l'application
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Communauté Agglomeration"); // Définir le titre de la fenêtre principale
        File selectedFile = chooseCommunityFile(primaryStage); // Proposer à l'utilisateur de choisir un fichier de communauté
        // Charger les données de la communauté depuis le fichier sélectionné, s'il y en a un
        if (selectedFile != null) {
            communaute.chargerCommunaute(selectedFile.getAbsolutePath());
            initializeUI(primaryStage); // Initialiser l'interface utilisateur
        } else {
            // Afficher une alerte d'erreur et fermer l'application en cas de non-sélection de fichier
            showAlert("Erreur", "Aucun fichier sélectionné. Fermeture de l'application.");
            primaryStage.close();
        }
    }

    /**
     * Permet à l'utilisateur de choisir un fichier de communauté
     *
     * @param primaryStage La fenêtre principale de l'application
     * @return Le fichier choisi par l'utilisateur ou null s'il n'en a pas choisi
     */
    private File chooseCommunityFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir le fichier de communauté");
        return fileChooser.showOpenDialog(primaryStage);
    }

    /**
     * Initialise l'interface utilisateur
     *
     * @param primaryStage La fenêtre principale de l'application
     */
    private void initializeUI(Stage primaryStage) {
        VBox root = createMainLayout(primaryStage); // Créer la mise en page principale de l'interface utilisateur
        // Créer une scène avec la mise en page principale et la définir sur la fenêtre principale
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show(); // Afficher la fenêtre principale
    }

    /**
     * Crée la mise en page principale de l'interface utilisateur en utilisant un VBox
     *
     * @param primaryStage La fenêtre principale de l'application
     * @return Le VBox qui représente la mise en page principale
     */
    private VBox createMainLayout(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        Label scoreLabel = new Label("Score : " + communaute.score());
        root.getChildren().add(scoreLabel);
        // Labels pour afficher des informations sur les villes avec ou sans zones de recharge
        Label avecRechargeLabel = new Label("Villes avec zone de recharge et rechargées :");
        Label rechargeesLabel = new Label("Villes rechargées :");
        Label sansRechargeLabel = new Label("Villes sans zone de recharge :");
        root.getChildren().addAll(avecRechargeLabel, rechargeesLabel, sansRechargeLabel);
        // Bouton pour afficher le graphique
        Button showGraphButton = new Button("Afficher le graphique");
        showGraphButton.setOnAction(e -> showGraph());
        root.getChildren().add(showGraphButton);
        // Mettre à jour les labels d'état avec les données actuelles de la communauté
        updateStatusLabel(avecRechargeLabel, rechargeesLabel, sansRechargeLabel);
        // Boutons pour la résolution manuelle et automatique, la sauvegarde et la sortie
        Button solveManuallyButton = createSolveManuallyButton(scoreLabel, avecRechargeLabel, rechargeesLabel, sansRechargeLabel);
        Button solveAutomaticallyButton = createSolveAutomaticallyButton(scoreLabel, avecRechargeLabel, rechargeesLabel, sansRechargeLabel);
        Button saveButton = createSaveButton(primaryStage);
        Button exitButton = createExitButton(primaryStage);
        root.getChildren().addAll(solveManuallyButton, solveAutomaticallyButton, saveButton, exitButton);
        return root; // Retourner la mise en page principale
    }

    /**
     * Met à jour les labels d'état avec les données actuelles de la communauté
     *
     * @param avecRechargeLabel Label pour les villes avec zone de recharge et rechargées
     * @param rechargeesLabel Label pour les villes rechargées
     * @param sansRechargeLabel Label pour les villes sans zone de recharge
     */
    private void updateStatusLabel(Label avecRechargeLabel, Label rechargeesLabel, Label sansRechargeLabel) {
        avecRechargeLabel.setText("Villes avec zone de recharge et rechargées : " + communaute.getVillesAvecSourceRecharge().stream().map(charger -> charger.getVille().getNom()).collect(Collectors.joining(", ")));
        rechargeesLabel.setText("Villes rechargées : " + communaute.getVillesRechargeesSansSource().stream().map(charger -> charger.getVille().getNom()).collect(Collectors.joining(", ")));
        sansRechargeLabel.setText("Villes sans zone de recharge : " + communaute.getVillesSansZoneRecharge().stream().map(Ville::getNom).collect(Collectors.joining(", ")));
    }

    /**
     * Crée un bouton de résolution manuelle
     *
     * @param scoreLabel Label pour afficher le score actuel
     * @param avecRechargeLabel Label pour les villes avec zone de recharge et rechargées
     * @param rechargeesLabel Label pour les villes rechargées
     * @param sansRechargeLabel Label pour les villes sans zone de recharge
     * @return Le bouton de résolution manuelle
     */
    private Button createSolveManuallyButton(Label scoreLabel, Label avecRechargeLabel, Label rechargeesLabel, Label sansRechargeLabel) {
        Button solveManuallyButton = new Button("Résoudre manuellement");
        solveManuallyButton.setOnAction(e -> {
            // Générer une solution initiale si les zones de recharge sont vides
            if (communaute.getZonesRecharge().isEmpty() || communaute.getVillesSansZoneRecharge().size() > 0) {
                communaute.genererSolutionInitiale();
            }
            // Créer une nouvelle fenêtre pour la résolution manuelle
            Stage manualStage = new Stage();
            manualStage.setTitle("Résolution Manuelle");
            // Créer la mise en page pour la résolution manuelle
            VBox manualRoot = new VBox(10);
            manualRoot.setAlignment(Pos.CENTER);
            Label manualScoreLabel = new Label("Score : " + communaute.score());
            manualRoot.getChildren().add(manualScoreLabel);
            // Labels pour les informations sur les villes avec ou sans zones de recharge
            Label manualAvecRechargeLabel = new Label("Villes avec zone de recharge et rechargées :");
            Label manualRechargeesLabel = new Label("Villes rechargées :");
            Label manualSansRechargeLabel = new Label("Villes sans zone de recharge :");
            manualRoot.getChildren().addAll(manualAvecRechargeLabel, manualRechargeesLabel, manualSansRechargeLabel);
            // Champ de texte pour entrer le nom de la ville
            TextField villeTextField = new TextField();
            villeTextField.setPromptText("Nom de la ville");
            // Boutons pour ajouter et retirer une zone de recharge, et terminer la résolution manuelle
            Button addRechargeButton = new Button("Ajouter une zone de recharge");
            addRechargeButton.setOnAction(event -> {
                String nomVille = villeTextField.getText();
                communaute.recharge(nomVille);
                manualScoreLabel.setText("Score : " + communaute.score());
                updateStatusLabel(manualAvecRechargeLabel, manualRechargeesLabel, manualSansRechargeLabel);
            });
            Button removeRechargeButton = new Button("Retirer une zone de recharge");
            removeRechargeButton.setOnAction(event -> {
                String nomVille = villeTextField.getText();
                Ville ville = communaute.trouverVilleParNom(nomVille);
                if (ville != null) {
                    communaute.retirerZoneRechargeMenu(ville);
                    manualScoreLabel.setText("Score : " + communaute.score());
                    updateStatusLabel(manualAvecRechargeLabel, manualRechargeesLabel, manualSansRechargeLabel);
                }
            });
            Button finishButton = new Button("Fin");
            finishButton.setOnAction(event -> {
                // Fermer la fenêtre de résolution manuelle et mettre à jour les labels d'état
                manualStage.close();
                updateStatusLabel(avecRechargeLabel, rechargeesLabel, sansRechargeLabel);
                scoreLabel.setText("Score : " + communaute.score());
            });
            // Ajouter les composants à la mise en page manuelle
            manualRoot.getChildren().addAll(villeTextField, addRechargeButton, removeRechargeButton, finishButton);
            // Créer une scène pour la mise en page manuelle
            Scene manualScene = new Scene(manualRoot, 300, 250);
            manualStage.setScene(manualScene);
            // Afficher la fenêtre de résolution manuelle
            manualStage.show();
            // Mettre à jour les labels d'état de la résolution manuelle
            updateStatusLabel(manualAvecRechargeLabel, manualRechargeesLabel, manualSansRechargeLabel);
        });
        return solveManuallyButton; // Retourner le bouton de résolution manuelle
    }

    /**
     * Crée un bouton de résolution automatique
     *
     * @param scoreLabel Label pour afficher le score actuel
     * @param avecRechargeLabel Label pour les villes avec zone de recharge et rechargées
     * @param rechargeesLabel Label pour les villes rechargées
     * @param sansRechargeLabel Label pour les villes sans zone de recharge
     * @return Le bouton de résolution automatique
     */
    private Button createSolveAutomaticallyButton(Label scoreLabel, Label avecRechargeLabel, Label rechargeesLabel, Label sansRechargeLabel) {
        Button solveAutomaticallyButton = new Button("Résoudre automatiquement");
        solveAutomaticallyButton.setOnAction(e -> {
            communaute.resoudreAutomatiquement(); // Résoudre automatiquement la communauté
            scoreLabel.setText("Score : " + communaute.score()); // Mettre à jour le label du score et les labels d'état
            updateStatusLabel(avecRechargeLabel, rechargeesLabel, sansRechargeLabel);
        });
        return solveAutomaticallyButton;  // Retourner le bouton de résolution automatique
    }

    /**
     * Crée un bouton de sauvegarde
     *
     * @param primaryStage La fenêtre principale de l'application
     * @return Le bouton de sauvegarde
     */
    private Button createSaveButton(Stage primaryStage) {
        Button saveButton = new Button("Sauvegarder");
        saveButton.setOnAction(e -> {
            // Proposer à l'utilisateur de choisir l'emplacement de sauvegarde
            FileChooser saveFileChooser = new FileChooser();
            saveFileChooser.setTitle("Choisir l'emplacement de sauvegarde");
            File saveFile = saveFileChooser.showSaveDialog(primaryStage);
            // Sauvegarder la solution dans le fichier spécifié, s'il y en a un
            if (saveFile != null) {
                communaute.sauvegarderSolution(saveFile.getAbsolutePath());
                showAlert("Sauvegarde", "Solution sauvegardée avec succès !");
            }
        });
        return saveButton; // Retourner le bouton de sauvegarde
    }

    /**
     * Crée un bouton de sortie
     *
     * @param primaryStage La fenêtre principale de l'application
     * @return Le bouton de sortie
     */
    private Button createExitButton(Stage primaryStage) {
        Button exitButton = new Button("Quitter");
        exitButton.setOnAction(e -> primaryStage.close());
        return exitButton; // Retourner le bouton de sortie
    }

    /**
     * Affiche une alerte à l'utilisateur avec le titre et le contenu spécifiés.
     *
     * @param title Le titre de l'alerte
     * @param content Le contenu de l'alerte
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Affiche le graphique en temps réel des villes chargées
     */
    private void showGraph() {
        int villesAvecSourceRecharge = communaute.getVillesAvecSourceRecharge().size();
        int villesRechargeesSansSource = communaute.getVillesRechargeesSansSource().size();
        int villesTotales = communaute.getVilles().size();
        // Créer les séries de données pour le graphique
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("État de charge des villes");
        series.getData().add(new XYChart.Data<>("Rechargées avec source de recharge", villesAvecSourceRecharge));
        series.getData().add(new XYChart.Data<>("Rechargées sans source de recharge", villesRechargeesSansSource));
        series.getData().add(new XYChart.Data<>("Non rechargées", villesTotales - villesAvecSourceRecharge - villesRechargeesSansSource));
        // Créer le graphique en barres
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("État de charge des villes");
        barChart.getData().add(series);
        // Créer une nouvelle fenêtre pour afficher le graphique
        Stage stage = new Stage();
        stage.setTitle("Graphique d'état de charge");
        stage.setScene(new Scene(barChart, 400, 300));
        stage.show();
    }
}
