package com.zrpg.display;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImgLib {
	private static BufferedImage[] bgImages;

	public ImgLib() {
		try {
			bgImages = new BufferedImage[]{
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("Background/grass.png")),
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("Background/grass2.png")),
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("Background/rock.png")),
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("Background/rock2.png")),
					ImageIO.read(getClass().getClassLoader().getResourceAsStream("Background/rock3.png")),
			};


		} catch (IOException e) {
			System.out.println("Erreur de chargement des images de fond");
		}
	}

	/**
	 * Charge une image depuis le dossier de ressources
	 * @param filename chemin vers l'image depuis le dossier ressources
	 * @return l'objet associé à l'image chargée
	 */
	public BufferedImage loadImage(String filename) {
		try {
			BufferedImage img = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filename)); // Lecture de l'image spécifiée
			return img;
		} catch (Exception e) {
			System.out.println("Image " + filename + " non trouvée");
			return null;
		}
	}

	public BufferedImage getBgImage(int number) { return bgImages[number]; }
}
