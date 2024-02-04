package View;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import Controller.ControlMonster;
import Controller.ControlMonsterBot;
import Controller.ControlMonsterPlayer;
import Model.GameModel;
import Model.Hunter;
import Model.Monster;
import Utils.Coordinate;
import Utils.Maps;
import Utils.Observer;
import Utils.Subject;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

/**
 * La classe VueMonster est une classe qui gère l'interface utilisateur pour le
 * monstre dans le jeu. Elle affiche la grille de jeu, les informations
 * actuelles, et elle permet au joueur (ou à l'IA) de faire des mouvements.
 */
public class VueMonster extends AbstractView implements Observer {
    /** Le monstre associé à cette vue. */
    private Monster monster;

    /** Le contrôleur associé à cette vue. */
    private ControlMonster controlleur;

    /** Le panneau de grille de jeu. */
    private GridPane gridPane;

    /** Le stage (fenêtre) associé à cette vue. */
    private Stage stage;

    /** Le label affichant les informations actuelles. */
    private Label currentLabel;

    private int rangeOfFog;

    public int getRangeOfFog() {
        return rangeOfFog;
    }

    public void setRangeOfFog(int rangeOfFog) {
        this.rangeOfFog = rangeOfFog;
    }

    /**
     * Boolean du brouillard mis de base à false , cela veut dire que le brouillard
     * n'est pas activé
     */
    private boolean fogOfWar = false;

    public boolean isFogOfWar() {
        return fogOfWar;
    }

    public void setFogOfWar(boolean fogOfWar) {
        this.fogOfWar = fogOfWar;
    }

    /**
     * Setter pour le monstre associé à la vue.
     *
     * @param monster Le nouveau monstre associé à la vue.
     */
    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    /**
     * Getter pour le monstre associé à la vue.
     *
     * @return Le monstre associé à la vue.
     */
    public Monster getMonster() {
        return monster;
    }

    /**
     * Getter pour le panneau de grille de jeu.
     *
     * @return Le panneau de grille de jeu.
     */
    public GridPane getGridPane() {
        return gridPane;
    }

    public Scene getScene() {
        return stage.getScene();
    }

    /**
     * Constructeur de la classe VueMonster.
     *
     * @param monster Le monstre associé à la vue.
     * @param hunter  Le chasseur associé au jeu.
     * @param control Un booléen indiquant si le monstre est contrôlé par un joueur
     *                ou par une IA.
     */
    public VueMonster(Monster monster, Hunter hunter, boolean control) {
        this.monster = monster;
        if (!control) {
            this.controlleur = new ControlMonsterPlayer(this);
        } else {
            this.controlleur = new ControlMonsterBot(this);
        }
        this.stage = creerStage();
        monster.attach(this);
    }

