package Controller;

import Model.HunterStrategy;
import Utils.Coordinate;
import View.VueHunter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * La classe ControlHunterBot gère le mouvement du chasseur dans le jeu en
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
public class ControlHunterBot implements ControlHunter {

    public VueHunter view;
    private Coordinate clickedCase;

    /**
     * Constructeur de la classe ControlHunterBot.
     *
     * @param view La VueHunter associée à cette instance.
     */
    public ControlHunterBot(VueHunter view) {
        this.view = view;
        this.clickedCase = new Coordinate(-1, -1);
    }

    /**
     * Gère le mouvement du chasseur en utilisant une stratégie définie.
     * Cette méthode est appelée en réponse à des événements de clics de souris sur
     * la grille.
     * Elle utilise une timeline pour effectuer le mouvement du chasseur à
     * intervalles réguliers.
     */
    public void hMouvement() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> handleHunterMovement()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void handleHunterMovement() {
        HunterStrategy strategy = (HunterStrategy) view.getHunter().getStrategy();
        if (view.getHunter().getGameModel().currentPlayer == 2) {
            Coordinate coordinate = strategy.smartPlay(clickedCase, view);
            int clickedRow = coordinate.getRow();
            int clickedCol = coordinate.getCol();

            this.clickedCase = new Coordinate(clickedRow, clickedCol);

            view.getHunter().shoot(clickedRow, clickedCol);
            if (view.getHunter().victory(clickedRow, clickedCol)) {
                view.getHunter().getGameModel().currentPlayer = 3;
                view.showVictoryMessage(view.getGridPane(), "hunter");
            } else {
                view.getHunter().getGameModel().changeCurrentPlayer();
            }
            updateHunterPosition(clickedRow, clickedCol);
        }
        updateCurrentPlayerLabel();
    }

    private void updateHunterPosition(int row, int col) {
        view.getHunter().setHunted(new Coordinate(row, col));
    }

    private void updateCurrentPlayerLabel() {
        if (view.getHunter().getGameModel().currentPlayer == 1) {
            view.getCurrentLabel().setText("C'est au tour du Monstre");
        } else {
            view.getCurrentLabel().setText("C'est au tour du Chasseur");
        }
    }

    /**
     * Obtient la coordonnée de la case cliquée par le joueur.
     *
     * @return La coordonnée de la case cliquée.
     */
    public Coordinate getClickedCase() {
        return clickedCase;
    }
}
