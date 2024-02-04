/**
 * La classe MonsterStrategy implémente l'interface IMonsterStrategy pour définir
 * la stratégie du monstre dans le jeu. Elle offre deux méthodes de jeu,
 * l'une utilisant une solution précalculée et l'autre recalculant une solution
 * en tenant compte du brouillard sur la carte.
 *
 * Cette classe est utilisée pour déterminer le comportement du monstre
 * contrôlé par l'ordinateur dans le jeu.
 */
package Model;

import java.util.ArrayList;

import fr.univlille.iutinfo.cam.player.monster.IMonsterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;
import fr.univlille.iutinfo.cam.player.perception.ICoordinate;

public class MonsterStrategy implements IMonsterStrategy {

    // Solveur pour la recherche de solution sur la carte
    Utils.Solveur solve = new Utils.Solveur(GameModel.map.getMaps());

    // Solution précalculée
    ArrayList<Utils.Coordinate> solution = solve.estFaisable(false);

    /**
     * Méthode implémentant la stratégie de base du monstre.
     * Le monstre choisit la première case de la solution précalculée.
     *
     * @return ICoordinate représentant la case choisie par le monstre.
     */
    @Override
    public ICoordinate play() {
        ICoordinate caseToPlay = null;
        if (solution.size() > 0) {
            caseToPlay = solution.get(0);
            solution.remove(0);
        }

        return caseToPlay;
    }

    /**
     * Méthode implémentant la stratégie du monstre en tenant compte du brouillard
     * sur la carte.
     * Le monstre recalculera une solution à chaque appel de cette méthode.
     *
     * @return ICoordinate représentant la case choisie par le monstre.
     */
    public ICoordinate playBrouillard() {
        ICoordinate caseToPlay = null;
        solve.updateModeBrouillard();
        solution = solve.estFaisable(true);
        System.out.println(solution);
        if (solution.size() > 0) {
            caseToPlay = solution.get(0);
        }
        return caseToPlay;
    }

    /**
     * Méthode appelée pour mettre à jour la stratégie du monstre en fonction d'un
     * événement de case.
     *
     * @param arg0 Objet ICellEvent représentant l'événement de case.
     */
    @Override
    public void update(ICellEvent arg0) {
        // do nothing
    }

    /**
     * Méthode d'initialisation appelée lors de la configuration de la stratégie du
     * monstre.
     *
     * @param arg0 Tableau bidimensionnel représentant l'état initial de la carte.
     */
    @Override
    public void initialize(boolean[][] arg0) {
        // do nothing
    }
}
