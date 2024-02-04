package test;

import Model.GameModel;
import Model.Hunter;
import Model.Monster;
import Utils.Coordinate;
import fr.univlille.iutinfo.cam.player.hunter.IHunterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestHunter {
    @Test
    public void testVictory() {
        // Taille du modèle de jeu pour l'initialisation
        int size = 10;

        // Créer un GameModel
        GameModel gameModel = new GameModel(null, null, size, 10, false);

        // Créer un Hunter pour le test
        Hunter hunter = new Hunter("TestHunter", gameModel, null);

        // Simuler la position du monstre
        Monster monster = new Monster("TestMonster", gameModel);
        monster.addCurrentCordMonster(0, 0);

        // Tester la victoire en tirant sur le monstre
        assertTrue(hunter.victory(0, 0));

        // Tester la non-victoire en tirant dans une autre position
        assertFalse(hunter.victory(1, 1));
    }

    @Test
    public void testChangeCanMove() {
        // Taille du modèle de jeu pour l'initialisation
        int size = 10;

        // Créer un GameModel
        GameModel gameModel = new GameModel(null, null, size, 10, false);

        // Créer un Hunter pour le test
        Hunter hunter = new Hunter("TestHunter", gameModel, null);

        // Vérifier l'état initial
        assertFalse(hunter.getCanMoove());

        // Changer l'état et vérifier
        hunter.changeCanMoove();
        assertTrue(hunter.getCanMoove());

        // Changer à nouveau et vérifier
        hunter.changeCanMoove();
        assertFalse(hunter.getCanMoove());
    }

    @Test
    public void testShoot() {
        // Taille du modèle de jeu pour l'initialisation
        int size = 10;

        // Créer un GameModel
        GameModel gameModel = new GameModel(null, null, size, 10, false);

        // Créer un Hunter pour le test
        Hunter hunter = new Hunter("TestHunter", gameModel, null);

        // Coordonnées de tir
        int x = 2;
        int y = 3;

        // Tirer et vérifier les coordonnées
        hunter.shoot(x, y);
        Coordinate hunted = hunter.getHunted();
        assertEquals(x, hunted.getRow());
        assertEquals(y, hunted.getCol());

        // Vérifier que la case correspondante dans la carte de tir est marquée
        assertTrue(gameModel.getMap().getMapShoot()[x][y]);
    }
    @Test
    public void testGetHunted() {
        // Créez une instance de la classe GameModel pour le chasseur
        GameModel gameModel = new GameModel(null, null, 10, 20, false);

        // Créez une instance de la classe Hunter
        Hunter hunter = new Hunter("TestHunter", gameModel, null);

        // Utilisez un objet Coordinate pour définir les coordonnées
        Coordinate coordinate = new Coordinate(3, 4);
        gameModel.setHunted(coordinate);

        // Appelez la méthode getHunted
        Coordinate result = hunter.getHunted();

        // Ajoutez une assertion pour vérifier que les coordonnées obtenues sont les mêmes que celles définies
        assertEquals(coordinate, result);
    }
        @Test
    public void testGetStrategy() {
        // Créez une instance de la classe Hunter
        Hunter hunter = new Hunter("TestHunter", null, null);

        // Appelez la méthode getStrategy
        IHunterStrategy result = hunter.getStrategy();

        // Ajoutez une assertion pour vérifier que la stratégie obtenue est null (non définie)
        assertNull(result);
    }
    @Test
    public void testSetStrategy() {
        // Créez une instance de la classe Hunter
        Hunter hunter = new Hunter("TestHunter", null, null);

        // Créez une instance de la classe IHunterStrategy pour définir la stratégie
        IHunterStrategy strategy = new IHunterStrategy() {
            @Override
            public ICoordinate play() {
                throw new UnsupportedOperationException("Unimplemented method 'play'");
            }
            @Override
            public void update(ICellEvent arg0) {
                throw new UnsupportedOperationException("Unimplemented method 'update'");
            }
            @Override
            public void initialize(int arg0, int arg1) {
                throw new UnsupportedOperationException("Unimplemented method 'initialize'");
            }
            // Implémentez les méthodes nécessaires
        };

        // Appelez la méthode setStrategy
        hunter.setStrategy(strategy);

        // Appelez la méthode getStrategy pour obtenir la stratégie définie
        IHunterStrategy result = hunter.getStrategy();

        // Ajoutez une assertion pour vérifier que la stratégie obtenue est la même que celle définie
        assertEquals(strategy, result);
    }
    @Test
    public void testGetCanMoove() {
        // Créez une instance de la classe Hunter
        Hunter hunter = new Hunter("TestHunter", null, null);

        // Appelez la méthode getCanMoove
        boolean result = hunter.getCanMoove();

        // Ajoutez une assertion pour vérifier que la valeur obtenue est celle par défaut (false)
        assertFalse(result);
    }

    @Test
    public void testSetCanMoove() {
        // Créez une instance de la classe Hunter
        Hunter hunter = new Hunter("TestHunter", null, null);

        // Appelez la méthode setCanMoove avec la valeur true
        hunter.setCanMoove(true);

        // Appelez la méthode getCanMoove pour obtenir la nouvelle valeur
        boolean result = hunter.getCanMoove();

        // Ajoutez une assertion pour vérifier que la valeur obtenue est celle définie (true)
        assertTrue(result);
    }

    @Test
    public void testGetNickname() {
        // Créez une instance de la classe Hunter avec un pseudo spécifié
        Hunter hunter = new Hunter("TestHunter", null, null);

        // Appelez la méthode getNickname
        String result = hunter.getNickname();

        // Ajoutez une assertion pour vérifier que le pseudo obtenu est celui défini
        assertEquals("TestHunter", result);
    }

}
