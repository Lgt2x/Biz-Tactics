# Biz Tactics

Un Tactical-RPG au tour par tour.

## Cadre du projet

Nous avons choisi le 5ème problème, le jeu de plateau, où l'objectif est de simuler un combat entre deux entités. Le style du TRPG s'est imposé naturellement à la lecture de l'énoncé, comme combat entre deux joueurs manipulant des tableaux 2D.

## Cahier des charges

Sur une grille, chaque joueur contrôle plusieurs personnages tour à tour. Chaque personnage a des caractéristiques différentes. Le premier joueur qui a éliminé les personnages de l'adversaire gagne.

A chaque tour, le joueur prend le contrôle d'un des personnages (dans un ordre défini au début du jeu). Ce personnage peut effectuer 2 actions: se déplacer, puis attaquer un personnage adverse ou se défendre.

La carte présentera des obstacles limitant le déplacement, et des objets à récupérer (en bonus).

Nous utiliserons Swing et AWT pour l'affichage graphique et la gestion des évements. Deux composants principaux: la carte de jeu, sous forme de grille, et une boite de texte en dessous pour afficher diverses informations.

Le contrôle du jeu est fait à la souris principalement, pour le choix de la case de déplacement, l'attaque etc.

## Répartition du travail

- Zac: Classes de personnages, attaques
- Louis: Input, affichage
- Matias: Gestion du jeu

### Inspirations

Gameplay librement inspiré de la série Fire Emblem, de Final Fantasy Tactics...

## Planification

La date de rendu est le 3 Juin, ce qui nous laisse un mois de développement environ.

Planning prévisionnel:

- Semaine du 29/04 : Rédaction du cahier des charges, organisation du code et des tâches.
- Semaine du 6/05 : Début du développement, chacun crée ses classes de son côté.
- Semaine du 13/05 : Relier les différentes classes, pour avoir un premier prototype fonctionnel.
- Semaine du 20/05 : Chargement de maps (bonus), des types de personnage, interactions entre personnages.
- Semaine du 27/05 : Peaufinage, réglage des détails, début et fin du jeu

## Organisation du code

```
├── Launch.java # Lancement du jeu
├── GameManager.java # Gestion du plateau, des joueurs
├── Affichage.java # Gestion de l'affichage
├── Map.java # Gestion de la map
├── Joueur.java # Classe du joueur, contenant les personnages
├── Personnage.java # Classe du personnage, avec ses caractéristiques
├── maps.txt # Maps prédéfinies, à charger
├── Assets # Images utilisées, sons si besoin...
│   └── Img1.png
│   └── ...
└── types.txt # Définition des types de personnages ainsi que leurs caractéristiques
```

## Run

Windows
```bash
./gradlew play 
```

Unix
```bash
./play.sh
```

## Todo

Objectifs à court terme (10 mins à 2h)

- Calcul des cases possibles de déplacement et d'attaque selon la distance (difficile)
- Animation pour l'attaque
- Animation pour le déplacement
- Equilibrage du jeu
- Définition des couleurs dans une classe, proprement