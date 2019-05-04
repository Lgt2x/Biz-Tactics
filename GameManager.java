import java.util.Arrays.*;

public class GameManager {
    private static Display aff; // Référence à l'objet d'affichage
    public static int[][] overlay;
    public static int mapX = 15; // Nombre de colonnes de la map
    public static int mapY = 10; // Nombre de lignes de la map
    public static Player[] players;

    public static Character selectedChar;

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
    public static boolean gameOver = false;

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
        aff = new Display(this);

        setupCharSelect();
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
        if (overlay[y][x] == 1) {
            selectedChar = findChar(x, y, players[tour%2]);
            step = 1;

            setupMoveSelect();
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
                System.out.println(character);
            }

            if (counter == 0) {
                gameOver = true;
                System.out.println("Partie terminée");
                aff.changeMessage("Partie terminée");
            }
        }
    }

    /**** SELECTION DU MOUVEMENT ****/
    public static void moveSelect(int x, int y) {
        if (overlay[y][x] == 2) {
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

        overlay [selectedChar.getPosY()][selectedChar.getPosX()] = 1;

        for (int offsetX = -selectedChar.speed; offsetX <= selectedChar.speed; offsetX++) {
            for (int offsetY = Math.abs(offsetX) - selectedChar.speed; offsetY <= -Math.abs(offsetX) + selectedChar.speed; offsetY++) {

                x = selectedChar.getPosX()+offsetX;
                y = selectedChar.getPosY()+offsetY;

                if (x >= 0 && x < mapX && y >= 0 && y < mapY && findChar(x, y, players[(tour+1)%2]) == null && findChar(x, y, players[tour%2]) == null) { // && !(x == 0 && y == 0)
                    overlay[y][x] = 2;
                }
            }
        }

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
