package test;

import Model.GameModel;
import Utils.Coordinate;
import Utils.Maps;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.Map;

public class GameModelTest {
    GameModel gameModel = new GameModel(null, null, 10, 20, false);

    @Test
    public void testGetTurn() {
        // Appelez la méthode getTurn
        int result = gameModel.getTurn();

        // Ajoutez une assertion pour vérifier que le numéro du tour obtenu est égal à 0
        // (valeur initiale)
        assertEquals(0, result);
    }

    @Test
    public void testAddPath() {

        // Créez une instance de la classe Coordinate
        Coordinate coordinate = new Coordinate(2, 3);

        // Appelez la méthode addPath avec les coordonnées
        gameModel.addPath(coordinate);

        // Appelez la méthode getPath pour obtenir le numéro du tour pour les
        // coordonnées spécifiées
        int result = gameModel.getPath(coordinate);

        // Ajoutez une assertion pour vérifier que le numéro du tour obtenu est égal à 1
        // (tour suivant)
        assertEquals(1, result);
    }

    @Test
    public void testIsMonster() {
        // Créez une instance de la classe Coordinate
        Coordinate coordinate = new Coordinate(2, 3);

        // Appelez la méthode isMonster avec les coordonnées
        boolean result = gameModel.isMonster(coordinate);

        // Ajoutez une assertion pour vérifier que le résultat obtenu est faux (la case
        // ne contient pas le monstre)
        assertFalse(result);
    }

    @Test
    public void testChangeCurrentPlayer() {
        // Obtenez le joueur actuel avant le changement
        int currentPlayerBefore = gameModel.currentPlayer;

        // Appelez la méthode changeCurrentPlayer
        gameModel.changeCurrentPlayer();

        // Obtenez le joueur actuel après le changement
        int currentPlayerAfter = gameModel.currentPlayer;

        // Ajoutez une assertion pour vérifier que le joueur actuel après le changement
        // est différent de celui avant
        assertNotEquals(currentPlayerBefore, currentPlayerAfter);
    }

    @Test
    public void testPlayround() {
        // Obtenez le numéro du tour avant l'exécution de Playround
        int turnBefore = gameModel.getTurn();

        // Appelez la méthode Playround
        gameModel.Playround();

        // Obtenez le numéro du tour après l'exécution de Playround
        int turnAfter = gameModel.getTurn();

        // Ajoutez une assertion pour vérifier que le numéro du tour après l'exécution
        // de Playround est égal à celui avant plus 1
        assertEquals(turnBefore + 1, turnAfter);
    }

    @Test
    public void testGetMap() {
        // Appelez la méthode getMap
        Maps result = gameModel.getMap();

        // Ajoutez une assertion pour vérifier que la carte obtenue est la même que
        // celle définie initialement
        assertEquals(Maps.class, result.getClass());
    }

    @Test
    public void testGetPath() {
        // Créez une instance de la classe Coordinate
        Coordinate coordinate = new Coordinate(2, 3);

        // Appelez la méthode addPath avec les coordonnées
        gameModel.addPath(coordinate);

        // Appelez la méthode getPath pour obtenir le chemin parcouru
        Map<Coordinate, Integer> result = gameModel.getPath();

        // Ajoutez une assertion pour vérifier que le chemin parcouru obtenu contient
        // les coordonnées spécifiées
        assertTrue(result.containsKey(coordinate));
    }
}