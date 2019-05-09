package com.zrpg.characters;

import java.util.ArrayList;
import java.io.*;

import com.google.gson.Gson;
import com.zrpg.jsonloaders.*;

public class Player{

	private String name;
	public ArrayList<PblCharacter> characters; // Tableau des personnages du joueur

	public Player(String name) {
		this.name = name;
		characters = new ArrayList<>();
	}

	public String toString () {
		return name;
	}

	public void addChar (String name, String type, int posX, int posY) {
		try {
			String filename = "Json/charStats/" + type + ".json";
			InputStream a = getClass().getClassLoader().getResourceAsStream(filename);
			Gson gson = new Gson(); // Deserializer Json

			// Transpose le json en objet contenant les stats d'un type de personnage jouable
            CharLoader loader = gson.fromJson(new InputStreamReader(a), CharLoader.class);

			characters.add(new PblCharacter(
				name,
				posX,
				posY,
				loader.type,
				loader.hp,
				loader.mp,
				loader.defense,
				loader.attack,
				loader.range,
				loader.precision,
				loader.speed
			));

        } catch (Exception e) {
            System.out.println("Erreur de chargement du personnage");
        }
	}

	public boolean isDed () {
		for (PblCharacter character : characters) {
			if (character.isAlive())
				return false;
		}

		return true;
	}

	public String getName() {
		return name;
	}

}
