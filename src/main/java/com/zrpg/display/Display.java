package com.zrpg.display;

import javax.swing.*;
import java.awt.*;


import com.zrpg.GameManager;

public class Display extends JFrame {

    public MapDisplay mapPanel; // Dessin de la map
    private  JSplitPane splitPane; // Conteneur de tous les panels
    private  JPanel bottomPanel; // Panel contenant le message
    private  JLabel message; // Message affiché en bas de l'écran
    private final int messageHeight = 100; // Hauteur du Label
    public int res; // Taille d'un côté d'une case de la map, calculée à partir de la résolution de l'écran

    public Display(GameManager gm) {
        super("T-RPG"); // Nom de la fenêtre

        res = calcRes(gm.overlay); // Résolution des cases en px en fonction de la taille de l'écran
        mapPanel = new MapDisplay(this, gm); // Grille d'Display, sous forme de classe
        splitPane = new JSplitPane(); // Conteneur global qui comprend la gm.map et le message
        bottomPanel = new JPanel(); // Panel du bas qui contient le texte
        message = new JLabel("Some text"); // Message en bas


        setPreferredSize(new Dimension(res * gm.mapX,
                                       res * gm.mapY+messageHeight)); // Réglage de la taille de la fenêtre

        getContentPane().setLayout(new GridLayout()); /* Layout en forme de grille qui va recevoir les panels
                                                        (ici 1 colonne, 2 lignes) */
        getContentPane().add(splitPane); // Ajout du panel diviseur contenant les autres dans la fenêtre

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(mapPanel); // Composant du haut: la map
        splitPane.setBottomComponent(bottomPanel); // Composant du bas: le message
        splitPane.setDividerSize(5); // Taille en px du séparateur
        splitPane.setDividerLocation(res*gm.mapY); // Emplacement du séparateur depuis le haut
        splitPane.setEnabled(false); // Le séparateur ne peut plus bouger

        bottomPanel.setMaximumSize(new Dimension(res*gm.mapY, 100)); // Taille max du Label
        bottomPanel.add(message);

        pack(); // Arrange la fenêtre

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Sort du jeu lors du clic sur le bouton de fermeture
        setLocationRelativeTo(null); // Centre
        setResizable(false); // Le fenêtre ne peut pas être redimensionnée
        setVisible(true); // La fenêtre est visible


    }

    public  int calcRes(int[][] monde) {
        final double p = .8; // % de la zone utile a occuper
        int resC;
        Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds(); // Espace dispo de l'écran

        resC = Math.max(
            Math.min((int) (bounds.height * p) / monde.length,
                     (int) (bounds.width * p) / monde[0].length),
            1); // Valeur plancher de 1

        return resC;
    }

    public  void changeMessage(String m) {
        message.setText(m);
    }

}

// https://stackoverflow.com/questions/15694107/how-to-layout-multiple-panels-on-a-jframe-java
