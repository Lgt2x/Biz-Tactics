public class GameManager {
    public static void main(String[] args) {
        int mapSize = 10;
        int[][] map = new int[mapSize][mapSize];

        map[0][0] = 1;
        map[mapSize-1][0] = 1;
        map[9][3] = 2;
        map[mapSize-1][mapSize-1] = 1;
        map[0][mapSize-1] = 1;
        Affichage2.afficherPlateau(map);
    }

    public static void pause(int ms) {
        try{
            Thread.sleep(ms);
        }catch(InterruptedException e){}
    }
}