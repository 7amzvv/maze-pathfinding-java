# Solveur et visualiseur de labyrinthes

Projet Java consacré à la génération et à la résolution de labyrinthes. Le modèle de grille et la démonstration console sont disponibles.

## Lancement sous Windows

Installez un JDK 17 ou ultérieur et vérifiez que `java` et `javac` sont accessibles depuis le `PATH`. Double-cliquez sur `run.bat` pour compiler les sources dans `bin/` et afficher la grille.

```text
S..#.....
...#.....
...#.....
......#..
......#.E
```

`S` représente le départ, `E` l'arrivée, `#` un mur et `.` une case libre.

## Modèle

`Cell` conserve ses coordonnées, son type, son état de visite et son parent. `Grid` maintient un départ et une arrivée distincts et renvoie les voisins accessibles dans les quatre directions cardinales. L'état de recherche peut être réinitialisé entre deux résolutions.
