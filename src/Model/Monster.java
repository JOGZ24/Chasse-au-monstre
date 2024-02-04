package Model;

import Utils.Coordinate;
import Utils.Maps;
import Utils.Subject;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

/**
 * La classe Monster représente le monstre dans le jeu "Monster Hunter". Le
 * monstre peut se déplacer sur la grille,
 * effectuer des actions en fonction de sa stratégie, et être observé par des
 * observateurs.
 */
public class Monster extends Subject {

    /** Pseudo du monstre. */
    public String nickname;

    /** Indique si c'est le tour du monstre. */
    public boolean tour;

    /** Indique si le monstre peut se déplacer. */
    public boolean canMoove;

    /** Coordonnées actuelles du monstre. */
    public Coordinate cordMonster;

    /** Modèle du jeu associé au monstre. */
    public GameModel gameModel;

    /** Tableau représentant le chemin suivi par le monstre. */
    public int[][] path;

    /** Stratégie du monstre. */
    private MonsterStrategy strategy;

    /**
     * Constructeur de la classe Monster.
     *
     * @param nickname  Pseudo du monstre.
     * @param gameModel Modèle du jeu associé au monstre.
     */
    public Monster(String nickname, GameModel gameModel) {
        this.tour = true;
        this.nickname = nickname;
        this.gameModel = gameModel;
        canMoove = true;
        cordMonster = gameModel.getMap().getCordUser(CellInfo.MONSTER);
        initPath();
        this.strategy = new MonsterStrategy();
    }

    /**
     * Obtient la stratégie du monstre.
     *
     * @return La stratégie du monstre.
     */
    public MonsterStrategy getStrategy() {
        return strategy;
    }

    /**
     * Définit la stratégie du monstre.
     *
     * @param strategy La nouvelle stratégie du monstre.
     */
    public void setStrategy(MonsterStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Obtient les coordonnées actuelles du monstre.
     *
     * @return Les coordonnées actuelles du monstre.
     */
    public Coordinate getCordMonster() {
        return this.cordMonster;
    }

    /**
     * Obtient le pseudo du monstre.
     *
     * @return Le pseudo du monstre.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Vérifie si c'est le tour du monstre.
     *
     * @return True si c'est le tour du monstre, sinon False.
     */
    public boolean isTour() {
        return tour;
    }

    /**
     * Vérifie si le monstre peut se déplacer.
     *
     * @return True si le monstre peut se déplacer, sinon False.
     */
    public boolean getCanMoove() {
        return canMoove;
    }

    /**
     * Vérifie si les coordonnées (newX, newY) sont adjacentes aux coordonnées
     * actuelles du monstre.
     *
     * @param currentX Coordonnée x actuelle du monstre.
     * @param currentY Coordonnée y actuelle du monstre.
     * @param newX     Coordonnée x nouvelle du monstre.
     * @param newY     Coordonnée y nouvelle du monstre.
     * @return True si les coordonnées sont adjacentes, sinon False.
     */
    public boolean isAdjacent(int currentX, int currentY, int newX, int newY) {
        int dx = Math.abs(newX - currentX);
        int dy = Math.abs(newY - currentY);
        // Vérifiez si les coordonnées (newX, newY) sont adjacentes horizontalement,
        // verticalement ou en diagonale
        return (dx >= 0 && dx <= 1 && dy >= 0 && dy <= 1);
    }

    public boolean isWithinRange(int currentX, int currentY, int newX, int newY, int range) {
        int dx = Math.abs(newX - currentX);
        int dy = Math.abs(newY - currentY);

        // Vérifiez si les coordonnées (newX, newY) sont adjacentes avec une portée de
        // range
        // cases
        return !(dx >= 0 && dx <= range && dy >= 0 && dy <= range);
    }

    /**
     * Déplace le monstre vers les nouvelles coordonnées (x, y) sur la grille.
     *
     * @param x Coordonnée x de la destination.
     * @param y Coordonnée y de la destination.
     * @return True si le monstre a réussi à se déplacer, sinon False.
     */
    public boolean moveMonster(int x, int y) {
        Coordinate cord = cordMonster;
        Maps map = gameModel.getMap();
        if (gameModel.isWall(new Coordinate(x, y)) || (x == cord.getRow() && y == cord.getCol()))
            return false;
        // Vérifiez si les nouvelles coordonnées sont adjacentes aux coordonnées
        // actuelles du monstre
        if (isAdjacent(cord.getRow(), cord.getCol(), x, y)) {
            map.getMaps()[cord.getRow()][cord.getCol()] = CellInfo.EMPTY;
            map.getMaps()[x][y] = CellInfo.MONSTER;
            getGameModel().Playround();
            gameModel.addPath(new Coordinate(x, y));
            performActionThatChangesState(x, y);
            return true;
        }
        return false;
    }

    /**
     * Met à jour les coordonnées actuelles du monstre après un déplacement.
     *
     * @param x Nouvelle coordonnée x du monstre.
     * @param y Nouvelle coordonnée y du monstre.
     */
    public void addCurrentCordMonster(int x, int y) {
        cordMonster.setRow(x);
        cordMonster.setCol(y);
    }

    /**
     * Vérifie si le monstre a remporté la partie en atteignant la case spécifiée.
     *
     * @param x Coordonnée x de la case.
     * @param y Coordonnée y de la case.
     * @return True si le monstre a remporté la partie, sinon False.
     */
    public boolean victory(int x, int y) {
        return gameModel.getMap().getMaps()[x][y] == CellInfo.EXIT;
    }

    /**
     * Inverse l'état de la possibilité de déplacement du monstre.
     */
    public void changeCanMoove() {
        canMoove = !canMoove;
    }

    /**
     * Obtient les coordonnées où le chasseur a repéré le monstre pour la dernière
     * fois.
     *
     * @return Les coordonnées repérées par le chasseur.
     */
    public Coordinate getHunted() {
        return this.gameModel.getHunted();
    }

    /**
     * Initialise le tableau représentant le chemin suivi par le monstre.
     */
    public void initPath() {
        this.path = new int[10][5];
        for (int i = 0; i < 10; i++) {
            for (int y = 0; y < 5; y++) {
                path[i][y] = -1;
            }
        }
    }

    /**
     * Effectue des opérations qui modifient l'état du monstre.
     *
     * @param x Coordonnée x de l'action effectuée.
     * @param y Coordonnée y de l'action effectuée.
     */
    public void performActionThatChangesState(int x, int y) {
        // Effectuez les opérations qui modifient l'état du monstre ici
        // Par exemple, enregistrez les coordonnées du clic ou de l'action

        Coordinate coordonnees = new Coordinate(x, y);

        // Après les modifications, appelez notifyObservers avec l'objet Coordonnees en
        // paramètre
        notifyObservers(coordonnees);
    }

    /**
     * Obtient le modèle du jeu associé au monstre.
     *
     * @return Le modèle du jeu associé au monstre.
     */
    public GameModel getGameModel() {
        return gameModel;
    }
}
