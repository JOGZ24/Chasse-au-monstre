/**
 * La classe HunterStrategy implémente l'interface IHunterStrategy pour définir
 * la stratégie du chasseur dans le jeu. Elle offre deux méthodes de jeu,
 * l'une basique (play) et l'autre plus avancée (smartPlay) en prenant en compte
 * la dernière case cliquée et la vue du chasseur.
 *
 * Cette classe est utilisée pour déterminer le comportement du chasseur
 * contrôlé par l'ordinateur dans le jeu.
 */
package Model;

import Utils.Coordinate;
import View.VueHunter;
import fr.univlille.iutinfo.cam.player.hunter.IHunterStrategy;
import fr.univlille.iutinfo.cam.player.perception.ICellEvent;

public class HunterStrategy implements IHunterStrategy {

   // Taille de la carte du jeu
   Coordinate sizeOfMap;

   /**
    * Méthode implémentant la stratégie basique du chasseur.
    * Le chasseur choisit aléatoirement une case sur la carte.
    *
    * @return Coordinate représentant la case choisie par le chasseur.
    */
   @Override
   public Coordinate play() {
      int tailleMinCol = 1;
      int tailleMaxCol = sizeOfMap.getCol() - 1;
      int tailleMinRow = 1;
      int tailleMaxRow = sizeOfMap.getRow() - 1;

      int caseToPlayCol = tailleMinCol + (int) (Math.random() * (tailleMaxCol - tailleMinCol));
      int caseToPlayRow = tailleMinRow + (int) (Math.random() * (tailleMaxRow - tailleMinRow));

      Coordinate caseToPlay = new Coordinate(caseToPlayRow, caseToPlayCol);
      return caseToPlay;
   }

   /**
    * Méthode implémentant une stratégie plus avancée du chasseur en tenant compte
    * de la dernière case cliquée et de la vue du chasseur.
    *
    * @param lastCaseClicked Dernière case cliquée par le chasseur.
    * @param view            Vue du chasseur.
    * @return Coordinate représentant la case choisie par le chasseur.
    */
   public Coordinate smartPlay(Coordinate lastCaseClicked, VueHunter view) {
      if (lastCaseClicked.getRow() == -1) {
         return play();
      }
      if (view.getHunter().getGameModel().getPath().containsKey(lastCaseClicked)) {
         int nbTour = view.getHunter().getGameModel().getPath().get(lastCaseClicked);

         int maxCol = lastCaseClicked.getCol() + nbTour;
         int minCol = lastCaseClicked.getCol() - nbTour;
         int maxRow = lastCaseClicked.getRow() + nbTour;
         int minRow = lastCaseClicked.getRow() - nbTour;

         int tailleCol = GameModel.map.getCol();
         int tailleRow = GameModel.map.getRow();

         if (maxCol >= tailleCol)
            maxCol = tailleCol - 1;
         if (minCol <= 0)
            minCol = 1;
         if (maxRow >= tailleRow)
            maxRow = tailleRow - 1;
         if (minRow <= 0)
            minRow = 1;

         int caseToPlayCol = minCol + (int) (Math.random() * (maxCol - minCol));
         int caseToPlayRow = minRow + (int) (Math.random() * (maxRow - minRow));

         Coordinate caseToPlay = new Coordinate(caseToPlayRow, caseToPlayCol);
         return caseToPlay;
      }
      return play();
   }

   /**
    * Méthode appelée pour mettre à jour la stratégie du chasseur en fonction d'un
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
    * chasseur.
    *
    * @param arg0 Taille de la carte (nombre de colonnes).
    * @param arg1 Taille de la carte (nombre de lignes).
    */
   @Override
   public void initialize(int arg0, int arg1) {
      this.sizeOfMap = new Coordinate(arg0, arg1);
   }
}
