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
    private static int opacity = 60;
    private static Color[] colors = new Color[] {
        new Color(255, 255, 255, opacity),  // 0: blanc
        new Color (255, 204, 0, opacity),     // 1: jaune de sélection de personnage
        new Color (153, 204, 255, opacity), // 2: bleu de possibilité de déplacement
        new Color (0, 102, 255, opacity),   // 3: bleu, déplacement possible au survol de la souris
        new Color (204, 51, 0, opacity)     // 4: rouge, attaque possible d'un ennemi
    };

    private static int caseHoveredX = 0;
    private static int caseHoveredY = 0;

    private static BufferedImage[] backgroundPics;

    public Map(Display aff, GameManager gm) {
        this.aff = aff;
        this.gm = gm;

        worldImage = new BufferedImage(aff.res * gm.mapX, aff.res * gm.mapY, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(aff.res * gm.mapX, aff.res * gm.mapY));

        try {
            backgroundPics = new BufferedImage[] {
                ImageIO.read(new File("Assets/Background/grass.png")),
                ImageIO.read(new File("Assets/Background/rock.png"))
            };
        } catch (IOException e) {
            System.out.println("Erreur de chargement de l'image");
        }

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

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int x = e.getX(); // Récupération des coordonnées du clic
                int y = e.getY();

                int caseX = (int)(x/aff.res);
                int caseY = (int)(y/aff.res);

                if (gm.overlay[caseHoveredY][caseHoveredX] == 3)
                    gm.overlay[caseHoveredY][caseHoveredX] = 2;

                caseHoveredX = caseX;
                caseHoveredY = caseY;

                if (gm.overlay[caseY][caseX] == 2)
                    gm.overlay[caseY][caseX] = 3;

                repaint();
            }
        });;
    }

    private void drawOverlay(Graphics g) {
        /*

            COULEURS
            0 = BLANC
            1 = NOIR
            2 = BLEU
            Défaut: BLANC

        */

        for (int y = 0; y < gm.mapY; y++) {
            for (int x = 0; x < gm.mapX; x++) {
                fillTile(x, y, gm.overlay[y][x], g);
            }
        }
    }

    private void fillTile(int x, int y, int couleur, Graphics g) {
        if (y >= 0 && y < gm.mapY && x >= 0 && x < gm.mapX && couleur != 0) {
            g.setColor(colors[couleur]);
            g.fillRect(aff.res * x, aff.res * y, aff.res, aff.res);
        }
    }

    private void drawBackground(Graphics g) {
        for (int y = 0; y < gm.mapY; y++) {
            for (int x = 0; x < gm.mapX; x++) {
                g.drawImage(backgroundPics[gm.background[y][x]], x * aff.res, y * aff.res, aff.res, aff.res, null);
            }
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

        drawBackground(gw);
        drawOverlay(gw);

        drawChars(gw, gm.players[0]);
        drawChars(gw, gm.players[1]);

        g.drawImage(worldImage, 0, 0, null); // Affichage de l'image créée sur le Panel
    }
}
