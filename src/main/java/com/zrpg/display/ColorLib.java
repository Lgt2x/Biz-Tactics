package com.zrpg.display;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ColorLib {
	private static Map<String, Color> color = new HashMap<>(); // Couleurs associées à un nom pour un usage facile
	private static ArrayList<Color> bgColor = new ArrayList<>(); // Couleurs de mise en évidence des cases du plateau de jeu

	public ColorLib() {
		color.put("White", new Color(255, 255, 255, 255));  // Blanc
		color.put("Light blue", new Color(172, 191, 255, 255));  // Bleu clair
		color.put("Red", new Color(173, 14, 43, 255));    //Rouge
		color.put("Darker Blue", new Color(82, 155, 176, 255));
		color.put("Darkest Blue", new Color(77, 100, 197, 255));

		bgColor.add(new Color(255, 255, 255, 80));  // 0: Blanc
		bgColor.add(new Color(255, 204, 0, 80));    // 1: jaune de sélection de personnage
		bgColor.add(new Color(153, 204, 255, 80));  // 2: bleu de possibilité de déplacement
		bgColor.add(new Color(0, 102, 255, 80));    // 3: bleu, déplacement possible au survol de la souris
		bgColor.add(new Color(204, 51, 0, 80));     // 4: rouge, attaque possible d'un ennemi
	}

	public Color getColor(String colorName) {
		return color.get(colorName);
	}
	public Color getBgColor(int number) {
		return bgColor.get(number);
	}
}