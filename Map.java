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
    private Display aff;
    private GameManager gm;
    private BufferedImage worldImage;

    public Map(Display aff, GameManager gm) {
        this.aff = aff;
        this.gm = gm;

        worldImage = new BufferedImage(aff.res * gm.map.length, aff.res * gm.map.length, BufferedImage.TYPE_INT_RGB);
        setPreferredSize(new Dimension(aff.res * gm.map.length, aff.res * gm.map.length));

        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                int x = e.getX();
                int y = e.getY();

                aff.changeMessage((int)(x/aff.res) + " " + (int)(y/aff.res));
                gm.map[(int)(y/aff.res)][(int)(x/aff.res)] = 2;
                repaint();

                gm.clic();
            }
        });
    }

    private void dessineMonde(Graphics g) {
        int largeurMap = gm.map.length;
        int hauteurMap = gm.map[0].length;

        g.setColor(Color.WHITE); // Couleur de fond
        g.fillRect(0, 0, aff.res * hauteurMap, aff.res * largeurMap); // Remplissage

        /*

            COULEURS
            0 = BLANC
            1 = NOIR
            2 = BLEU
            Défaut: BLANC

        */

        for (int i = 0; i < largeurMap; i++) {
            for (int j = 0; j < hauteurMap; j++) {
                remplitCase(i, j, gm.map[i][j], g);
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
        g.fillRect(aff.res * colonne, aff.res * ligne, aff.res, aff.res);
    }

    public void paint(Graphics g) {
        Graphics gw = worldImage.getGraphics();
        dessineMonde(gw);
        g.drawImage(worldImage, 0, 0, null);
    }
}
