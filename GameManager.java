import java.util.Arrays.*;

public class GameManager {
    private static Display aff; // Référence à l'objet d'affichage
    public static int[][] overlay;
    public static int mapX = 15; // Nombre de colonnes de la map
    public static int mapY = 10; // Nombre de lignes de la map
    public static Player player1;
    public static Player player2;

    public Character selectedChar;

    /*
        tour%2 = 1 => Joueur 1
        tour%2 = 0 => Joueur 2
    */

    public static int tour;

    /*
        etape%3 = 0 => Sélection du personnage à jouer
        etape%3 = 1 => Sélection de la case de déplacement
        etape%3 = 2 => Sélection du personnage à attaquer
    */

    public static int etape;

    public GameManager() {
        tour = 1;
        etape = 0;

        player1 = new Player("Player 1");
        player2 = new Player("Player 2");

        player1.addChar("Berserker", "Berserker", 1, 1, 20,  6,  5,  1,  2,  10,  3);
        player1.addChar("Sniper", "Sniper", 1, 5, 1, 10, 5, 3, 5, 10, 3);

        player2.addChar("Knight", "Knight", 13, 1, 30, 10, 3, 1, 1, 10, 1);

        overlay = new int[mapY][mapX];
        aff = new Display(this);

        setupCharSelect();

    }

    /*
     * Méthode déclenchée au clic sur une case, déclenchée par le gestionnaire d'events de la Map
    */
    public static void clickHandle(int x, int y) {
        if (etape%3 == 0) {
            charSelect(x, y);
        } else if (etape%3 == 1) {
            moveSelect(x,y);
        } else if (etape%3 ==2) {
            attackSelect(x,y);
        }
    }

    /**** SELECTION DU PERSONNAGE ****/
    public static void charSelect(int x, int y) {
        if (overlay[x][y] == 1) {
            selectedChar = findChar(x,y);

            etape++;
        }

        setupMoveSelect();
    }

    public static void setupCharSelect() {
        cleanOverlay(); // Nettoyage de l'overlay de l'overlay

        if (tour%2 == 1) { // Si c'est au joueur 1
            for (int i = 0; i < player1.characters.size(); i++) {
                Character character = player1.characters.get(i);

                overlay[character.getPosY()][character.getPosX()] = 1;
                System.out.println(overlay.toString());
                aff.mapPanel.repaint();
            }
        }
    }

    /**** SELECTION DU MOUVEMENT ****/
    public static void moveSelect(int x, int y) {

    }


    public static void setupMoveSelect() {
        cleanOverlay();
    }

    /**** SELECTION DU PERSONNAGE ATTAQUE ****/
    public static void attackSelect(int x, int y) {

    }

    public static void setupAttackSelect() {

    }

    /*
    Cherche si un personnage existe sur cette case
    */
    //public static Character findChar(int x, int y)

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
