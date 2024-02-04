package Controller;

import Utils.Coordinate;
import View.VueHunter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * La classe ControlHunterPlayer gère le mouvement du chasseur dans le jeu en
 * réponse aux clics de souris sur la grille.
 * Lorsque le joueur clique sur le Label de la grille, cette classe est
 * responsable de gérer le déplacement du chasseur.
 *
 * @implNote Cette classe récupère la grille à partir de la vue et configure un
 *           gestionnaire d'événements pour les clics de souris sur la grille.
 *           Si le joueur clique sur une étiquette, le chasseur peut se déplacer
 *           en fonction de certaines conditions (notamment, l'état "canMove").
 *           Si le chasseur réussit à se déplacer vers une nouvelle position, la
 *           carte du chasseur est mise à jour pour refléter le tir et la
 *           victoire.
 *           De plus, les états "canMove" du chasseur et du monstre sont
 *           modifiés en conséquence.
 *
 * @implNote Cette classe est liée à un jeu ou à une simulation où le chasseur
 *           peut tirer sur la grille pour éliminer le monstre.
 *           La victoire est déterminée en fonction de la position du chasseur.
 */
public class ControlHunterPlayer implements ControlHunter {

    /**
     * Vue du chasseur associée à cette instance de ControlHunterPlayer.
     */
    public VueHunter view;

    /**
     * Coordonnée de la case cliquée par le joueur.
     */
    public Coordinate clickedCase;

    /**
     * Constructeur de la classe ControlHunterPlayer.
     *
     * @param view La VueHunter associée à cette instance.
     */
    public ControlHunterPlayer(VueHunter view) {
        this.view = view;
        clickedCase = new Coordinate(0, 0);
    }

    /**
     * Gère le mouvement du chasseur en utilisant une interaction avec la grille.
     * Configure un gestionnaire d'événements pour les clics de souris sur la
     * grille.
     */
    public void hMouvement() {
        refresh();
        view.getGridPane().setOnMouseClicked(this::handleMouseClick);
    }

    /**
     * Gère l'événement de clic de souris sur la grille.
     * Identifie la case cliquée et effectue les actions appropriées.
     *
     * @param event L'événement de clic de souris.
     */
    private void handleMouseClick(MouseEvent event) {
        Node source = event.getPickResult().getIntersectedNode();
        StackPane clickedStackPane = findClickedStackPane(source);

        if (clickedStackPane != null) {
            int clickedRow = GridPane.getRowIndex(clickedStackPane);
            int clickedCol = GridPane.getColumnIndex(clickedStackPane);

            this.clickedCase = new Coordinate(clickedRow, clickedCol);

            if (view.getHunter().getGameModel().currentPlayer == 2) {
                handleHunterTurn(clickedRow, clickedCol);
            }
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
     * Gère le tour du chasseur lorsqu'il tire sur la grille.
     *
     * @param clickedRow L'indice de ligne de la case cliquée.
     * @param clickedCol L'indice de colonne de la case cliquée.
     */
    private void handleHunterTurn(int clickedRow, int clickedCol) {
        Model.Hunter hunter = view.getHunter();
        Model.GameModel gameModel = hunter.getGameModel();

        hunter.shoot(clickedRow, clickedCol);
        view.resetAllEffect(view.getGridPane());

        if (hunter.victory(clickedRow, clickedCol)) {
            gameModel.currentPlayer = 3;
            view.showVictoryMessage(view.getGridPane(), "hunter");
        } else {
            gameModel.changeCurrentPlayer();
        }

        updateHunterPosition(clickedRow, clickedCol);
    }

    /**
     * Met à jour la position du chasseur.
     *
     * @param row L'indice de ligne de la nouvelle position.
     * @param col L'indice de colonne de la nouvelle position.
     */
    private void updateHunterPosition(int row, int col) {
        view.getHunter().setHunted(new Coordinate(row, col));
    }

    /**
     * Obtient la coordonnée de la case cliquée par le joueur.
     *
     * @return La coordonnée de la case cliquée.
     */
    public Coordinate getClickedCase() {
        return clickedCase;
    }

    /**
     * Rafraîchit l'affichage du label indiquant le tour du joueur.
     */
    public void refresh() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (view.getHunter().getGameModel().currentPlayer == 1) {
                view.getCurrentLabel().setText("C'est au tour du Monstre");
            } else {
                view.getCurrentLabel().setText("C'est au tour du Chasseur");
            }
        }));
        // Configure la répétition indéfinie de la timeline, ce qui signifie que le
        // rafraîchissement continuera indéfiniment.
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
