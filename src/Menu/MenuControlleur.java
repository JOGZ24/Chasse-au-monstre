/**
 * La classe MenuControlleur est le contrôleur associé à l'interface utilisateur du menu principal du jeu.
 * Elle implémente l'interface Initializable de JavaFX pour l'initialisation des composants.
 *
 * Les fonctionnalités principales de cette classe incluent la gestion des actions liées aux boutons du menu,
 * la récupération des options sélectionnées par l'utilisateur, et la navigation entre différentes vues du jeu.
 */
package Menu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Model.GameModel;
import Model.Hunter;
import Model.HunterStrategy;
import Model.Monster;
import View.GameView;
import View.VueHunter;
import View.VueMonster;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * La classe MenuControlleur implémente l'interface Initializable de JavaFX.
 * Elle gère les actions liées au menu principal du jeu.
 */
public class MenuControlleur implements Initializable {

    // Déclarations de membres pour la gestion de la scène et des composants
    // graphiques du menu
    Stage stage;
    Parent root;
    Scene scene;

    // Déclarations de composants graphiques utilisés dans le menu
    @FXML
    TextField taille;

    @FXML
    Label label;

    @FXML
    TextField pourcentage;

    @FXML
    ComboBox<String> comboBox;

    @FXML
    ComboBox<String> comboBox2;

    @FXML
    ChoiceBox<String> comboBox3;

    @FXML
    ChoiceBox<String> fog;

    @FXML
    Slider sliderObstacle;

    @FXML
    Slider sliderTaille;

    @FXML
    Label labelPlateau;

    @FXML
    Label labelObstacle;

    @FXML
    ChoiceBox<String> laby;

    // Tableaux de valeurs pour les options du jeu
    String[] players = { "bot", "joueur" };
    String[] affichages = { "1 fenêtre", "2 fenêtres" };
    String[] brouillard = { "Activé ( brouillard de 1 )", "Activé ( brouillard de 2 )", "Désactivé" };
    String[] labyrinthOption = { "Activé", "Désactivé" };

    // Variables statiques pour stocker les options sélectionnées
    public static int intTaillePlateau;
    public static int intProbaWall;
    public static String affichage;
    public static String fogOfWar;
    public static String labyrinth;

    // Chemins vers les feuilles de style CSS
    private String cssMenu = this.getClass().getResource("/res/css/application.css").toExternalForm();
    private String cssOption = this.getClass().getResource("/res/css/option.css").toExternalForm();

