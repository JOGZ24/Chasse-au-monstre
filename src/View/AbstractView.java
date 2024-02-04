/**
 * La classe AbstractView est une classe abstraite qui fournit des méthodes et des fonctionnalités communes
 * aux vues spécifiques du jeu. Elle contient des méthodes pour la gestion de l'interface utilisateur,
 * l'affichage des messages de victoire, la réinitialisation des effets, etc.
 *
 * Les classes de vue spécifiques du jeu doivent étendre cette classe pour bénéficier de ses fonctionnalités.
 */
package View;

import java.util.ArrayList;
import java.util.List;

import Menu.Menu;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

public abstract class AbstractView {

    /**
     * Détermine le chemin de l'image associée à un type de cellule spécifié.
     *
     * @param cellInfo Type de cellule (CellInfo) pour lequel trouver le chemin de
     *                 l'image.
     * @return Chemin de l'image associée au type de cellule spécifié.
     */
    protected String determineImagePath(CellInfo cellInfo) {
        switch (cellInfo) {
            case WALL:
                return "trou.jpg";
            case MONSTER:
                return "loup.jpg";
            case EXIT:
                return "sortie.png";
            default:
                return "green.jpg";
        }
    }

    /**
     * Ferme tous les stages (fenêtres) ouverts.
     */
    protected void fermerTousLesStages() {
        List<Stage> stagesToClose = new ArrayList<>();

        // Collecte les stages ouverts
        for (Window window : Window.getWindows()) {
            if (window instanceof Stage && !((Stage) window).isIconified()) {
                stagesToClose.add((Stage) window);
            }
        }

        // Ferme les stages collectés
        for (Stage stage : stagesToClose) {
            stage.close();
        }
    }

    /**
     * Crée et retourne un StackPane avec une bordure, contenant une image associée
     * à un type de cellule spécifié.
     *
     * @param cellInfo Type de cellule (CellInfo) pour lequel créer le StackPane
     *                 avec bordure.
     * @return StackPane avec une bordure contenant l'image associée au type de
     *         cellule spécifié.
     */
    protected StackPane createStackPaneWithBorder(CellInfo cellInfo) {
        StackPane stackPane = new StackPane();
        String imagePath = determineImagePath(cellInfo);
        Image image = null;

        try {
            image = new Image(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);

        stackPane.getChildren().add(imageView);

        return stackPane;
    }

    /**
     * Récupère le nœud (Node) situé à la position spécifiée dans une grille
     * (GridPane) en fonction de ses indices de ligne et de colonne.
     *
     * @param row      Indice de la ligne du nœud recherché.
     * @param column   Indice de la colonne du nœud recherché.
     * @param gridPane Grille (GridPane) dans laquelle rechercher le nœud.
     * @return Nœud situé à la position spécifiée, ou null si aucun nœud n'est
     *         trouvé.
     */
    public Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }

    /**
     * Applique un style prédéfini à une grille (GridPane) en spécifiant la couleur
     * de fond, le padding, l'espacement horizontal et vertical,
     * et ajuste la taille des images (ImageView) dans la grille.
     *
     * @param pane      La grille (GridPane) à styliser.
     * @param imageSize La taille souhaitée des images dans la grille.
     */
    public void styleGridPane(GridPane pane, double imageSize) {
        pane.setStyle("-fx-background-color: #ececec;");
        pane.setPadding(new Insets(1));
        pane.setHgap(1);
        pane.setVgap(1);

        for (Node node : pane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView imageView = (ImageView) node;
                imageView.setFitWidth(imageSize);
                imageView.setFitHeight(imageSize);
            }
        }
    }

    /**
     * Affiche un message de victoire lorsque le monstre gagne.
     */
    public void showVictoryMessage(GridPane gridPane, String whoWon) {
        Stage victoryStage = new Stage();
        victoryStage.setTitle("Victory!");
        Label victoryLabel = new Label("Congratulations! " + whoWon + " won!");
        Button replay = new Button("Retour au menu");
        replay.setOnAction(e -> {
            Menu menu = new Menu();
            victoryStage.close();
            fermerTousLesStages();
            fermerStage(gridPane);
            menu.createStage().show();
        });

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> {
            // Ferme toutes les fenêtres ouvertes
            victoryStage.close();
            fermerTousLesStages();
            fermerStage(gridPane);
        });
        VBox vbox = new VBox(victoryLabel, replay, closeButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        Scene victoryScene = new Scene(vbox, 300, 200);
        victoryStage.setScene(victoryScene);
        victoryStage.show();
    }

    /**
     * Ferme le stage associé à la grille de jeu.
     *
     * @param gridPane La grille de jeu dont le stage doit être fermé.
     */
    public void fermerStage(GridPane gridPane) {
        Stage stage = (Stage) gridPane.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    /**
     * Réinitialise tous les effets appliqués aux cases de la grille de jeu.
     *
     * @param gridPane La grille de jeu dont les effets doivent être réinitialisés.
     */
    public void resetAllEffect(GridPane gridPane) {
        ObservableList<Node> children = gridPane.getChildren();

        // Réinitialise l'effet pour toutes les cases
        for (Node node : children) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                stackPane.setEffect(null);
            }
        }
    }

}
