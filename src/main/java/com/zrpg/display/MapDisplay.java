package com.zrpg.display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import com.zrpg.GameManager;
import com.zrpg.characters.*;

public class MapDisplay extends JPanel {

	private Display aff; // Référence à l'objet JFrame d'affichage
	private GameManager gm; // Référence à la classe principale

	private BufferedImage worldImage; // Espace de dessin
	private ImgLib imgLib;
	private ColorLib colorLib; // Couleurs prédéfinies

	private int caseHoveredX = 0; // X de la case survolée
	private int caseHoveredY = 0; // Y de la case survolée

	private int caseClickedX = 0; // X de la case où le clic a été fait
	private int caseClickedY = 0;


	public MapDisplay(Display aff, GameManager gm) {
		this.aff = aff;
		this.gm = gm;
		this.colorLib = new ColorLib();
		this.imgLib = new ImgLib();

		worldImage = new BufferedImage(aff.res * gm.sizeX, aff.res * gm.sizeY, BufferedImage.TYPE_INT_RGB);
		setPreferredSize(new Dimension(aff.res * gm.sizeX, aff.res * gm.sizeY));


		// Détection du clic
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) { // Le bouton de la souris est pressé
				int x = e.getX(); // Récupération des coordonnées du clic
				int y = e.getY();

				caseClickedX = x / aff.res; // Calcul de la case cliquée
				caseClickedY = y / aff.res;
			}
		});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) { // Le bouton de la souris est relâché
				if (gm.mode == "game") {
					int x = e.getX();
					int y = e.getY();

					int caseX = x / aff.res;
					int caseY = y / aff.res;

					if (caseX == caseClickedX && caseY == caseClickedY) { // Si la souris est restée sur la même case depuis que le clic est enfoncé
						gm.clickHandle(caseX, caseY); // Appel d'une fonction de la classe maîtresse pour savoir si ce clic a des conséquences sur le jeu
						repaint(); // Recalcul des éléments du canvas mis à jour
					}
				} else if (gm.mode == "title"){
					gm.clickTitleScreen("play");
				}
			}
		});

		// Détection du mouvement de souris
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX(); // Récupération des coordonnées du clic
				int y = e.getY();


				int caseX = x / aff.res;
				int caseY = y / aff.res;

				if (gm.overlay[caseHoveredY][caseHoveredX] == 3)
					gm.overlay[caseHoveredY][caseHoveredX] = 2;

				caseHoveredX = caseX;
				caseHoveredY = caseY;

				if (gm.overlay[caseY][caseX] == 2)
					gm.overlay[caseY][caseX] = 3;

				repaint();

			}
		});
	}

	/**
	 * Dessine le fond de la map de jeu
	 * @param g
	 */
	private void drawBackground(Graphics g) {
		for (int y = 0; y < gm.sizeY; y++) {
			for (int x = 0; x < gm.sizeX; x++) {
				g.drawImage(imgLib.getBgImage(gm.background[y][x]), x * aff.res, y * aff.res, aff.res, aff.res, null);
			}
		}
	}

	/**
	 * Dessine l'overlay au dessus du fond
	 * @param g Image graphique
	 */
	private void drawOverlay(Graphics g) {
		for (int y = 0; y < gm.sizeY; y++) {
			for (int x = 0; x < gm.sizeX; x++) {
				fillTile(x, y, gm.overlay[y][x], g);
			}
		}
	}

	/**
	 * Remplit une case avec une couleur donnée
	 * @param x
	 * @param y
	 * @param color
	 * @param g
	 */
	private void fillTile(int x, int y, int color, Graphics g) {
		if (y >= 0 && y < gm.sizeY && x >= 0 && x < gm.sizeX && color != 0) {
			g.setColor(colorLib.getBgColor(color)); // Choix de la couleur de peinture
			g.fillRect(aff.res * x, aff.res * y, aff.res, aff.res);
		}
	}

	/**
	 * Dessine les personnages d'un joueur et leurs barres de vie
	 * @param g
	 * @param player
	 */
	private void drawChars(Graphics g, Player player) {
		int imgWidth;
		int imgHeight;

		for (int i = 0; i < player.characters.size(); i++) {
			PblCharacter character = player.characters.get(i); // Récupération du personnage

			BufferedImage sprite = character.getCurrentSprite();

			// Positionnement et affichage du personnage
			if (character.isAlive()) {
				if (sprite.getWidth() >= sprite.getHeight()) {
					imgWidth = aff.res;
					imgHeight = (sprite.getHeight() * aff.res) / sprite.getWidth();
				} else {
					imgHeight = aff.res;
					imgWidth = (sprite.getWidth() * aff.res) / sprite.getHeight();
				}

				// Retournement horizontal du sprite si il vient de se déplacer vers la gauche
				if (character.isFacingLeft()) {
					AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
					tx.translate(-sprite.getWidth(null), 0);
					AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
					sprite = op.filter(sprite, null);
				}

				// Dessin du personnage
				g.drawImage(sprite, character.getPosX() * aff.res + (aff.res - imgWidth) / 2,
						character.getPosY() * aff.res + (aff.res - imgHeight) / 2,
						imgWidth, imgHeight, null);

				// Dessin du fond bleu de la barre de vie
				g.setColor(colorLib.getColor("Light blue"));
				g.fillRoundRect(aff.res * character.getPosX(),      // x
						(int) (aff.res * (character.getPosY() - 0.2)), // y

						aff.res,                                       // Largeur
						(int) (aff.res * 0.2),                         // Hauteur

						(int) (aff.res * 0.08),                        // Arrondi x
						(int) (aff.res * 0.08));                       // Arrondi y

				// Dessin du rouge de la barre de vie
				g.setColor(colorLib.getColor("Red"));
				g.fillRoundRect((int) (aff.res * (character.getPosX() + 0.05)),
						(int) (aff.res * (character.getPosY() - 0.15)),

						(int)(aff.res * 0.9 * character.getHp()/character.getHpMax()),
						(int) (aff.res * 0.12),

						(int) (aff.res * 0.08),
						(int) (aff.res * 0.08));
			}
		}
	}

	/**
	 * Dessin de l'écran de titre
	 * @param g
	 */
	private void drawTitleScreen(Graphics g) {
		g.setColor(Color.WHITE);

		int windowSizeX = aff.res * gm.sizeX;
		int windowSizeY = aff.res * gm.sizeX;

		g.fillRect(0,0, windowSizeX, windowSizeY);
		g.drawImage(imgLib.loadImage("Imgs/Logo.png"), (int) (windowSizeX * 0.05), (int) (windowSizeY * 0.1), (int) (windowSizeX * 0.9), (int) (windowSizeX * 0.9 * 9/16), null);
	}

	/**
	 * Re-dessin de l'écran, selon la phase de jeu
	 * @param g
	 */
	public void paint(Graphics g) {
		Graphics gw = worldImage.getGraphics(); // Espace de dessin

		if (gm.mode == "game") {
			drawBackground(gw);
			drawOverlay(gw);

			drawChars(gw, gm.players[0]);
			drawChars(gw, gm.players[1]);

		} else if (gm.mode == "title") {
			drawTitleScreen(gw);
		}

		g.drawImage(worldImage, 0, 0, null); // Affichage de l'image créée sur le Panel
	}
}
