package com.zrpg.display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class ImgLib {
    private static Map<String, BufferedImage> images = new HashMap<>();
    private static BufferedImage[] bgImages;

    public ImgLib() {
        try {
            bgImages = new BufferedImage[]{
                    ImageIO.read(getClass().getClassLoader().getResourceAsStream("Sprites/Background/grass.png")),
                    ImageIO.read(getClass().getClassLoader().getResourceAsStream("Sprites/Background/rock.png")),
                    ImageIO.read(getClass().getClassLoader().getResourceAsStream("Sprites/Background/rock2.png")),

            };

            //images.put("Attack", ImageIO.read()

        } catch (IOException e) {
            System.out.println("Erreur de chargement des images");
        }

    }

    public BufferedImage getBgImage(int number) {
        return bgImages[number];
    }

    public BufferedImage getImage(String imgName) {
        return images.get(imgName);
    }
}
