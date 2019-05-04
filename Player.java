import java.util.ArrayList;

public class Player{

	private String name;
	public ArrayList<Character> characters; // A voir pour faire un ArrayList à la place

	public Player(String name) {
		characters = new ArrayList<Character>();
	}

	public String toString () {
		return name;
	}

	public void addChar (String name, String type, int posX, int posY, int hp, int def, int attack, int range, int speed, int mp, int precision) {
		characters.add(new Character(name, type, posX, posY, hp,  def,  attack,  range,  speed,  mp,  precision));
	}

	public boolean isDed () {
		for (int i = 0; i < characters.size(); i++) {
			if (characters.get(i).isAlive())
				return false;
		}

		return true;
	}
}
