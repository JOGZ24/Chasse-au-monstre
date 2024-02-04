/**
 * La classe Coordinate représente une paire d'indices (ligne, colonne) sur une grille.
 * Elle implémente l'interface ICoordinate pour représenter les coordonnées d'une case.
 * Chaque instance de Coordinate peut également avoir une référence à un Coordinate parent.
 *
 * Cette classe est utilisée pour représenter les coordonnées des cases dans le jeu.
 */
package Utils;

import java.util.Objects;

import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

public class Coordinate implements ICoordinate {

    // Indices de la ligne et de la colonne
    private int row;
    private int col;

    // Coordinate parent (optionnel)
    private Coordinate father;

    /**
     * Constructeur de la classe Coordinate.
     *
     * @param row Indice de la ligne.
     * @param col Indice de la colonne.
     */
    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
        this.father = null;
    }

    /**
     * Constructeur de la classe Coordinate avec référence à un Coordinate parent.
     *
     * @param row    Indice de la ligne.
     * @param col    Indice de la colonne.
     * @param father Coordinate parent.
     */
    public Coordinate(int row, int col, Coordinate father) {
        this.row = row;
        this.col = col;
        this.father = father;
    }

    /**
     * Obtient l'indice de la colonne.
     *
     * @return L'indice de la colonne.
     */
    @Override
    public int getCol() {
        return this.col;
    }

    /**
     * Obtient l'indice de la ligne.
     *
     * @return L'indice de la ligne.
     */
    @Override
    public int getRow() {
        return this.row;
    }

    /**
     * Obtient le Coordinate parent.
     *
     * @return Le Coordinate parent.
     */
    public Coordinate getFather() {
        return father;
    }

    /**
     * Modifie l'indice de la ligne.
     *
     * @param row Nouvel indice de la ligne.
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Modifie l'indice de la colonne.
     *
     * @param col Nouvel indice de la colonne.
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Compare cette instance de Coordinate avec un autre objet pour l'égalité.
     *
     * @param obj Objet à comparer.
     * @return true si les objets sont égaux, false sinon.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Coordinate other = (Coordinate) obj;
        return this.row == other.row && this.col == other.col;
    }

    /**
     * Génère un code de hachage pour cette instance de Coordinate.
     *
     * @return Code de hachage.
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Convertit les indices de ligne et de colonne en un tableau d'entiers.
     *
     * @return Tableau d'entiers [row, col].
     */
    int[] toArray() {
        return new int[] { row, col };
    }

    /**
     * Retourne une représentation sous forme de chaîne de cette instance de
     * Coordinate.
     *
     * @return Chaîne de caractères représentant Coordinate.
     */
    @Override
    public String toString() {
        return "Coordinate [row=" + row + ", col=" + col + "]";
    }
}
