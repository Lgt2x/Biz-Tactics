package com.zrpg.display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImgLib {
    private static BufferedImage[] bgImages;

    public ImgLib() {
        try {
            bgImages = new BufferedImage[]{
                    ImageIO.read(getClass().getClassLoader().getResourceAsStream("Background/grass.png")),
                    ImageIO.read(getClass().getClassLoader().getResourceAsStream("Background/rock.png")),
                    ImageIO.read(getClass().getClassLoader().getResourceAsStream("Background/rock2.png")),

            };


        } catch (IOException e) {
            System.out.println("Erreur de chargement des images de fond");
        }

    }

    public BufferedImage getBgImage(int number) {
        return bgImages[number];
    }

    public BufferedImage loadImage(String filename) {
        try {

            BufferedImage img = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filename));
            return img;
        } catch (Exception e) {
            System.out.println("Image " + filename + " non trouvée");
            return null;
        }
    }
}
