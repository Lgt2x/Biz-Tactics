# Biz Tactics
Un Tactical-RPG au tour par tour

### Cadre du projet
Nous avons choisi le 5ème problème, le jeu de plateau, où l'objectif 
est de simuler un combat entre deux entités.
Le style du TRPG s'est imposé naturellement à la lecture de l'énoncé, comme combat entre deux joueurs manipulant des tableaux 2D.
Nous utiliserons l'affichage du TP11 du S2 sur Swing, modifié selon nos besoins.

### Cahier des charges
Sur une grille, chaque joueur contrôle plusieurs personnages  tour à tour. Chaque personnage a des caractéristiques différentes. Le premier joueur qui a éliminé les personnages de l'adversaire gagne.
A chaque tour, le joueur prend le contrôle d'un des personnages (dans un ordre défini au début du jeu). Ce personnage peut effectuer 2 actions: se déplacer, puis attaquer un personnage adverse ou se défendre.
La carte présentera des obstacles limitant le déplacement, et des objets à récupérer (en bonus).

### Répartition du travail
- Zac: Classes de personnages, attaques
- Louis: Input, affichage
- Matias: Gestion du jeu

#### Inspirations
Librement inspiré de la série Fire Emblem, Final Fantasy Tactics, de TRPG

### Planification
La date de rendu est le 3 Juin, ce qui nous laisse un mois de développement environ.

Planning prévisionnel:
 - Semaine du 29/04 : Rédaction du cahier des charges, organisation du code et des tâches
 - Semaine du 6/05 : Début du développement, chacun crée ses classes
 - Semaine du 13/05 : Relier les différentes classes, pour avoir un premier prototype fonctionnel
 - Semaine du 20/05 : Chargement de maps (bonus), des types de personnage, interactions entre personnages
 - Semaine du 27/05 : Peaufinage, réglage des détails, début et fin du jeu

### Organisation du code
```
├── Affichage.java # Gestion de l'affichage
├── GameManager.java # Gestion du plateau, des joueurs
├── Joueur.java # Classe du joueur, contenant les personnages
├── Personnage.java # Classe du personnage, avec ses caractéristiques
├── maps.txt # Maps prédéfinies, à charger
└── types.txt # Définition des types de personnages
```
