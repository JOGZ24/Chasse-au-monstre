package View;

import java.util.ArrayList;

import Controller.ControlHunter;
import Controller.ControlHunterBot;
import Controller.ControlHunterPlayer;
import Model.GameModel;
import Model.Hunter;
import Utils.Coordinate;
import Utils.Maps;
import Utils.Observer;
import Utils.Subject;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * La classe VueHunter est une classe qui gère l'interface utilisateur pour le
 * chasseur dans le jeu. Elle affiche la grille de jeu, les informations
 * actuelles, et elle permet au joueur (ou à l'IA) de faire des mouvements.
 */
public class VueHunter extends View.AbstractView implements Observer {
    /** Le chasseur associé à cette vue. */
    private Hunter hunter;

    /** Le panneau de grille de jeu. */
    private GridPane gridPane;

    /** Le contrôleur associé à cette vue. */
    private ControlHunter controlleur;

    /** Le stage (fenêtre) associé à cette vue. */
    private Stage stage;

    /** Le label affichant les informations actuelles. */
    private Label currentLabel;

    private VBox mainBox;

    private boolean tipAvailable;

    /**
     * Constructeur de la classe VueHunter.
     *
     * @param hunter  Le chasseur associé à la vue.
     * @param control Un booléen indiquant si le chasseur est contrôlé par un
     *                joueur ou par une IA.
     */
    public VueHunter(Hunter hunter, boolean control) {
        this.hunter = hunter;
        if (!control) {
            this.controlleur = new ControlHunterPlayer(this);
        } else {
            this.controlleur = new ControlHunterBot(this);
        }
        this.stage = creerStage();
        hunter.attach(this);
        this.tipAvailable = true;
    }

    /**
     * Getter pour le panneau de grille de jeu.
     *
     * @return Le panneau de grille de jeu.
     */
    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * Getter pour le chasseur associé à la vue.
     *
     * @return Le chasseur associé à la vue.
     */
    public Hunter getHunter() {
        return hunter;
    }

    /**
     * Getter pour le label affichant les informations actuelles.
     *
     * @return Le label affichant les informations actuelles.
     */
    public Label getCurrentLabel() {
        return currentLabel;
    }

    /**
     * Setter pour le label affichant les informations actuelles.
     *
     * @param currentLabel Le nouveau label affichant les informations actuelles.
     */
    public void setCurrentLabel(Label currentLabel) {
        this.currentLabel = currentLabel;
    }

    public Scene getScene() {
        return stage.getScene();
    }

    /**
     * Crée et configure le stage (fenêtre) associé à cette vue.
     *
     * @return Le stage créé.
     */
    public Stage creerStage() {
        Stage stage = new Stage();
        gridPane = new GridPane();
        HBox hbox = new HBox(new Label("HUNTER"));
        hbox.setAlignment(Pos.CENTER);

        int imageSize = 50; // Taille de l'image ajustée à 50
        chargePlateau(-1, -1, imageSize);
        controlleur.hMouvement();
        styleGridPane(gridPane, imageSize);

        int rowCount = hunter.getGameModel().getMap().getRow() + 2;
        int colCount = hunter.getGameModel().getMap().getCol() + 2;
        VBox vboxCurrent = new VBox();
        currentLabel = new Label("C'est au tour du Monstre");

        VBox tips = new VBox();
        Button tip = new Button("Aide");
        tips.getChildren().add(tip);
        tip.setOnAction(e -> {
            showTipMessage();
        });
        tip.setAlignment(Pos.TOP_RIGHT);
        vboxCurrent.getChildren().add(currentLabel);
        vboxCurrent.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(hbox, vboxCurrent, tips, gridPane);
        this.mainBox = vbox;
        Scene scene = new Scene(vbox, colCount * 40, rowCount * 40);
        stage.setScene(scene);
        return stage;
    }

    public VBox getMainBox() {
        return mainBox;
    }

    /**
     * Charge la grille de jeu dans le panneau de la grille.
     *
     * @param row       L'indice de la ligne cliquée.
     * @param col       L'indice de la colonne cliquée.
     * @param imageSize La taille des images.
     */
    public void chargePlateau(int row, int col, double imageSize) {
        gridPane.getChildren().clear();
        Maps map = GameModel.map;
        boolean[][] mapShoot = map.getMapShoot();
        CellInfo[][] maps = map.getMaps();

        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[i].length; j++) {
                Image image = new Image(getClass().getResourceAsStream("carrenoir.png"));
                StackPane stackPane = createStackPaneWithBorder(map.getMaps()[i][j]);
                ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                imageView.setImage(image);

                if (mapShoot[i][j]) {
                    stackPane = createStackPaneWithBorder(map.getMaps()[i][j]);
                    stackPane.setStyle("-fx-border-color: red; -fx-border-width: 1;"); // Bordure rouge pour les tirs
                }
                gridPane.add(stackPane, j, i);
            }
        }
    }

    /**
     * Met à jour la grille de jeu après un mouvement du chasseur.
     */
    public void updatePlateau() {
        // Récupération des coordonnées de la case cliquée
        Coordinate clickedCoordinates = controlleur.getClickedCase();
        int clickedRow = clickedCoordinates.getRow();
        int clickedCol = clickedCoordinates.getCol();

        // Récupération du nœud correspondant à la case cliquée dans le GridPane
        Node node = getNodeByRowColumnIndex(clickedRow, clickedCol, gridPane);

        if (node != null && node instanceof StackPane) {
            StackPane existingStackPane = (StackPane) node;

            // Mise à jour de l'image de la case cliquée
            updateClickedImage(existingStackPane, clickedRow, clickedCol);

            // Vérification et ajout d'un label s'il y a un chemin
            addPathLabel(existingStackPane, clickedRow, clickedCol);

            // Réarrangement du panneau dans le GridPane
            existingStackPane.toBack();
        }
    }

    /**
     * Met à jour l'image de la case cliquée.
     *
     * @param existingStackPane Le StackPane existant pour la case cliquée.
     * @param clickedRow        L'indice de la ligne cliquée.
     * @param clickedCol        L'indice de la colonne cliquée.
     */
    private void updateClickedImage(StackPane existingStackPane, int clickedRow, int clickedCol) {
        String imagePathClicked = determineImagePath(hunter.getGameModel().getMap().getMaps()[clickedRow][clickedCol]);
        Image imageClicked = new Image(getClass().getResourceAsStream(imagePathClicked));
        ImageView existingImageView = (ImageView) existingStackPane.getChildren().get(0);
        existingImageView.setImage(imageClicked);
    }

    /**
     * Ajoute un label avec le chemin s'il y a un chemin dans la case cliquée.
     *
     * @param existingStackPane Le StackPane existant pour la case cliquée.
     * @param clickedRow        L'indice de la ligne cliquée.
     * @param clickedCol        L'indice de la colonne cliquée.
     */
    private void addPathLabel(StackPane existingStackPane, int clickedRow, int clickedCol) {
        if (hunter.getGameModel().getPath().containsKey(new Coordinate(clickedRow, clickedCol))) {
            int path = hunter.getGameModel().getPath(new Coordinate(clickedRow, clickedCol));
            Label label = new Label(String.valueOf(path));
            existingStackPane.getChildren().add(label);
        }
    }

    private void showRangeOfMonster(int range) {
        ObservableList<Node> children = gridPane.getChildren();

        resetAllEffect(gridPane);

        // Calculez la nouvelle portée du monstre
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        int monsterRow = hunter.getGameModel().getMonster().getCordMonster().getRow();
        int monsterCol = hunter.getGameModel().getMonster().getCordMonster().getCol();

        for (int i = 0; i < hunter.getGameModel().getSizeOfMap(); i++) {
            for (int j = 0; j < hunter.getGameModel().getSizeOfMap(); j++) {
                if (!hunter.isWithinRange(monsterRow, monsterCol, i, j, range)) {
                    Coordinate newCoord = new Coordinate(i, j);
                    if (!coordinates.contains(newCoord)) {
                        coordinates.add(newCoord);
                    }
                }
            }
        }

        // Appliquez l'effet uniquement aux cases dans la portée du monstre
        for (Node node : children) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                Integer colIndex = GridPane.getColumnIndex(node);
                Integer rowIndex = GridPane.getRowIndex(node);

                if (colIndex != null && rowIndex != null) {
                    for (Coordinate coord : coordinates) {
                        if (coord.getRow() == rowIndex && coord.getCol() == colIndex) {
                            // Réinitialisez l'effet avant de l'appliquer
                            stackPane.setEffect(null);
                            applyColorEffect(stackPane, Color.ALICEBLUE);
                        }
                    }
                }
            }
        }
    }

    private void applyColorEffect(StackPane stackPane, Color newColor) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.80);

        stackPane.setEffect(colorAdjust);
        stackPane.setBlendMode(BlendMode.MULTIPLY);
    }

    public void showTipMessage() {
        Stage tipStage = new Stage();
        tipStage.setTitle("Message d'aide ");
        Label text = new Label(
                "Si vous voulez de l'aide vous pouvez appuyer \n sur boutton juste en dessous,\n cela permettra d'activer une couleur blachâtre sur la map, \n cette zone signifie \n que le monstre se situe quelque part dans cette zone au moment \n ou vous activez l'aide. Cette aide n'est valable \n qu'une seule fois ;)");
        Label alreadyUse = new Label();
        Button activateTip = new Button("Activer l'aider");
        Button backToGame = new Button("Retourner au jeu ");
        text.setAlignment(Pos.CENTER);
        activateTip.setOnAction(e -> {
            if (tipAvailable) {
                showRangeOfMonster((int) hunter.getGameModel().getSizeOfMap() / 4);
                tipAvailable = !tipAvailable;
            } else {
                alreadyUse.setText("Tu a déja utiliser ton aide !");
            }
        });
        alreadyUse.setStyle("-fx-text-fill: red;");

        backToGame.setOnAction(e -> {
            tipStage.close();
        });
        VBox vbox = new VBox(text, alreadyUse, activateTip, backToGame);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        Scene tipScene = new Scene(vbox, 400, 340);
        tipStage.setScene(tipScene);
        tipStage.show();
    }

    /**
     * Getter pour le stage (fenêtre) associé à cette vue.
     *
     * @return Le stage associé à cette vue.
     */
    public Stage getStage() {
        return this.stage;
    }

    @Override
    public void update(Subject subj) {
        update(subj, null);
    }

    @Override
    public void update(Subject subj, Object data) {
        updatePlateau();
    }

}
