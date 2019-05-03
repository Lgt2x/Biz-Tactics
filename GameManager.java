public class GameManager {
    private static Display aff; // Référence à l'objet d'affichage
    public static int[][] map;
    public static int mapX = 15; // Nombre de colonnes de la map
    public static int mapY = 10; // Nombre de lignes de la map
    public static Player player1;
    public static Player player2;

    public GameManager() {

        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");

        player1.addChar("Berserker", "Berserker", 1, 1, 20,  6,  5,  1,  2,  10,  3);
        player1.addChar("Sniper", "Sniper", 1, 5, 1, 10, 5, 3, 5, 10, 3);
        
        player2.addChar("Knight", "Knight", 13, 1, 30, 10, 3, 1, 1, 10, 1);

        map = new int[mapY][mapX];
        Display aff = new Display(this);

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
