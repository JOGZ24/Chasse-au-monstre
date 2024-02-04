package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;
import Model.GameModel;
import Model.Monster;
import Utils.Coordinate;

public class TestMonster {
    GameModel gameModel = new GameModel(null, null, 10, 20, false);
    @Test
    void testMoveMonster() {
        // Taille du modèle de jeu pour l'initialisation
        int size = 10;

        // Créer un GameModel
        GameModel gameModel = new GameModel(null, null, size, 10, false);

        // Créer un Monster pour le test
        Monster monster = new Monster("TestMonster", gameModel);

        // Coordonnées actuelles du monstre
        Coordinate currentCord = monster.getCordMonster();

        // Coordonnées de la nouvelle position (vers la droite)
        int newX = currentCord.getRow();
        int newY = currentCord.getCol() + 1;

        // Effectuer le mouvement et vérifier
        assertTrue(monster.moveMonster(newX, newY));
        assertEquals(CellInfo.EMPTY, gameModel.getMap().getMaps()[currentCord.getRow()][currentCord.getCol()]);
        assertEquals(CellInfo.MONSTER, gameModel.getMap().getMaps()[newX][newY]);

        // Coordonnées actuelles du monstre après le mouvement
        Coordinate newCord = monster.getCordMonster();
        assertEquals(newX, newCord.getRow());
        // assertEquals(newY, newCord.getCol());

        // Test de mouvement vers une position non adjacente
        int invalidX = currentCord.getRow() + 2;
        int invalidY = currentCord.getCol() + 2;
        assertFalse(monster.moveMonster(invalidX, invalidY));
        assertEquals(CellInfo.EMPTY, gameModel.getMap().getMaps()[invalidX][invalidY]);

        // Test de mouvement vers un mur
        int wallX = currentCord.getRow();
        int wallY = currentCord.getCol() - 1;
        gameModel.getMap().setCellInfo(wallX, wallY, CellInfo.WALL);
        assertFalse(monster.moveMonster(wallX, wallY));
        assertEquals(CellInfo.WALL, gameModel.getMap().getMaps()[wallX][wallY]);
    }

    @Test
    void testVictory() {
        // Taille du modèle de jeu pour l'initialisation
        int size = 10;

        // Créer un GameModel
        GameModel gameModel = new GameModel(null, null, size, 10, false);

        // Créer un Monster pour le test
        Monster monster = new Monster("TestMonster", gameModel);

        // Coordonnées de la sortie
        Coordinate exitCord = gameModel.getMap().getCordUser(CellInfo.EXIT);

        // Tester la victoire en atteignant la sortie
        assertTrue(monster.victory(exitCord.getRow(), exitCord.getCol()));

        // Tester la non-victoire en restant à la position initiale du monstre
        assertFalse(monster.victory(0, 0));
    }

    @Test
    void testChangeCanMove() {
        // Taille du modèle de jeu pour l'initialisation
        int size = 10;

        // Créer un GameModel
        GameModel gameModel = new GameModel(null, null, size, 10, false);

        // Créer un Monster pour le test
        Monster monster = new Monster("TestMonster", gameModel);

        // Vérifier l'état initial
        assertTrue(monster.getCanMoove());

        // Changer l'état et vérifier
        monster.changeCanMoove();
        assertFalse(monster.getCanMoove());

        // Changer à nouveau et vérifier
        monster.changeCanMoove();
        assertTrue(monster.getCanMoove());
    }

    @Test
    public void testGetCordMonster() {
        // Créez une instance de la classe GameModel pour le monstre
        GameModel gameModel = new GameModel(null, null, 10, 20, false);

        // Créez une instance de la classe Monster
        Monster monster = new Monster("TestMonster", gameModel);

        // Appelez la méthode getCordMonster
        Coordinate result = monster.getCordMonster();

        // Ajoutez une assertion pour vérifier que les coordonnées obtenues sont non nulles
        assertNotNull(result);
    }
    @Test
    public void testGetStrategy() {
        // Créez une instance de la classe Monster
        Monster monster = new Monster("TestMonster", gameModel);

        // Appelez la méthode getStrategy
        Model.MonsterStrategy result = monster.getStrategy();

        // Ajoutez une assertion pour vérifier que la stratégie obtenue est la même que celle définie
        assertNotNull(result);
    }

