package Controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import Model.Monster;
import Utils.Coordinate;
import View.VueMonster;

/**
 * La classe ControlMonsterPlayer gère le mouvement du monstre dans le jeu en
 * réponse aux clics de souris sur la grille.
 * Le monstre peut se déplacer en cliquant sur une case de la grille. Cette
 * classe récupère les objets Monster et Hunter à partir
 * de la vue et actualise l'affichage du jeu en appelant la méthode refresh().
 * Elle configure ensuite un gestionnaire d'événements
 * pour les clics de souris sur la grille, permettant au monstre de se déplacer
 * vers la case cliquée.
 *
 * @implNote Le monstre ne peut se déplacer que si son état "canMove" le permet.
 * @implNote Lorsque le monstre se déplace, la case d'origine devient vide et la
 *           case de destination est occupée par le monstre.
 *           Le monstre met à jour sa position interne et le plateau de jeu est
 *           rechargé pour refléter les changements.
 * @implNote Le monstre change son état "canMove" pour indiquer qu'il a effectué
 *           un mouvement, et le chasseur fait de même.
 *           De plus, le monstre enregistre le chemin suivi dans le tableau
 *           "path". Chaque case visitée est enregistrée avec le numéro du tour.
 */
public class ControlMonsterPlayer implements ControlMonster {

    /**
     * Vue du monstre associée à cette instance de ControlMonsterPlayer.
     */
    private VueMonster view;

    /**
     * Coordonnée de la case cliquée par le monstre.
     */
    private Coordinate clickedCase;

    /**
     * Constructeur de la classe ControlMonsterPlayer.
     *
     * @param view La VueMonster associée à cette instance.
     */
    public ControlMonsterPlayer(VueMonster view) {
        this.view = view;
        this.clickedCase = new Coordinate(0, 0);
    }

    /**
     * Obtient la VueMonster associée à cette instance.
     *
     * @return La VueMonster associée à cette instance.
     */
    public VueMonster getView() {
        return view;
    }

    /**
     * Obtient la coordonnée de la case cliquée par le monstre.
     *
     * @return La coordonnée de la case cliquée par le monstre.
     */
    public Coordinate getClickedCase() {
        return clickedCase;
    }

    /**
     * Gère le mouvement du monstre en utilisant une interaction avec la grille.
     * Configure un gestionnaire d'événements pour les clics de souris sur la
     * grille.
     */
    public void mMouvement() {
        refresh();
        view.getGridPane().setOnMouseClicked(this::handleMouseClickMonster);
    }

    /**
     * Gère l'événement de clic de souris sur la grille par le monstre.
     * Identifie la case cliquée et effectue les actions appropriées.
     *
     * @param event L'événement de clic de souris.
     */
    private void handleMouseClickMonster(MouseEvent event) {
        Node source = event.getPickResult().getIntersectedNode();
        StackPane clickedStackPane = findClickedStackPane(source);

        if (clickedStackPane != null) {
            int clickedRow = GridPane.getRowIndex(clickedStackPane);
            int clickedCol = GridPane.getColumnIndex(clickedStackPane);

            this.clickedCase = new Coordinate(clickedRow, clickedCol);
            Monster monster = view.getMonster();

            if (monster.getGameModel().currentPlayer == 1) {
                handleMonsterPlayerTurn(monster, clickedRow, clickedCol);
            }
        }
    }

    /**
     * Gère le tour du monstre lorsqu'il est joué par le joueur.
     *
     * @param monster    Le monstre en jeu.
     * @param clickedRow L'indice de ligne de la case cliquée.
     * @param clickedCol L'indice de colonne de la case cliquée.
     */
    private void handleMonsterPlayerTurn(Monster monster, int clickedRow, int clickedCol) {
        Model.GameModel gameModel = monster.getGameModel();

        if (monster.victory(clickedRow, clickedCol) && monster.moveMonster(clickedRow, clickedCol)) {
            gameModel.currentPlayer = 3;
            view.showVictoryMessage(view.getGridPane(), "monster");
        } else {
            handleMonsterMove(monster, clickedRow, clickedCol);
        }
    }

    /**
     * Effectue le déplacement du monstre sur la grille.
     *
     * @param monster    Le monstre en jeu.
     * @param clickedRow L'indice de ligne de la case cliquée.
     * @param clickedCol L'indice de colonne de la case cliquée.
     */
    private void handleMonsterMove(Monster monster, int clickedRow, int clickedCol) {
        if (monster.moveMonster(clickedRow, clickedCol)) {
            if (view.isFogOfWar()) {
                view.applyFogOfWar(view.getGridPane(), true, view.getRangeOfFog(), clickedRow, clickedCol);
            }
            monster.addCurrentCordMonster(clickedRow, clickedCol);
            monster.getGameModel().changeCurrentPlayer();
        }
    }

    /**
     * Identifie la StackPane associée à un nœud cliqué.
     *
     * @param source Le nœud cliqué.
     * @return La StackPane associée au nœud cliqué, ou null si non trouvée.
     */
    private StackPane findClickedStackPane(Node source) {
        if (source instanceof ImageView) {
            return (StackPane) source.getParent();
        } else if (source instanceof StackPane) {
            return (StackPane) source;
        }
        return null;
    }

    /**
     * Rafraîchit l'affichage du label indiquant le tour du joueur et applique un
     * effet visuel à la position actuelle du monstre.
     */
    public void refresh() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (!view.isFogOfWar()) {
                view.resetAllEffectImageView(view.getGridPane());
            }
            Coordinate cord = view.getMonster().getHunted();
            StackPane stackPane = getStackPaneByRowColumnIndex(cord.getRow(), cord.getCol(), view.getGridPane());
            if (stackPane != null) {
                ImageView imageView = (ImageView) stackPane.getChildren().get(0);
                ColorAdjust color = new ColorAdjust();
                color.setHue(0.5);
                imageView.setEffect(color);
            }
            if (view.getMonster().getGameModel().currentPlayer == 1) {
                view.getCurrentLabel().setText("C'est au tour du Monstre");
            } else {
                view.getCurrentLabel().setText("C'est au tour du Chasseur");
            }

        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Obtient la StackPane associée à une position donnée dans une grille.
     *
     * @param row      L'indice de ligne.
     * @param column   L'indice de colonne.
     * @param gridPane La grille dans laquelle rechercher.
     * @return La StackPane associée à la position spécifiée, ou null si non
     *         trouvée.
     */
    public StackPane getStackPaneByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                if (node instanceof StackPane) {
                    return (StackPane) node;
                }
            }
        }
        return null;
    }

    public void applyFogOfWar(GridPane gridPane) {
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                Integer colIndex = GridPane.getColumnIndex(node);
                Integer rowIndex = GridPane.getRowIndex(node);

                if (colIndex != null && rowIndex != null) {
                    // On remet la case à la valeur de base avant de la (re) modifier
                    resetStackPane(stackPane);
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
}
