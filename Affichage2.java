import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class Affichage2 extends JFrame {
    private static JSplitPane splitPane;
    private static JPanel mapPanel;
    private static JPanel bottomPanel;
    private static JLabel message;

    public Affichage2() {
        splitPane = new JSplitPane();
        mapPanel = new JPanel();
        bottomPanel = new JPanel();
        message = new JLabel("Some text");

        setPreferredSize(new Dimension(400, 400));
        getContentPane().setLayout(new GridLayout());
        getContentPane().add(splitPane);

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(200);
        splitPane.setTopComponent(mapPanel);
        splitPane.setBottomComponent(bottomPanel);
        splitPane.setEnabled(false);

        bottomPanel.add(message);

        pack();

        super("T-RPG");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Sort du jeu lors du clic sur le bouton de fermeture
        setLocationRelativeTo(null); // Centre
        setResizable(false); // Le fenêtre ne peut pas être redimensionnée
        setVisible(true); // La fenêtre est visible


    }

    private static int calcRes(int[][] monde) {
        final double p = .9; // pourcentage de la zone utile a occuper
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
