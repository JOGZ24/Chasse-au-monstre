## Rapport de Développement Efficace de la SAE Chasse au Monstre

| Groupe J4 |       |
|------------|-----------|
| Milleville | Paul      |
| Martel     | Alexandre |
| Bouin      | Julien    |
| Fouconnier | Alexy     |


### Génération de labyrinthes :

La génération aléatoire (`randomInitMap()` dans la classe `Maps`) commence par créer un labyrinthe vierge, puis mettre tous les bords en mur. Ensuite, elle place le Monstre et la sortie. Enfin, elle ajoute avec une certaine probabilité des murs à l'intérieur du labyrinthe. Cette génération utilise la fonction `estFaisable()` de la classe `Solveur` pour vérifier la viabilité du labyrinthe. Si la sortie n'est pas trouvée, la fonction retourne une solution null, permettant à la fonction de création du labyrinthe (`initCheckedMap()` dans la classe Maps) de relancer une génération aléatoire.

### IA Monstre :

L'IA du monstre repose sur un algorithme de recherche du chemin le plus court utilisant les **Files**. Elle cherche en parallèle le chemin le plus court du monstre jusqu'à la sortie. La fonction s'appelle `estFaisable()` et se trouve dans la classe `Solveur`. Elle renvoie une `ArrayList` de `Coordinate` représentant le chemin le plus court, case par case, à travers le labyrinthe.

Pour le brouillard, on utilise la même fonction `estFaisable()`, ainsi qu'une fonction `updateBrouillard()` dans la classe pour mettre à jour la vue du chasseur en tenant compte du brouillard. C'est-à-dire que le bot monstre ne voit qu'une ou deux cases autour de lui. Cela permet une réaction plus réaliste du bot Monstre dans ce mode de jeux.


```
ArrayList<Coordinate> estFaisable(boolean brouillard)
    initialiser les marques 
    créer une file p pour stocker les coordonnées 
    obtenir les coordonnées de départ et d'arrivée
    créer un objet Coordinate end avec les coordonnées de sortie 
    créer un objet Coordinate start avec les coordonnées de entre 
    initialiser la variable booléenne trouvé à faux
    pousser la coordonnée start sur la file p
    marquer la coordonnée start comme visitée 
    tant que la file p n'est pas vide : 
        dépiler la coordonnée en haut de la file p et la stocker dans c
        si c est égale à la coordonnée end : 
            mettre trouvé à vrai 
        sinon : 
            si brouillard est vrai : 
                obtenir les voisins de c en présence de brouillard
                stocker le premier voisin valide dans voisin 
            sinon :
                obtenir les voisins de c 
                stocker le premier voisin valide dans voisin
            si voisin n'est pas null : 
                pousser voisin sur la file p
                marquer voisin comme visité 
                sortir de la boucle
            sinon : 
                dépiler c de la file p
    si trouvé est vrai : 
        créer un objet ArrayList res 
        créer un objet Coordinate temporaire tmp qui prend la valeur du dépilement de la file p 
        tant que tmp n'est pas égale à la coordonnée start : 
            ajouter tmp à la liste res 
            tmp = tmp.getFather()
        inverser la liste res 
        renvois de fonction de res 
    sinon : 
        return null

```

### IA Chasseur

En ce qui concerne le bot chasseur, ce dernier repose malheureusement principalement sur le hasard. En effet, dès que le chasseur tire, il sélectionne de manière aléatoire une case du labyrinthe sur laquelle tirer. Cependant, pour tenter d'augmenter les chances de victoire de notre bot, nous avons décidé d'implémenter une autre solution.

Lorsque le chasseur souhaite tirer, il va vérifier si la dernière case sur laquelle il a tiré fait partie du chemin emprunté par le monstre. Si c'est le cas, cela va permettre de réduire le périmètre des possibilités du prochain tir. En effet, si la dernière case contient un nombre correspondant au passage du monstre, le chasseur va calculer en fonction du nombre de tours actuel la distance maximale dans laquelle le monstre peut se trouver. Ainsi, il réduit le champ de tir maximal et augmente ses chances de tirer sur le monstre.

Il y une fonction qui tire en aléatoire (`play()`)

```
Coordinate smartPlay(Coordinate lastCaseClicked, VueHunter view):
    Si lastCaseClicked.getRow() == -1 Alors
        Retourner play()
    Fin Si

    Si view.getHunter().getGameModel().getPath().contientClé(lastCaseClicked) Alors
        nbTour = view.getHunter().getGameModel().getPath().get(lastCaseClicked)

        maxCol = lastCaseClicked.getCol() + nbTour
        minCol = lastCaseClicked.getCol() - nbTour
        maxRow = lastCaseClicked.getRow() + nbTour
        minRow = lastCaseClicked.getRow() - nbTour

        tailleCol = GameModel.map.getCol()
        tailleRow = GameModel.map.getRow()

        Si maxCol >= tailleCol Alors
            maxCol = tailleCol - 1
        Fin Si
        Si minCol <= 0 Alors
            minCol = 1
        Fin Si
        Si maxRow >= tailleRow Alors
            maxRow = tailleRow - 1
        Fin Si
        Si minRow <= 0 Alors
            minRow = 1
        Fin Si

        caseToPlayCol = minCol + entierAleatoireEntre(0, maxCol - minCol)
        caseToPlayRow = minRow + entierAleatoireEntre(0, maxRow - minRow)

        caseToPlay = nouvelleCoordinate(caseToPlayRow, caseToPlayCol)
        Retourner caseToPlay
    Fin Si

    Retourner play()
Fin Fonction
```

### Structures de données :

Pour les structures de données utilisées, nous avons utilisé la même classe (`Solveur`), qui utilise un algorythme de recherche du plus court chemin avec comme structure de données une **File**. En effet, cette structure permet une recherche en largeur (ou en parallèle). Nous avons fait le choix d'un algorithme qui est plus efficace si la  sortie est à côté de l'entrée au contraire d'une sortie plus éloigné de l'entrée.

### Efficacité :

#### Choix d'algorithme :

L'algorithme utilisé est une version de **BFS** (`Breadth-First Search`), qui est bien adapté pour trouver le chemin le plus court dans un graphe non pondéré, comme c'est le cas ici pour le labyrinthe. Le BFS garantit de trouver le chemin le plus court en explorant les voisins de manière systématique grâce à un parcours en largeur des cases visités.

#### Choix de structure de données :

Utiliser une **File** (`First In First Out`) pour le parcours en largeur est une approche naturelle. Cela garantit que les positions sont explorées dans l'ordre où elles ont été découvertes, assurant ainsi la découverte du chemin le plus court en premier.