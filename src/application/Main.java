package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.stream.Collectors;

public class Main extends Application {
    private CommunauteAgglomeration communaute = new CommunauteAgglomeration();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Communaute Agglomeration");

        // Sélectionner le fichier de communauté
        File selectedFile = chooseCommunityFile(primaryStage);

        if (selectedFile != null) {
            // Charger la communauté depuis le fichier
            communaute.chargerCommunaute(selectedFile.getAbsolutePath());
            // Initialiser l'interface utilisateur
            initializeUI(primaryStage);
        } else {
            showAlert("Erreur", "Aucun fichier sélectionné. Fermeture de l'application.");
            primaryStage.close();
        }
    }

    // Méthode pour choisir le fichier de communauté
    private File chooseCommunityFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir le fichier de communauté");
        return fileChooser.showOpenDialog(primaryStage);
    }

    // Méthode pour initialiser l'interface utilisateur
    private void initializeUI(Stage primaryStage) {
        VBox root = createMainLayout(primaryStage);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode pour créer la disposition principale de l'interface utilisateur
    private VBox createMainLayout(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        Label scoreLabel = new Label("Score: " + communaute.score());
        root.getChildren().add(scoreLabel);

        Label avecRechargeLabel = new Label("Villes avec zone de recharge et rechargées :");
        Label rechargéesLabel = new Label("Villes rechargées :");
        Label sansRechargeLabel = new Label("Villes sans zone de recharge :");
        root.getChildren().addAll(avecRechargeLabel, rechargéesLabel, sansRechargeLabel);

        updateStatusLabel(avecRechargeLabel, rechargéesLabel, sansRechargeLabel);

        Button solveManuallyButton = createSolveManuallyButton(primaryStage, scoreLabel, avecRechargeLabel, rechargéesLabel, sansRechargeLabel);
        Button solveAutomaticallyButton = createSolveAutomaticallyButton(primaryStage, scoreLabel, avecRechargeLabel, rechargéesLabel, sansRechargeLabel);
        Button saveButton = createSaveButton(primaryStage);
        Button exitButton = createExitButton(primaryStage);

        root.getChildren().addAll(solveManuallyButton, solveAutomaticallyButton, saveButton, exitButton);

        return root;
    }

    // Méthode pour mettre à jour les labels d'état des villes
    private void updateStatusLabel(Label avecRechargeLabel, Label rechargéesLabel, Label sansRechargeLabel) {
        avecRechargeLabel.setText("Villes avec zone de recharge et rechargées : " +
                communaute.getVillesAvecSourceRecharge().stream().map(charger -> charger.getVille().getNom()).collect(Collectors.joining(", ")));
        rechargéesLabel.setText("Villes rechargées : " +
                communaute.getVillesRechargeesSansSource().stream().map(charger -> charger.getVille().getNom()).collect(Collectors.joining(", ")));
        sansRechargeLabel.setText("Villes sans zone de recharge : " +
                communaute.getVillesSansZoneRecharge().stream().map(Ville::getNom).collect(Collectors.joining(", ")));
    }

    // Méthode pour créer le bouton de résolution manuelle
    private Button createSolveManuallyButton(Stage primaryStage, Label scoreLabel, Label avecRechargeLabel, Label rechargéesLabel, Label sansRechargeLabel) {
        Button solveManuallyButton = new Button("Résoudre manuellement");
        solveManuallyButton.setOnAction(e -> {
            // Générer une solution initiale si nécessaire
            if (communaute.getcharger().isEmpty()) {
                communaute.genererSolutionInitiale();
            }
            // Créer une nouvelle fenêtre pour la résolution manuelle
            Stage manualStage = new Stage();
            manualStage.setTitle("Résolution Manuelle");

            VBox manualRoot = new VBox(10);
            manualRoot.setAlignment(Pos.CENTER);

            Label manualScoreLabel = new Label("Score: " + communaute.score());
            manualRoot.getChildren().add(manualScoreLabel);

            Label manualAvecRechargeLabel = new Label("Villes avec zone de recharge et rechargées :");
            Label manualRechargéesLabel = new Label("Villes rechargées :");
            Label manualSansRechargeLabel = new Label("Villes sans zone de recharge :");
            manualRoot.getChildren().addAll(manualAvecRechargeLabel, manualRechargéesLabel, manualSansRechargeLabel);

            TextField villeTextField = new TextField();
            villeTextField.setPromptText("Nom de la ville");

            Button addRechargeButton = new Button("Ajouter une zone de recharge");
            addRechargeButton.setOnAction(event -> {
                String nomVille = villeTextField.getText();
                communaute.recharge(nomVille);

                manualScoreLabel.setText("Score: " + communaute.score());
                updateStatusLabel(manualAvecRechargeLabel, manualRechargéesLabel, manualSansRechargeLabel);
            });

            Button removeRechargeButton = new Button("Retirer une zone de recharge");
            removeRechargeButton.setOnAction(event -> {
                String nomVille = villeTextField.getText();
                Ville ville = communaute.trouverVilleParNom(nomVille);

                if (ville != null) {
                    communaute.retirerZoneRechargeMenu(ville);
                    manualScoreLabel.setText("Score: " + communaute.score());
                    updateStatusLabel(manualAvecRechargeLabel, manualRechargéesLabel, manualSansRechargeLabel);
                }
            });

            Button finishButton = new Button("Fin");
            finishButton.setOnAction(event -> {
                manualStage.close();
                updateStatusLabel(avecRechargeLabel, rechargéesLabel, sansRechargeLabel);
                scoreLabel.setText("Score: " + communaute.score());
            });

            manualRoot.getChildren().addAll(villeTextField, addRechargeButton, removeRechargeButton, finishButton);

            Scene manualScene = new Scene(manualRoot, 300, 250);
            manualStage.setScene(manualScene);
            manualStage.show();

            updateStatusLabel(manualAvecRechargeLabel, manualRechargéesLabel, manualSansRechargeLabel);
        });

        return solveManuallyButton;
    }

    // Méthode pour créer le bouton de résolution automatique
    private Button createSolveAutomaticallyButton(Stage primaryStage, Label scoreLabel, Label avecRechargeLabel, Label rechargéesLabel, Label sansRechargeLabel) {
        Button solveAutomaticallyButton = new Button("Résoudre automatiquement");
        solveAutomaticallyButton.setOnAction(e -> {
            // Boîte de dialogue pour obtenir le nombre d'itérations
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nombre d'itérations");
            dialog.setHeaderText("Veuillez entrer le nombre d'itérations :");
            dialog.setContentText("Nombre d'itérations:");

            int nombreIterations;
            try {
                // Récupérer le nombre d'itérations depuis la boîte de dialogue
                nombreIterations = Integer.parseInt(dialog.showAndWait().orElse("0"));
                // Résoudre automatiquement et mettre à jour les labels
                communaute.resoudreAutomatiquementAlgo2(nombreIterations);
                scoreLabel.setText("Score: " + communaute.score());
                updateStatusLabel(avecRechargeLabel, rechargéesLabel, sansRechargeLabel);
            } catch (NumberFormatException ex) {
                showAlert("Erreur", "Veuillez entrer un nombre valide.");
            }
        });

        return solveAutomaticallyButton;
    }

    // Méthode pour créer le bouton de sauvegarde
    private Button createSaveButton(Stage primaryStage) {
        Button saveButton = new Button("Sauvegarder");
        saveButton.setOnAction(e -> {
            // Boîte de dialogue pour choisir l'emplacement de sauvegarde
            FileChooser saveFileChooser = new FileChooser();
            saveFileChooser.setTitle("Choisir l'emplacement de sauvegarde");
            File saveFile = saveFileChooser.showSaveDialog(primaryStage);

            if (saveFile != null) {
                // Sauvegarder la solution et afficher une alerte
                communaute.sauvegarderSolution(saveFile.getAbsolutePath());
                showAlert("Sauvegarde", "Solution sauvegardée avec succès !");
            }
        });

        return saveButton;
    }

    // Méthode pour créer le bouton de sortie
    private Button createExitButton(Stage primaryStage) {
        Button exitButton = new Button("Quitter");
        exitButton.setOnAction(e -> primaryStage.close());

        return exitButton;
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}