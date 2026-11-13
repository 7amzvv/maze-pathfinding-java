# Solveur et visualiseur de labyrinthes

Projet Java consacré à la génération et à la résolution de labyrinthes. La démonstration console génère un labyrinthe à chaque lancement.

## Lancement sous Windows

Installez un JDK 17 ou ultérieur et vérifiez que `java` et `javac` sont accessibles depuis le `PATH`. Double-cliquez sur `run.bat` pour compiler les sources dans `bin/` et afficher la grille.

`S` représente le départ, `E` l'arrivée, `#` un mur et `.` une case libre.

## Modèle

`Cell` conserve ses coordonnées, son type, son état de visite et son parent. `Grid` maintient un départ et une arrivée distincts et renvoie les voisins accessibles dans les quatre directions cardinales. L'état de recherche peut être réinitialisé entre deux résolutions.

## Génération

`MazeGenerator` applique un parcours en profondeur aléatoire avec retour arrière (*Recursive Backtracker*). Depuis la case courante, il choisit une case voisine encore inexplorée, à deux pas, et ouvre le mur entre les deux. Quand il est bloqué, il remonte dans la pile. Chaque nouvelle case se raccorde par un seul passage au réseau déjà creusé, ce qui donne un labyrinthe connexe et sans cycle. [Présentation de l'algorithme par Jamis Buck](https://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking).

La génération accepte des grilles d'au moins 3 × 3, paires ou impaires. L'arrivée occupe la dernière case du réseau, aux coordonnées paires en bas à droite. Une dernière ligne ou colonne de murs complète les dimensions paires. Un générateur aléatoire fourni par l'appelant permet de reproduire un labyrinthe à partir d'une graine fixe.
