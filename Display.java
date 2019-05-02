import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Display extends JFrame {
    private static GameManager gm;

    private static Map mapPanel;
    private static JSplitPane splitPane;
    private static JPanel bottomPanel;
    private static JLabel message;
    private final int messageHeight = 100;
    public int res;

    public Display(GameManager gm) {
        super("T-RPG"); // Nom de la fenêtre

        this.gm = gm;

        res = calcRes(gm.map); // Résolution des cases en px en fonction de la taille de l'écran
        mapPanel = new Map(this, gm); // Grille d'Display, sous forme de classe
        splitPane = new JSplitPane(); // Conteneur global qui comprend la gm.map et le message
        bottomPanel = new JPanel(); // Panel du bas qui contient le texte
        message = new JLabel("Some text"); // Message en bas


        setPreferredSize(new Dimension(res * gm.map.length,
                                       res * gm.map[0].length+messageHeight)); // Réglage de la taille de la fenêtre

        getContentPane().setLayout(new GridLayout()); // Layout en forme de grille qui va recevoir les panels
        getContentPane().add(splitPane);

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(200);
        splitPane.setTopComponent(mapPanel);
        splitPane.setBottomComponent(bottomPanel);
        splitPane.setDividerSize(5); // Taille en px du séparateur
        splitPane.setDividerLocation(res*gm.map[0].length); // Emplacement du séparateur depuis le haut
        splitPane.setEnabled(false); // Le séparateur ne peut plus bouger

        bottomPanel.setMaximumSize(new Dimension(res*gm.map.length, 100));
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

}

// https://stackoverflow.com/questions/15694107/how-to-layout-multiple-panels-on-a-jframe-java
