package Menu; // Remplacez par le vrai nom de votre package

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * La classe Menu est responsable de la création de la fenêtre du menu principal
 * du jeu "Monster Hunter".
 * Elle charge le fichier FXML associé et crée une nouvelle scène pour afficher
 * la fenêtre.
 */
public class Menu {

    private String css = this.getClass().getResource("/res/css/application.css").toExternalForm();

    /**
     * Crée et configure la scène du menu principal du jeu "Monster Hunter".
     *
     * @return La fenêtre du menu principal du jeu.
     */
    public Stage createStage() {
        try {
            Stage stage = new Stage();

            // Charger le fichier FXML associé au menu
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/fxml/Menu.fxml"));
            Parent root = loader.load();

            // Créer la scène
            Scene scene = new Scene(root);
            scene.getStylesheets().add(css);

            // Configurer la scène et afficher la fenêtre
            stage.setTitle("Monster Hunter");
            stage.setScene(scene);

            return stage;
        } catch (IOException e) {
            e.printStackTrace(); // Gérer l'exception d'entrée/sortie ici
            return null;
        }
    }
}
