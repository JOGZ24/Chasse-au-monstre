package View;

import Model.Hunter;
import Model.Monster;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameView {

    private VueHunter vueHunter;
    private VueMonster vueMonster;
    private Stage stage;

    private Scene player1Scene;
    private Scene player2Scene;

    public GameView(Hunter hunter, Monster monster, int rangeOfFog) {
        this.vueHunter = new VueHunter(hunter, false);
        this.vueMonster = new VueMonster(monster, hunter, false);
        this.player1Scene = this.vueHunter.getScene();
        this.player2Scene = this.vueMonster.getScene();
        this.stage = vueMonster.getStage();
    }

    public void show() {
        refresh();
        stage.setScene(player2Scene);
        stage.show();
    }

    public void changeStage() {
        int playerTurn = vueHunter.getHunter().getGameModel().currentPlayer;
        if (playerTurn == 1) {
            this.stage.setScene(player2Scene);
        } else if (playerTurn == 2) {
            this.stage.setScene(player1Scene);
        } else {
            this.stage.setScene(player2Scene);
        }
    }

    public void refresh() {

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            changeStage();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void close() {
        stage.close();
    }

    public VueHunter getVueHunter() {
        return vueHunter;
    }

    public VueMonster getVueMonster() {
        return vueMonster;
    }

    public Stage getStage() {
        return stage;
    }

}