import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;


public class Affichage extends JFrame {

    private static Affichage world = null;
    private PanneauGrille pg;

    private Affichage(int[][] p) {
        super("T-RPG"); // Nom de la fenêtre
        pg = new PanneauGrille(p); // Grille d'affichage de la map
        setContentPane(pg); // Panel qui contient des éléments
        pack(); // Arrange la fenêtre

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Sort du jeu lors du clic sur le bouton de fermeture
        setLocationRelativeTo(null); // Centre
        setResizable(false); // Le fenêtre ne peut pas être redimensionnée
        setVisible(true); // La fenêtre est visible
    }

    public static void afficherPlateau(int[][] map) { // L'affichage est demandé
        if (world == null) // Si le map n'est pas encore créé, on crée une fenêtre pour l'affichage
            world = new Affichage(map);

        world.pg.map = map;
        world.repaint();
    }

    /**
     * Calcule la resolution la plus appropriee a la taille de map de
     * facon a ce que la fenetre occupe 80% de la hauteur ou de la
     * largeur de la zone utile de l'ecran
     * de l'ecran.
     *
     * @param monde le map à afficher
     */
    private static int calcRes(int[][] monde) {
        final double p = .9; // pourcentage de la zone utile a occuper
        int resC;
        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds(); // Taille zone utile

        resC = Math.min((int) (bounds.height * p) / monde.length,
                (int) (bounds.width * p) / monde[0].length);

        resC = Math.max(1, resC); // valeur plancher de 1

        return resC;
    }

    class PanneauGrille extends JPanel {
        private int res;
        private int[][] map;
        private BufferedImage worldImage;

        public PanneauGrille(int[][] map) {
            this.map = map;
            res = Affichage.calcRes(this.map);
            worldImage = new BufferedImage(res * this.map.length, res * this.map.length, BufferedImage.TYPE_INT_RGB);
            setPreferredSize(new Dimension(res * this.map.length, res * this.map.length));
        }

        private void dessineMonde(Graphics g) {
            int nbL = map.length;
            int nbC = map.length;

            g.setColor(Color.WHITE); // Couleur de fond
            g.fillRect(0, 0, res * nbC, res * nbL);

            // Cases
            g.setColor(Color.BLACK);
            for (int i = 0; i < nbL; i++) {
                for (int j = 0; j < nbC; j++) {
                    switch (map[i][j]) {
                        case 0:
                            g.setColor(Color.WHITE);
                            break;
                        case 1:
                            g.setColor(Color.BLACK);
                            break;
                        case 2:
                            g.setColor(Color.BLUE);
                            break;
                        default:
                            g.setColor(Color.WHITE);
                    }
                    g.fillRect(res * j, res * i, res, res);
                }
            }
        }

        public void paint(Graphics g) {
            Graphics gw = worldImage.getGraphics();
            dessineMonde(gw);
            g.drawImage(worldImage, 0, 0, null);
        }
    }
}
