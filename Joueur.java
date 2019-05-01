import java.util.ArrayList;

public class Joueur{

	private static String name;
	private static ArrayList<Personnage> characters; // A voir pour faire un ArrayList à la place

	public Joueur (String name) {
		characters = new ArrayList<Personnage>();
	}

	public static void addPerso (String name, String type, int posX, int posY, int hp, int def, int attack, int range, int speed, int mp, int precision) {
		characters.add(new Personnage(name, type, posX, posY, hp,  def,  attack,  range,  speed,  mp,  precision));
	}
}
