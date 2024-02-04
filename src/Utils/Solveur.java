package Utils;

import java.util.ArrayList;
import java.util.Collections;

import fr.univlille.iutinfo.cam.player.perception.ICellEvent.CellInfo;

public class Solveur {

    private CellInfo[][] map;
    private CellInfo[][] mapBrouillard;
    // les marques
    private char[][] marque;

    public Solveur(CellInfo[][] map) {
        this.map = map;
        initMarque();

    }

    public void setMap(CellInfo[][] map) {
        this.map = map;
    }

    public CellInfo[][] getMap() {
        return map;
    }

    private int[] getEntre() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == CellInfo.MONSTER)
                    return new int[] { i, j };
            }
        }
        return null;
    }

    private int[] getSortie() {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == CellInfo.EXIT)
                    return new int[] { i, j };
            }
        }
        return null;
    }

    // est-ce que (x,y) est un mur ?
    private boolean estMur(int x, int y) {
        return map[x][y].equals(CellInfo.WALL);
    }

    // est-ce que (x,y) est un mur ?
    private boolean estMurBrouillard(int x, int y) {
        return mapBrouillard[x][y].equals(CellInfo.WALL);
    }

    private final char VIDE = 'a', ROSE = 'c';

    private void initMarque() {
        marque = new char[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                marque[i][j] = VIDE;
            }
        }
    }

    // teste si la cellule (x,y) comporte une marque
    private boolean estMarque(int x, int y) {
        return marque[x][y] != VIDE;
    }

    // pose une marque
    private void poserMarque(int x, int y) {
        marque[x][y] = ROSE;
    }

    private void poserMarque(int[] c) {
        marque[c[0]][c[1]] = ROSE;
    }

    // renvoi une ArrayList du plus court chemin en partant du monstre et commencant
    // directement par le premier coup à jouer
    public ArrayList<Coordinate> estFaisable(boolean brouillard) {
        initMarque();
        File<Coordinate> p = new File<Coordinate>();
        int[] entre = getEntre();
        int[] sortie = getSortie();
        Coordinate end = new Coordinate(sortie[0], sortie[1]);
        Coordinate start = new Coordinate(entre[0], entre[1]);
        Coordinate voisin;
        boolean found = false;
        p.push(new Coordinate(entre[0], entre[1]));
        poserMarque(start.getRow(), start.getCol());
        Coordinate c;
        while (!p.isEmpty()) {
            c = p.peek();
            if (c.equals(end)) {
                found = true;
                break;
            } else {
                if(brouillard) voisin = getVoisinesDiagonalBrouillard(c);
                else voisin = getVoisinesDiagonal(c);
                if (voisin != null) {
                    p.push(voisin);
                    poserMarque(voisin.toArray());
                } else {
                    p.pop();
                }
            }
        }
        if (found) {
            ArrayList<Coordinate> res = new ArrayList<>();
            Coordinate tmp = p.pop();
            while (!tmp.equals(start)) {
                res.add(tmp);
                tmp = tmp.getFather();
            }
            reverseArrayList(res);
            return res;
        } else {
            return null;
        }
    }

    private Coordinate getVoisinesDiagonal(Coordinate c) {
        int[] cell = c.toArray();
        if (!estMarque(cell[0], cell[1] + 1) && !estMur(cell[0], cell[1] + 1)) {
            return new Coordinate(cell[0], cell[1] + 1, c);
        }
        if (!estMarque(cell[0] + 1, cell[1]) && !estMur(cell[0] + 1, cell[1])) {
            return new Coordinate(cell[0] + 1, cell[1], c);
        }
        if (!estMarque(cell[0] - 1, cell[1]) && !estMur(cell[0] - 1, cell[1])) {
            return new Coordinate(cell[0] - 1, cell[1], c);
        }
        if (!estMarque(cell[0], cell[1] - 1) && !estMur(cell[0], cell[1] - 1)) {
            return new Coordinate(cell[0], cell[1] - 1, c);
        }

        if (!estMarque(cell[0] + 1, cell[1] + 1) && !estMur(cell[0] + 1, cell[1] + 1)) {
            return new Coordinate(cell[0] + 1, cell[1] + 1, c);
        }
        if (!estMarque(cell[0] - 1, cell[1] + 1) && !estMur(cell[0] - 1, cell[1] + 1)) {
            return new Coordinate(cell[0] - 1, cell[1] + 1, c);
        }
        if (!estMarque(cell[0] - 1, cell[1] - 1) && !estMur(cell[0] - 1, cell[1] - 1)) {
            return new Coordinate(cell[0] - 1, cell[1] - 1, c);
        }
        if (!estMarque(cell[0] + 1, cell[1] - 1) && !estMur(cell[0] + 1, cell[1] - 1)) {
            return new Coordinate(cell[0] + 1, cell[1] - 1, c);
        }
        return null;
    }
    
    private Coordinate getVoisinesDiagonalBrouillard(Coordinate c) {
        int[] cell = c.toArray();
        if (!estMarque(cell[0], cell[1] + 1) && !estMurBrouillard(cell[0], cell[1] + 1)) {
            return new Coordinate(cell[0], cell[1] + 1, c);
        }
        if (!estMarque(cell[0] + 1, cell[1]) && !estMurBrouillard(cell[0] + 1, cell[1])) {
            return new Coordinate(cell[0] + 1, cell[1], c);
        }
        if (!estMarque(cell[0] - 1, cell[1]) && !estMurBrouillard(cell[0] - 1, cell[1])) {
            return new Coordinate(cell[0] - 1, cell[1], c);
        }
        if (!estMarque(cell[0], cell[1] - 1) && !estMurBrouillard(cell[0], cell[1] - 1)) {
            return new Coordinate(cell[0], cell[1] - 1, c);
        }

        if (!estMarque(cell[0] + 1, cell[1] + 1) && !estMurBrouillard(cell[0] + 1, cell[1] + 1)) {
            return new Coordinate(cell[0] + 1, cell[1] + 1, c);
        }
        if (!estMarque(cell[0] - 1, cell[1] + 1) && !estMurBrouillard(cell[0] - 1, cell[1] + 1)) {
            return new Coordinate(cell[0] - 1, cell[1] + 1, c);
        }
        if (!estMarque(cell[0] - 1, cell[1] - 1) && !estMurBrouillard(cell[0] - 1, cell[1] - 1)) {
            return new Coordinate(cell[0] - 1, cell[1] - 1, c);
        }
        if (!estMarque(cell[0] + 1, cell[1] - 1) && !estMurBrouillard(cell[0] + 1, cell[1] - 1)) {
            return new Coordinate(cell[0] + 1, cell[1] - 1, c);
        }
        return null;
    }


    public static <T> void reverseArrayList(ArrayList<T> list) {
        Collections.reverse(list);
    }

    public void updateModeBrouillard() {
        int[] entre = getEntre();
        mapBrouillard = new CellInfo[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (i == 0 || j == 0 || i == map.length - 1 || j == map[0].length - 1) {
                    mapBrouillard[i][j] = CellInfo.WALL;
                } else if (map[i][j] == CellInfo.MONSTER) {
                    mapBrouillard[i][j] = CellInfo.MONSTER;
                } else if (map[i][j] == CellInfo.EXIT) {
                    mapBrouillard[i][j] = CellInfo.EXIT;
                } else {
                    mapBrouillard[i][j] = CellInfo.EMPTY;
                }
            }
        }
        
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (entre[0] + i >= 0 && entre[0] + i < map.length && entre[1] + j >= 0 && entre[1] + j < map[0].length) {
                    mapBrouillard[entre[0] + i][entre[1] + j] = map[entre[0] + i][entre[1] + j];
                }
            }
        }
    }
    
}
