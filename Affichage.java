import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class Affichage extends JFrame {
    private static JSplitPane splitPane;
    private static Map mapPanel;
    private static JPanel bottomPanel;
    private static JLabel message;
    private final int messageHeight = 100;
    public int res;

    public Affichage(int[][] map) {
        super("T-RPG"); // Nom de la fenêtre

        splitPane = new JSplitPane(); // Conteneur global qui comprend la map et le message
        mapPanel = new Map(map); // Grille d'affichage, sous forme de classe
        bottomPanel = new JPanel(); // Panel du bas qui contient le texte
        message = new JLabel("Some text"); // Message en bas

        res = calcRes(map); // Résolution des cases en px en fonction de la taille de l'écran
        setPreferredSize(new Dimension(res * map.length,
                         res * map[0].length+messageHeight)); // Réglage de la taille de la fenêtre

        getContentPane().setLayout(new GridLayout()); // Layout en forme de grille qui va recevoir les panels
        getContentPane().add(splitPane);

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(200);
        splitPane.setTopComponent(mapPanel);
        splitPane.setBottomComponent(bottomPanel);
        splitPane.setDividerSize(5); // Taille en px du séparateur
        splitPane.setDividerLocation(res*map[0].length); // Emplacement du séparateur depuis le haut
        splitPane.setEnabled(false); // Le séparateur ne peut plus bouger

        bottomPanel.setMaximumSize(new Dimension(res*map.length, 100));
        bottomPanel.add(message);

        pack(); // Arrange la fenêtre

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Sort du jeu lors du clic sur le bouton de fermeture
        setLocationRelativeTo(null); // Centre
        setResizable(false); // Le fenêtre ne peut pas être redimensionnée
        setVisible(true); // La fenêtre est visible


    }

    public static int calcRes(int[][] monde) {
        final double p = .8; // pourcentage de la zone utile a occuper
        int resC;
        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds(); // Taille zone utile

        resC = Math.min((int) (bounds.height * p) / monde.length,
        (int) (bounds.width * p) / monde[0].length);

        resC = Math.max(1, resC); // valeur plancher de 1

        return resC;
    }

    public static void changeMessage(String m) {
        message.setText(m);
    }

    public static void actualisePlateau(int[][] map) { // L'affichage est demandé
        mapPanel.map = map;
        mapPanel.repaint(); // Appel de la méthode paint de la map
    }

    class Map extends JPanel {
        private int res;
        private int[][] map;
        private BufferedImage worldImage;

        public Map(int[][] map) {
            this.map = map;
            res = Affichage.calcRes(this.map);
            worldImage = new BufferedImage(res * this.map.length, res * this.map.length, BufferedImage.TYPE_INT_RGB);
            setPreferredSize(new Dimension(res * this.map.length, res * this.map.length));
        }

        private void dessineMonde(Graphics g) {
            int largeurMap = map.length;
            int hauteurMap = map[0].length;

            g.setColor(Color.WHITE); // Couleur de fond
            g.fillRect(0, 0, res * hauteurMap, res * largeurMap); // Remplissage

            /*
                COULEURS
                0 = BLANC
                1 = NOIR
                2 = BLEU
                Défaut: BLANC
            */

            for (int i = 0; i < largeurMap; i++) {
                for (int j = 0; j < hauteurMap; j++) {
                    remplitCase(i, j, map[i][j], g);
                }
            }
        }

        private void remplitCase(int ligne, int colonne, int couleur, Graphics g) {
            switch (couleur) {
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
            g.fillRect(res * colonne, res * ligne, res, res);
        }

        public void paint(Graphics g) {
            Graphics gw = worldImage.getGraphics();
            dessineMonde(gw);
            g.drawImage(worldImage, 0, 0, null);
        }
    }
}

// https://stackoverflow.com/questions/15694107/how-to-layout-multiple-panels-on-a-jframe-java
