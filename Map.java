import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Map extends JPanel {

    private static Display aff; // Référence à l'objet JFrame d'affichage
    private static GameManager gm; // Référence à la classe principale
    private BufferedImage worldImage; // Espace de dessin

    // Définition des constantes de couleur
    private static int opacity = 80;
    private static Color[] colors = new Color[] {
        new Color(255, 255, 255, opacity),  // 0: blanc
        new Color (0, 204, 0, opacity),     // 1: vert de sélection de personnage
        new Color (153, 204, 255, opacity), // 2: bleu de possibilité de déplacement
        new Color (0, 102, 255, opacity),   // 3: bleu, déplacement possible au survol de la souris
        new Color (204, 51, 0, opacity)     // 4: rouge, attaque possible d'un ennemi
    };

    public Map(Display aff, GameManager gm) {
        this.aff = aff;
        this.gm = gm;

        worldImage = new BufferedImage(aff.res * gm.mapX, aff.res * gm.mapY, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(aff.res * gm.mapX, aff.res * gm.mapY));



        // Ajout d'un récepteur d'évenement
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){ // Au clic
                int x = e.getX(); // Récupération des coordonnées du clic
                int y = e.getY();

                int caseX = (int)(x/aff.res);
                int caseY = (int)(y/aff.res);

                gm.clickHandle(caseX, caseY); // Appel d'une fonction de la classe maîtresse pour savoir si ce clic a des conséquences sur le jeu

                aff.changeMessage(caseX + " " + caseY); // On change le message pour afficher la case
                //gm.overlay[(int)(y/aff.res)][(int)(x/aff.res)] = 2;

                repaint(); // Recalcul des éléments du canvas mis à jour
            }
        });
    }

    private void drawTiles(Graphics g) {

        g.setColor(Color.WHITE); // Couleur de fond
        g.fillRect(0, 0, aff.res * gm.mapX, aff.res * gm.mapY); // Remplissage

        /*

            COULEURS
            0 = BLANC
            1 = NOIR
            2 = BLEU
            Défaut: BLANC

        */

        for (int i = 0; i < gm.mapY; i++) {
            for (int j = 0; j < gm.mapX; j++) {
                fillTile(i, j, gm.overlay[i][j], g);
            }
        }
    }

    private void fillTile(int y, int x, int couleur, Graphics g) {
        if (y >= 0 && y < gm.mapY && x >= 0 && x < gm.mapX) {
            g.setColor(colors[couleur]);
            g.fillRect(aff.res * x, aff.res * y, aff.res, aff.res);
        }
    }

    private void drawChars(Graphics g, Player player) {
        int imgWidth;
        int imgHeight;

        for (int i = 0; i < player.characters.size(); i++) {
            Character character = player.characters.get(i); // Récupération du personnage
            // Positionnement et affichage du personnage
            if (character.isAlive()) {
                if (character.idle.getWidth() >= character.idle.getHeight()) {
                    imgWidth = aff.res;
                    imgHeight = (character.idle.getHeight() * aff.res) / character.idle.getWidth();
                } else {
                    imgHeight = aff.res;
                    imgWidth = (character.idle.getWidth() * aff.res) / character.idle.getHeight();
                }

                g.drawImage(character.idle, character.getPosX() * aff.res + (aff.res - imgWidth)/2,
                                            character.getPosY() * aff.res + (aff.res - imgHeight)/2,
                            imgWidth, imgHeight, null);
            }
        }
    }

    public void paint(Graphics g) {
        Graphics gw = worldImage.getGraphics(); // Espace de dessin

        drawTiles(gw);
        drawChars(gw, gm.players[0]);
        drawChars(gw, gm.players[1]);

        g.drawImage(worldImage, 0, 0, null); // Affichage de l'image créée sur le Panel
    }
}
