package Main;

import Menu.Menu;

import javafx.application.Application;

import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Menu app = new Menu();
        primaryStage = app.createStage();
        primaryStage.show();

    }
}
