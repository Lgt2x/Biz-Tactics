public class GameManager {
    private static Display aff; // Référence à l'objet d'affichage
    public static int[][] map;
    public static int mapX = 15; // Nombre de colonnes de la map
    public static int mapY = 10; // Nombre de lignes de la map

    public GameManager() {
        map = new int[mapY][mapX];

        /* Test de la map */
        map[mapY-1][0] = 1;
        map[1][3] = 2;

        Display aff = new Display(this);

        Player player1 = new Player("Player 1");
        player1.addChar("Mage", "mage", 1, 1, 20,  5,  5,  2,  2,  10,  3);
    }

    /*
     * Méthode déclenchée au clic sur une case, déclenchée par le gestionnaire d'events de la Map
    */
    public static void clic() {
        aff.changeMessage("clic!");
    }

    public static void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch(InterruptedException e){}
    }
}
