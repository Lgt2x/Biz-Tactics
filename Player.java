import java.util.ArrayList;
import java.util.Arrays;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Player{

	private String name;
	public ArrayList<Character> characters; // A voir pour faire un ArrayList à la place

	public Player(String name) {
		characters = new ArrayList<Character>();
	}

	public String toString () {
		return name;
	}

	public void addChar (String name, String type, int posX, int posY) {
		try {
            Gson gson = new Gson();
            CharLoader loader = gson.fromJson(new FileReader("Assets/charStats/" + type + ".json"), CharLoader.class);
			characters.add(new Character(
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
        } catch (FileNotFoundException e) {
            System.out.println("Erreur de chargement de la map");
        }
	}

	public boolean isDed () {
		for (int i = 0; i < characters.size(); i++) {
			if (characters.get(i).isAlive())
				return false;
		}

		return true;
	}
}