    /**
     * Crée et configure le stage (fenêtre) associé à cette vue.
     *
     * @return Le stage créé.
     */
    public Stage creerStage() {
        Stage stage = new Stage();
        gridPane = new GridPane();
        chargePlateau();
        HBox hbox = new HBox(new Label("MONSTER"));
        hbox.setAlignment(Pos.CENTER);
        VBox vbox = new VBox();
        currentLabel = new Label("C'est au tour du Monstre");
        vbox.getChildren().add(currentLabel);
        vbox.setAlignment(Pos.CENTER);
        controlleur.mMouvement();
        VBox vboxall = new VBox(hbox, vbox, gridPane);
        styleGridPane(gridPane, 40);
        int rowCount = monster.getGameModel().getMap().getRow() + 2;
        int colCount = monster.getGameModel().getMap().getCol() + 2;
        Scene scene = new Scene(vboxall, 40 * rowCount, 40 * colCount);
        stage.setScene(scene);
        return stage;
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

    /**
     * Charge la grille de jeu dans le panneau de la grille.
     */
    public void chargePlateau() {
        gridPane.getChildren().clear();
        Maps map = GameModel.map;
        for (int i = 0; i < map.getRow(); i++) {
            for (int j = 0; j < map.getCol(); j++) {
                StackPane stackPane = createStackPaneWithBorder(map.getMaps()[i][j]);
                Coordinate coordMonster = monster.getCordMonster();

                // Brouillard de deux
                if (fogOfWar && monster.isWithinRange(coordMonster.getRow(),
                        coordMonster.getCol(), i, j, rangeOfFog)) {
                    applyFogOfWarOne(stackPane);
                }

                gridPane.add(stackPane, j, i);
            }
        }

    }

    public void applyFogOfWar(GridPane gridPane, boolean fogOfWar, int range, int x, int y) {
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                Integer colIndex = GridPane.getColumnIndex(node);
                Integer rowIndex = GridPane.getRowIndex(node);

                if (colIndex != null && rowIndex != null) {
                    // On remet la case à la valeur de base avant de la (re) modifier
                    resetStackPane(stackPane);
                    // Brouillard avec une portée spécifiée
                    if (fogOfWar && monster.isWithinRange(x, y, rowIndex,
                            colIndex, range)) {
                        applyFogOfWarOne(stackPane);
                    }
                }
            }
        }
    }

    private void resetStackPane(StackPane stackPane) {
        // Réinitialiser les propriétés du StackPane ici
        // Par exemple, vous pouvez remettre l'opacité à 1, le filtre, etc.
        stackPane.setStyle(""); // Remettre le style à sa valeur par défaut
        stackPane.setEffect(null); // Supprimer les effets
    }

    /**
     * Applique le brouillard sur la cellule si activé.
     *
     * @param stackPane Le StackPane de la cellule.
     */
    private void applyFogOfWarOne(StackPane stackPane) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.88); // Ajustez la luminosité en fonction de vos besoins

        stackPane.setEffect(colorAdjust);
        stackPane.setBlendMode(BlendMode.MULTIPLY);
    }

    /**
     * Active ou désactive le brouillard.
     *
     * @param fogEnabled true pour activer le brouillard, false pour le désactiver.
     */
    public void setFogEnabled(boolean fogEnabled, int rangeOfFog) {
        this.rangeOfFog = rangeOfFog;
        setFogOfWar(fogEnabled);
        chargePlateau(); // Mettre à jour la grille après avoir activé/désactivé le brouillard
    }

    /**
     * Met à jour la grille de jeu après un mouvement du monstre.
     *
     * @param clickedRow L'indice de la ligne cliquée.
     * @param clickedCol L'indice de la colonne cliquée.
     */
    public void updatePlateau(int clickedRow, int clickedCol) {
        // Récupération des coordonnées du monstre
        Coordinate monsterCoordinates = monster.getCordMonster();
        int row = monsterCoordinates.getRow();
        int col = monsterCoordinates.getCol();

        // Récupération des nœuds correspondants aux positions cliquée et du monstre
        // dans le GridPane
        Node clickedNode = getNodeByRowColumnIndex(clickedRow, clickedCol, gridPane);
        Node monsterNode = getNodeByRowColumnIndex(row, col, gridPane);

        if (clickedNode instanceof StackPane && monsterNode instanceof StackPane) {
            StackPane existingStackPane = (StackPane) clickedNode;
            StackPane monsterStackPane = (StackPane) monsterNode;

            // Mise à jour de l'image du monstre
            updateMonsterImage(monsterStackPane);

            // Mise à jour du label du monstre
            updateMonsterLabel(monsterStackPane);

            // Réarrangement des panneaux dans le GridPane
            rearrangePanes(existingStackPane, monsterStackPane);

            // Mise à jour de l'image cliquée
            updateClickedImage(existingStackPane);

        }
    }

    /**
     * Met à jour l'image du monstre à une image vide après un mouvement.
     *
     * @param monsterStackPane Le StackPane du monstre.
     */
    private void updateMonsterImage(StackPane monsterStackPane) {
        String imagePathMonster = determineImagePath(CellInfo.EMPTY);
        Image imageMonster = new Image(getClass().getResourceAsStream(imagePathMonster));
        ImageView monsterImageView = (ImageView) monsterStackPane.getChildren().get(0);
        monsterImageView.setImage(imageMonster);
    }

    /**
     * Met à jour le label du monstre avec le nombre de tours effectués.
     *
     * @param monsterStackPane Le StackPane du monstre.
     */
    private void updateMonsterLabel(StackPane monsterStackPane) {
        Label label;
        if (monsterStackPane.getChildren().size() > 1 && monsterStackPane.getChildren().get(1) instanceof Label) {
            // Si le label existe déjà, mettez à jour son texte
            label = (Label) monsterStackPane.getChildren().get(1);
            label.setText(String.valueOf(monster.getGameModel().getTurn()));
        } else {
            // Sinon, créez un nouveau label
            label = new Label(String.valueOf(monster.getGameModel().getTurn()));
            label.setTextFill(Color.BLACK);
            monsterStackPane.getChildren().add(label);
        }
    }

    /**
     * Réarrange les panneaux dans le GridPane pour mettre le monstre en
     * arrière-plan.
     *
     * @param existingStackPane Le StackPane de la cellule cliquée.
     * @param monsterStackPane  Le StackPane du monstre.
     */
    private void rearrangePanes(StackPane existingStackPane, StackPane monsterStackPane) {
        existingStackPane.toBack();
        monsterStackPane.toBack();
    }

    /**
     * Met à jour l'image de la cellule cliquée après un mouvement.
     *
     * @param existingStackPane Le StackPane de la cellule cliquée.
     */
    private void updateClickedImage(StackPane existingStackPane) {
        String imagePathClicked = determineImagePath(CellInfo.MONSTER);
        Image imageClicked = new Image(getClass().getResourceAsStream(imagePathClicked));
        ImageView existingImageView = (ImageView) existingStackPane.getChildren().get(0);
        existingImageView.setImage(imageClicked);
    }

    public void resetAllEffectImageView(GridPane gridPane) {
        ObservableList<Node> children = gridPane.getChildren();

        // Réinitialisez l'effet pour toutes les cases
        for (Node node : children) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                imageView.setEffect(null);
                stackPane.setEffect(null);

            }
        }
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
        if (data instanceof Coordinate) {
            Coordinate coordonnees = (Coordinate) data;
            int row = coordonnees.getRow();
            int col = coordonnees.getCol();
            updatePlateau(row, col);

        }
    }
}