    /**
     * Méthode appelée lors du clic sur le bouton "Option". Charge la vue des
     * options du jeu.
     *
     * @param e Objet ActionEvent représentant l'événement de clic.
     * @throws IOException En cas d'erreur lors du chargement de la vue des options.
     */
    public void option(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/res/fxml/Option.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(cssOption);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Méthode appelée lors du clic sur le bouton "Retour". Enregistre les options
     * sélectionnées
     * par l'utilisateur et retourne à la vue du menu principal.
     *
     * @param e Objet ActionEvent représentant l'événement de clic.
     * @throws IOException En cas d'erreur lors du chargement de la vue du menu
     *                     principal.
     */
    public void retour(ActionEvent e) throws IOException {
        try {
            intTaillePlateau = (int) sliderTaille.getValue();
            intProbaWall = (int) sliderObstacle.getValue();
            affichage = comboBox3.getValue();
            fogOfWar = fog.getValue();
            labyrinth = laby.getValue();
        } catch (Exception exception) {
            exception.getMessage();
        }
        Parent root = FXMLLoader.load(getClass().getResource("/res/fxml/Menu.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.getStylesheets().add(cssMenu);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Méthode appelée lors du clic sur le bouton "Quitter". Ferme l'application.
     *
     * @param e Objet ActionEvent représentant l'événement de clic.
     * @throws IOException En cas d'erreur lors de la fermeture de l'application.
     */
    public void quitter(ActionEvent e) throws IOException {
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Méthode appelée lors du clic sur le bouton "Jouer". Charge la vue de
     * préparation du jeu.
     *
     * @param e Objet ActionEvent représentant l'événement de clic.
     * @throws IOException En cas d'erreur lors du chargement de la vue de
     *                     préparation du jeu.
     */
    public void jouer(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/res/fxml/PreGame.fxml"));
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Méthode appelée lors du clic sur le bouton "Play". Initialise le jeu en
     * fonction des options
     * sélectionnées par l'utilisateur et affiche les vues du chasseur et du
     * monstre.
     *
     * @param e Objet ActionEvent représentant l'événement de clic.
     * @throws IOException En cas d'erreur lors de l'initialisation et de
     *                     l'affichage du jeu.
     */
    public void play(ActionEvent e) {
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();

        int size = intTaillePlateau;
        int probaWall = intProbaWall;
        boolean isPredefined = false;

        if (size <= 0 || probaWall < 0) {
            size = 10;
            probaWall = 20;
        }

        if (labyrinth != null) {
            if (labyrinth.equals("Activé")) {
                size = 10;
                isPredefined = true;
            }
        }

        GameModel gameModel = new GameModel(null, null, size, probaWall, isPredefined);

        Monster monster = new Monster("STYLESHEET_CASPIAN", gameModel);

        Hunter hunter = new Hunter("STYLESHEET_CASPIAN", gameModel, null);

        gameModel.setMonster(monster);
        gameModel.setHunter(hunter);
        boolean controlHunter = false;
        boolean controlMonster = false;

        try {
            controlHunter = comboBox2.getValue().equals("bot");
            controlMonster = comboBox.getValue().equals("bot");
        } catch (Exception exception) {
            exception.getMessage();
        }
        if (controlHunter) {
            hunter.setStrategy(new HunterStrategy());
            hunter.getStrategy().initialize(size, size);
        }

        VueHunter hunterView = new VueHunter(hunter, controlHunter);
        VueMonster monsterView = new VueMonster(monster, hunter, controlMonster);
        GameView gameView = new GameView(hunter, monster, 1);

        // Création des deux stages joueurs
        Stage hunterStage = hunterView.creerStage();
        Stage monsterStage = monsterView.creerStage();

        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getBounds().getWidth();
        double screenHeight = screen.getBounds().getHeight();

        // Divisez l'écran en deux en ajustant les dimensions et les positions des
        // stages
        // Screen 1
        hunterStage.setX(0);
        hunterStage.setY(0);
        hunterStage.setWidth(screenWidth / 2);
        hunterStage.setHeight(screenHeight);
        // Screen 2
        monsterStage.setX(screenWidth / 2);
        monsterStage.setY(0);
        monsterStage.setWidth(screenWidth / 2);
        monsterStage.setHeight(screenHeight);
        // Déplacez le deuxième stage à droite du premier
        monsterStage.setX(hunterStage.getX() + hunterStage.getWidth());
        monsterStage.setY(hunterStage.getY());
        hunterStage.sizeToScene();
        monsterStage.sizeToScene();

        if (fogOfWar != null) {
            if (fogOfWar.equals("Activé ( brouillard de 1 )")) {
                gameView.getVueMonster().setFogEnabled(true, 1);
                monsterView.setFogEnabled(true, 1);
            } else if (fogOfWar.equals("Activé ( brouillard de 2 )")) {
                gameView.getVueMonster().setFogEnabled(true, 2);
                monsterView.setFogEnabled(true, 2);
            }
        }

        if (affichage != null) {
            if (affichage.equals("1 fenêtre")) {
                gameView.show();
            } else {
                if (!controlHunter && controlMonster) {
                    hunterStage.show();
                } else if (!controlMonster && controlHunter) {
                    monsterStage.show();
                } else {
                    monsterStage.show();
                    hunterStage.show();
                }
            }
        } else {
            if (!controlHunter && controlMonster) {
                hunterStage.show();
            } else if (!controlMonster && controlHunter) {
                monsterStage.show();
            } else {
                monsterStage.show();
                hunterStage.show();
            }
        }

    }

    /**
     * Méthode d'initialisation appelée lors de la création de la vue.
     * Initialise les composants graphiques et leurs écouteurs d'événements.
     *
     * @param arg0 URL de localisation non utilisée.
     * @param arg1 ResourceBundle non utilisée.
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        if (sliderTaille != null) {
            sliderTaille.valueProperty().addListener((observable, oldValue, newValue) -> {
                labelPlateau.setText(String.valueOf(newValue.intValue()));
            });
        }
        if (sliderObstacle != null) {
            sliderObstacle.valueProperty().addListener((observable, oldValue, newValue) -> {
                labelObstacle.setText(String.valueOf(newValue.intValue()));
            });
        }

        if (comboBox != null) {
            comboBox.getItems().addAll(players);
        }
        if (comboBox2 != null) {
            comboBox2.getItems().addAll(players);
        }

        if (comboBox3 != null) {
            comboBox3.getItems().addAll(affichages);
        }
        if (fog != null) {
            fog.getItems().addAll(brouillard);
        }
        if (laby != null) {
            laby.getItems().addAll(labyrinthOption);
        }
    }
}
