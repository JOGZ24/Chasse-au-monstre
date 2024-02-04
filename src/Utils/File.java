package Utils;

import java.util.ArrayList;
import java.util.EmptyStackException;

/**
 * La classe File représente une file (FIFO - First In, First Out) générique.
 *
 * @param <E> Le type des éléments stockés dans la file.
 */
public class File<E> {

    /** Liste interne pour stocker les éléments de la file. */
    public ArrayList<E> cells;

    /**
     * Constructeur de la classe File. Initialise la liste interne.
     */
    public File() {
        this.cells = new ArrayList<E>();
    }

    /**
     * Ajoute un élément à la fin de la file.
     *
     * @param e L'élément à ajouter.
     */
    public void push(E e) {
        if (e.getClass().equals(Coordinate.class)) {
            cells.add(e);
        }
    }

    /**
     * Supprime et renvoie l'élément en tête de la file.
     *
     * @return L'élément en tête de la file.
     * @throws EmptyStackException Si la file est vide.
     */
    public E pop() {
        if (cells.isEmpty()) {
            throw new EmptyStackException();
        }
        return cells.remove(0);
    }

    /**
     * Renvoie l'élément en tête de la file sans le supprimer.
     *
     * @return L'élément en tête de la file.
     * @throws EmptyStackException Si la file est vide.
     */
    public E peek() {
        if (cells.isEmpty()) {
            throw new EmptyStackException();
        }
        return cells.get(0);
    }

    /**
     * Vérifie si la file contient un seul élément.
     *
     * @return True si la file contient un seul élément, sinon False.
     */
    public boolean isLast() {
        return cells.size() == 1;
    }

    /**
     * Vérifie si la file est vide.
     *
     * @return True si la file est vide, sinon False.
     */
    public boolean isEmpty() {
        return cells.isEmpty();
    }

    public ArrayList<E> getCells() {
        return cells;
    }
}
