package Model;

import java.util.HashMap;
import java.util.Map;

import Utils.Coordinate;
import Utils.Maps;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

/**
 * La classe GameModel représente le modèle du jeu "Monster Hunter". Elle gère
 * les éléments généraux du jeu tels que le monstre,
 * le chasseur, le tour actuel, les joueurs en cours, le chemin parcouru, la
 * carte du jeu, et d'autres fonctionnalités liées au
 * déroulement du jeu.
 */
public class GameModel {

    /** Instance du monstre dans le jeu. */
    private Monster monster;

    /** Instance du chasseur dans le jeu. */
    private Hunter hunter;

    /** Numéro du tour actuel. */
    private int turn;

    /** Joueur actuel (1 pour le monstre, 2 pour le chasseur). */
    public int currentPlayer;

    /**
     * Chemin parcouru par les joueurs avec les coordonnées et les numéros de tour
     * correspondants.
     */
    private Map<Coordinate, Integer> path;

    /** Carte du jeu représentant les différents éléments dans chaque cellule. */
    public static Maps map;

    /**
     * Coordonnée du point où le chasseur a repéré le monstre pour la dernière fois.
     */
    private Coordinate hunted;

    /**
     * Constructeur de la classe GameModel.
     *
     * @param monster   Instance du monstre.
     * @param hunter    Instance du chasseur.
     * @param size      Taille de la carte (nombre de lignes/colonnes).
     * @param probaWall Probabilité de la présence d'un mur dans une cellule de la
     *                  carte.
     */
    public GameModel(Monster monster, Hunter hunter, int size, int probaWall, boolean predefinedLaby) {
        this.monster = monster;
        this.hunter = hunter;
        this.turn = 0;
        map = new Maps(size, size, probaWall, predefinedLaby);
        this.path = new HashMap<>();
        currentPlayer = 1;
        this.addPath(map.getCordUser(CellInfo.MONSTER));
        this.hunted = new Coordinate(1000, 1000);
    }

    /**
     * Obtient la coordonnée du point où le chasseur a repéré le monstre pour la
     * dernière fois.
     *
     * @return La coordonnée du point repéré par le chasseur.
     */
    public Coordinate getHunted() {
        return hunted;
    }

    /**
     * Définit la coordonnée du point où le chasseur a repéré le monstre pour la
     * dernière fois.
     *
     * @param hunted La coordonnée du point repéré par le chasseur.
     */
    public void setHunted(Coordinate hunted) {
        this.hunted = hunted;
    }

    /**
     * Retourne le numéro du tour actuel.
     *
     * @return Le numéro du tour actuel.
     */
    public int getTurn() {
        return this.turn;
    }

    /**
     * Retourne le numéro du tour en case "co". Renvoie null si la case n'existe pas
     * dans le chemin parcouru.
     *
     * @param co La coordonnée de la case.
     * @return Le numéro du tour pour la case spécifiée, ou null si la case n'existe
     *         pas dans le chemin parcouru.
     */
    public int getPath(Coordinate co) {
        return path.get(co);
    }

    /**
     * Ajoute les coordonnées à la liste du chemin parcouru.
     *
     * @param co Les coordonnées à ajouter au chemin.
     */
    public void addPath(Coordinate co) {
        path.put(co, turn + 1);
    }

    /**
     * Vérifie si l'un des deux joueurs a gagné en se trouvant sur la case "co".
     *
     * @param co Les coordonnées de la case.
     * @return True si l'un des joueurs a gagné, sinon False.
     */
    public boolean anyoneWin(Coordinate co) {
        return (monster.victory(co.getRow(), co.getCol()) || hunter.victory(co.getRow(), co.getCol()));
    }

    /**
     * Vérifie si la case "co" est un mur.
     *
     * @param co Les coordonnées de la case.
     * @return True si la case est un mur, sinon False.
     */
    public boolean isWall(Coordinate co) {
        return map.getMaps()[co.getRow()][co.getCol()] == CellInfo.WALL;
    }

    /**
     * Vérifie si la case "co" contient le monstre.
     *
     * @param co Les coordonnées de la case.
     * @return True si la case contient le monstre, sinon False.
     */
    public boolean isMonster(Coordinate co) {
        return map.getMaps()[co.getRow()][co.getCol()] == CellInfo.MONSTER;
    }

    /**
     * Change le joueur actuel.
     */
    public void changeCurrentPlayer() {
        if (currentPlayer == 1) {
            currentPlayer = 2;
        } else {
            currentPlayer = 1;
        }
    }

    public int getSizeOfMap() {
        return map.getCol();
    }

    /**
     * Exécute un tour de jeu.
     */
    public void Playround() {
        turn++;
    }

    /**
     * Obtient la carte du jeu.
     *
     * @return La carte du jeu.
     */
    public Maps getMap() {
        return map;
    }

    /**
     * Obtient l'instance du monstre dans le jeu.
     *
     * @return L'instance du monstre dans le jeu.
     */
    public Monster getMonster() {
        return monster;
    }

    /**
     * Obtient l'instance du chasseur dans le jeu.
     *
     * @return L'instance du chasseur dans le jeu.
     */
    public Hunter getHunter() {
        return hunter;
    }

    /**
     * Obtient le chemin parcouru.
     *
     * @return Le chemin parcouru avec les coordonnées et les numéros de tour
     *         correspondants.
     */
    public Map<Coordinate, Integer> getPath() {
        return path;
    }

    /**
     * Définit l'instance du monstre dans le jeu.
     *
     * @param monster L'instance du monstre dans le jeu.
     */
    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    /**
     * Définit l'instance du chasseur dans le jeu.
     *
     * @param hunter L'instance du chasseur dans le jeu.
     */
    public void setHunter(Hunter hunter) {
        this.hunter = hunter;
    }
}
