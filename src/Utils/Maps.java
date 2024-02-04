package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class Maps {
    private int row, col;

    public CellInfo[][] getMaps() {
        return map;
    }

    public boolean[][] getMapShoot() {
        return mapShoot;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    private CellInfo[][] map;
    private boolean[][] mapShoot;

    public Maps(int row, int col, int probaWall, boolean predefinedLaby) {
        if (predefinedLaby) {
            readMapFromCSV(10, 10);
        } else {
            this.map = randomInitMap(row, col, probaWall);
        }
        this.row = row;
        this.col = col;
        initShoot(row);
    }

    /**
     * Initialise la carte de suivi des tirs du chasseur, créant une matrice 2D de
     * booléens de taille 10x5 et initialisant toutes les valeurs à false.
     */
    public void initShoot(int size) {
        this.mapShoot = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int y = 0; y < size; y++) {
                this.mapShoot[i][y] = false;
            }
        }
    }

    /**
     * Affiche la carte actuelle du jeu en parcourant la matrice map et en imprimant
     * la première lettre de chaque élément CellInfo sur la console.
     */

    public void displayMap() {
        for (CellInfo[] cellInfos : map) {
            for (CellInfo cellInfo : cellInfos) {
                String temp = cellInfo.toString().substring(0, 1);
                System.out.print(temp);
            }
            System.out.println();
        }
    }

    /**
     * Recherche les coordonnées (lignes et colonnes) de la première occurrence de
     * la cellule donnée dans la matrice map et renvoie un objet Coordinate
     * représentant ces coordonnées. Si la cellule n'est pas trouvée, renvoie null.
     *
     * @param cell La cellule à rechercher dans la matrice map.
     * @return Un objet Coordinate représentant les coordonnées de la cellule, ou
     *         null si
     *         la cellule n'est pas trouvée.
     */

    public Coordinate getCordUser(CellInfo cell) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == cell) {
                    return new Coordinate(i, j);
                }
            }
        }
        return null;
    }

    /**
     * Lit une carte à partir d'un fichier CSV spécifié. Crée une nouvelle matrice
     * map
     * de la taille spécifiée (ligne x colonne) et lit les données à partir du
     * fichier
     * CSV pour remplir la carte.
     *
     * @param ligne   Le nombre de lignes de la carte.
     * @param colonne Le nombre de colonnes de la carte.
     */

    public void readMapFromCSV(int ligne, int colonne) {
        File csv = new File("res/Maps.csv");
        this.map = new CellInfo[ligne][colonne];
        try (BufferedReader reader = new BufferedReader(new FileReader(csv))) {
            String str;
            int cpt = 0;
            while ((str = reader.readLine()) != null) {
                String[] lst = str.split(",");
                for (int i = 0; i < colonne; i++) {
                    map[cpt][i] = CellInfo.valueOf(lst[i]);
                }
                cpt++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet de placer une instance de type CellInfo à l'endroit [x][y] sur la map.
     * 
     * @param x
     * @param y
     * @param cell
     */
    public void setCellInfo(int x, int y, CellInfo cell) {
        map[x][y] = cell;
    }

    // Check si la map est valide, notamment que le chemin le plus court fais 5
    // minimum
    public ArrayList<Coordinate> initCheckedMap(int rowsLength, int colsLength, int probaWall) {
        Solveur solv = new Solveur(randomInitMap(rowsLength, colsLength, probaWall));
        ArrayList<Coordinate> coords = solv.estFaisable(false);
        while ((coords = solv.estFaisable(false)) == null || coords.size() <= 5) {
            solv.setMap(randomInitMap(rowsLength, colsLength, probaWall));
        }
        System.out.println(coords);
        map = solv.getMap();
        return coords;
    }

    public CellInfo[][] randomInitMap(int rowsLength, int colsLength, int probaWall) {
        CellInfo[][] resMap = new CellInfo[rowsLength][colsLength];
        Coordinate tmpMonster = getRandomCellForMonsterOrExit(rowsLength, colsLength);
        resMap[tmpMonster.getRow()][tmpMonster.getCol()] = CellInfo.MONSTER;
        Coordinate tmpExit = getRandomCellForMonsterOrExit(rowsLength, colsLength);
        while (tmpMonster.equals(tmpExit)) {
            tmpExit = getRandomCellForMonsterOrExit(rowsLength, colsLength);
        }
        resMap[tmpExit.getRow()][tmpExit.getCol()] = CellInfo.EXIT;
        for (int i = 0; i < rowsLength; i++) {
            for (int j = 0; j < colsLength; j++) {
                if (i == 0 || j == 0 || i == rowsLength - 1 || j == colsLength - 1) {

                    resMap[i][j] = CellInfo.WALL;
                } else if (resMap[i][j] != CellInfo.MONSTER && resMap[i][j] != CellInfo.EXIT) {
                    resMap[i][j] = getRandomCellInfo(probaWall);
                }
            }
        }
        return resMap;
    }

    // retourne un CellInfo random , probaWall en Pourcents
    public CellInfo getRandomCellInfo(int probWall) {
        Random random = new Random();
        double nombreAleatoire = random.nextDouble();

        double probabilite = probWall / 100.0; // Convertit probWall en pourcentage

        if (nombreAleatoire < probabilite) {
            return CellInfo.WALL;
        } else {
            return CellInfo.EMPTY;
        }
    }

    public Coordinate getRandomCellForMonsterOrExit(int rowsLength, int columnsLength) {
        Random random = new Random();
        return new Coordinate(random.nextInt(rowsLength - 2) + 1, random.nextInt(columnsLength - 2) + 1);
    }
}
