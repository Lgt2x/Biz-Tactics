public class GameManager {
    private static Affichage aff;
    public static int[][] map;

    public static void main(String[] args) {
        int mapSize = 10;
        map = new int[mapSize][mapSize];

        /* Test de la map */
        map[0][0] = 1;
        map[mapSize-1][0] = 1;
        map[9][3] = 2;
        map[mapSize-1][mapSize-1] = 1;
        map[0][mapSize-1] = 1;

        Affichage aff = new Affichage(this);

        Joueur player1 = new Joueur("Joueur 1");
        player1.addPerso("Mage", "mage", 1, 1, 20,  5,  5,  2,  2,  10,  3);
    }

    public static void pause(int ms) {
        try{
            Thread.sleep(ms);
        }catch(InterruptedException e){}
    }

    public static void clic() {
        aff.changeMessage("clic!");
    }
}
