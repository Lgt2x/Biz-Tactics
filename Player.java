import java.util.ArrayList;

public class Player{

	private static String name;
	private static ArrayList<Character> characters; // A voir pour faire un ArrayList à la place

	public Player(String name) {
		characters = new ArrayList<Character>();
	}

	public static void addChar (String name, String type, int posX, int posY, int hp, int def, int attack, int range, int speed, int mp, int precision) {
		characters.add(new Character(name, type, posX, posY, hp,  def,  attack,  range,  speed,  mp,  precision));
	}
}
