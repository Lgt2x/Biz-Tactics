import java.util.Arrays.*;

public class GameManager {
    private static Display aff; // Référence à l'objet d'affichage
    public static int[][] overlay; // Tableau 2D qui représente les cases de l'overlay sur la map
    public static int mapX = 15; // Nombre de colonnes de la map
    public static int mapY = 10; // Nombre de lignes de la map
    public static int[][] background; // Fond, avec ses obstacles

    public static boolean gameOver = false; // Vrai lorsque la partie est finie
    public static Player[] players; // Liste de joueurs
    public static Character selectedChar; // Personnage joué ce tour, pour le déplacement et l'attaque

    /*
        tour%2 = 0 => Joueur 1
        tour%2 = 1 => Joueur 2
    */

    public static int tour;

    /*
        step%3 = 0 => Sélection du personnage à jouer
        step%3 = 1 => Sélection de la case de déplacement
        step%3 = 2 => Sélection du personnage à attaquer
    */

    public static int step;

    public GameManager() {
        tour = 0;
        step = 0;

        players = new Player[] {
            new Player("Player 1"),
            new Player("Player 2")
        };

        players[0].addChar("Berserker", "Berserker", 11, 1, 20,  6,  50,  1,  2,  10,  3);
        players[0].addChar("Sniper", "Sniper", 10, 5, 1, 10, 5, 3, 5, 10, 3);
        players[1].addChar("Knight", "Knight", 13, 1, 30, 10, 3, 1, 1, 10, 1);


        overlay = new int[mapY][mapX];
        background = new int[mapY][mapX];

        background[1][1] = 1;
        background[2][10] = 1;

        aff = new Display(this); // Instanciation de la classe d'affichage

        setupCharSelect(); // Déclenchement du jeu en initialisant le premier tour
        aff.mapPanel.repaint();

    }

    /*
     * Méthode déclenchée au clic sur une case, déclenchée par le gestionnaire d'events de la Map
    */
    public static void clickHandle(int x, int y) {
        if (!gameOver) {
            if (step%3 == 0) {
                charSelect(x, y);
            } else if (step%3 == 1) {
                moveSelect(x,y);
            } else if (step%3 == 2) {
                attackSelect(x,y);
            }
        }
    }

    /**** SELECTION DU PERSONNAGE ****/
    public static void charSelect(int x, int y) {
        if (overlay[y][x] == 1) { // Si la case cliquée est effectivement un personnage
            selectedChar = findChar(x, y, players[tour%2]); // Recherche le personnage positionné sur la case (x,y)
            step = 1;

            setupMoveSelect(); // Setup de l'étape suivante
            aff.mapPanel.repaint();
        }
    }

    public static void setupCharSelect() {
        cleanOverlay(); // Nettoyage de l'overlay de l'overlay

        Character character;
        int counter = 0;

        for (int i = 0; i < players[tour%2].characters.size(); i++) {
            character = players[tour%2].characters.get(i);
            if (character.isAlive()) {
                overlay[character.getPosY()][character.getPosX()] = 1; // Le personnage peut être sélectionné, on le met en vert
                counter++;
            }

            if (counter == 0) { // Si il n'y a plus aucun personnage vivant, la partie est terminée
                gameOver = true;
                aff.changeMessage("Partie terminée");
            }
        }
    }

    /**** SELECTION DU MOUVEMENT ****/
    public static void moveSelect(int x, int y) {
        if (overlay[y][x] == 2 || overlay[y][x] == 3) {
            selectedChar.moveTo(x, y);
            aff.repaint();
            step = 2;

            setupAttackSelect();
        }
    }


    public static void setupMoveSelect() {
        cleanOverlay();
        int x;
        int y;

        for (int offsetX = -selectedChar.speed; offsetX <= selectedChar.speed; offsetX++) {
            for (int offsetY = Math.abs(offsetX) - selectedChar.speed; offsetY <= -Math.abs(offsetX) + selectedChar.speed; offsetY++) {

                x = selectedChar.getPosX()+offsetX;
                y = selectedChar.getPosY()+offsetY;

                if (x >= 0 && x < mapX && y >= 0 && y < mapY && findChar(x, y, players[(tour+1)%2]) == null && findChar(x, y, players[tour%2]) == null) { // && !(x == 0 && y == 0)
                    overlay[y][x] = 2;
                }
            }
        }

        overlay [selectedChar.getPosY()][selectedChar.getPosX()] = 2; // Le personnage peut aussi ne pas se déplacer

        aff.mapPanel.repaint();
    }

    /**** SELECTION DU PERSONNAGE ATTAQUE ****/
    public static void attackSelect(int x, int y) {
        if (overlay[y][x] == 4) {
            Character adversary = findChar(x, y, players[(tour+1)%2]);
            selectedChar.attack(adversary);

            step = 0;
            tour++;

            aff.changeMessage("Attaqué");

            setupCharSelect();
        }
    }

    public static void setupAttackSelect() {
        cleanOverlay();

        int x;
        int y;
        int counter = 0;

        for (int offsetX = -selectedChar.range; offsetX <= selectedChar.range; offsetX++) {
            for (int offsetY = Math.abs(offsetX) - selectedChar.range; offsetY <= -Math.abs(offsetX) + selectedChar.range; offsetY++) {
                x = selectedChar.getPosX()+offsetX;
                y = selectedChar.getPosY()+offsetY;

                if (x >= 0 && x < mapX && y >= 0 && y < mapY && findChar(x, y, players[(tour+1)%2]) != null) {
                    overlay[y][x] = 4;
                    counter++;
                }
            }
        }

        if (counter == 0) { // Si il n'y a personne à attaquer
            step = 0;
            tour++;
            setupCharSelect();
        }
        aff.mapPanel.repaint();
    }

    /*
    Cherche si un personnage existe sur cette case
    */
    public static Character findChar (int x, int y, Player player) {
        Character character;
        for (int i = 0; i < player.characters.size(); i++) {
            character = player.characters.get(i);

            if (character.getPosX() == x && character.getPosY() == y) {
                return character;
            }
        }

        return null;
    }

    /*
    Nettoie l'overlay
    */
    public static void cleanOverlay() {
        for (int i=0; i<mapY; i++) {
            for (int j=0; j<mapX; j++) {
                overlay[i][j] = 0;
            }
        }
    }

    public static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch(InterruptedException e){}
    }
}