    @Test
    public void testSetStrategy() {
        // Créez une instance de la classe Monster
        Monster monster = new Monster("TestMonster",gameModel);

        // Créez une instance de la classe MonsterStrategy pour définir la stratégie
        Model.MonsterStrategy strategy = new Model.MonsterStrategy();

        // Appelez la méthode setStrategy
        monster.setStrategy(strategy);

        // Appelez la méthode getStrategy pour obtenir la stratégie définie
        Model.MonsterStrategy result = monster.getStrategy();

        // Ajoutez une assertion pour vérifier que la stratégie obtenue est la même que celle définie
        assertEquals(strategy, result);
    }

    @Test
    public void testGetNickname() {
        // Créez une instance de la classe Monster avec un pseudo spécifié
        Monster monster = new Monster("TestMonster", gameModel);

        // Appelez la méthode getNickname
        String result = monster.getNickname();

        // Ajoutez une assertion pour vérifier que le pseudo obtenu est celui défini
        assertEquals("TestMonster", result);
    }

    @Test
    public void testIsTour() {
        // Créez une instance de la classe Monster
        Monster monster = new Monster("TestMonster", gameModel);

        // Appelez la méthode isTour
        boolean result = monster.isTour();

        // Ajoutez une assertion pour vérifier que la valeur obtenue est celle par défaut (true)
        assertTrue(result);
    }

    @Test
    public void testGetCanMoove() {
        // Créez une instance de la classe Monster
        Monster monster = new Monster("TestMonster", gameModel);

        // Appelez la méthode getCanMoove
        boolean result = monster.getCanMoove();

        // Ajoutez une assertion pour vérifier que la valeur obtenue est celle par défaut (true)
        assertTrue(result);
    }

    @Test
    public void testIsAdjacent() {
        // Créez une instance de la classe Monster
        Monster monster = new Monster("TestMonster", gameModel);

        // Appelez la méthode isAdjacent avec des coordonnées adjacentes
        boolean result = monster.isAdjacent(1, 1, 1, 2);

        // Ajoutez une assertion pour vérifier que la valeur obtenue est true
        assertTrue(result);
    }

    @Test
    public void testAddCurrentCordMonster() {
        // Créez une instance de la classe Monster
        Monster monster = new Monster("TestMonster",gameModel);

        // Appelez la méthode addCurrentCordMonster avec des coordonnées
        monster.addCurrentCordMonster(3, 4);

        // Appelez la méthode getCordMonster pour obtenir les nouvelles coordonnées
        Coordinate result = monster.getCordMonster();

        // Ajoutez une assertion pour vérifier que les coordonnées obtenues sont les mêmes que celles définies
        assertEquals(new Coordinate(3, 4), result);
    }


    @Test
    public void testChangeCanMoove() {
        // Créez une instance de la classe Monster
        Monster monster = new Monster("TestMonster", gameModel);

        // Appelez la méthode changeCanMoove
        monster.changeCanMoove();

        // Appelez la méthode getCanMoove pour obtenir la nouvelle valeur
        boolean result = monster.getCanMoove();

        // Ajoutez une assertion pour vérifier que la valeur obtenue est inversée
        assertFalse(result);
    }

    @Test
    public void testGetHunted() {
        // Créez une instance de la classe GameModel pour le monstre
        GameModel gameModel = new GameModel(null, null, 10, 20, false);

        // Créez une instance de la classe Monster
        Monster monster = new Monster("TestMonster", gameModel);

        // Appelez la méthode getHunted
        Coordinate result = monster.getHunted();

        // Ajoutez une assertion pour vérifier que les coordonnées obtenues sont non nulles
        assertNotNull(result);
    }
}
