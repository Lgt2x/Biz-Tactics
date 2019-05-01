public class GameManager {
    public static Affichage aff;

    public static void main(String[] args) {
        int mapSize = 10;
        int[][] map = new int[mapSize][mapSize];

        /* Test de la map */
        map[0][0] = 1;
        map[mapSize-1][0] = 1;
        map[9][3] = 2;
        map[mapSize-1][mapSize-1] = 1;
        map[0][mapSize-1] = 1;


        Affichage aff = new Affichage(map);

        aff.changeMessage("ex");
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
