import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Character{

	public String name; // Nom du personnage
	private boolean alive; // Est-ce que le personnage est en vie
	public String type; // Classe du personnage

	private int posX; // colonne sur la map
	private int posY; // ligne

	public int hp; // Points de vie restants
	public int hpMax; // Points de vie totaux
	public int mp; // Points de magie restants
	public int mpMax; // Points de vie totaux
	public int def; // Points de défense/bouclier
	public int attack; // Points de dégats moyen

	public int range; // Portée d'attaque en nb de cases
	public int speed; // Portée de déplacement
	public int precision; // Variation des dégats d'attaque, l'attaque va de attack - precision à attack + precision

	public BufferedImage idle;

	public Character(String name, String type, int posX, int posY, int hp, int def, int attack, int range, int speed, int mp, int precision){
		this.name = name;
		this.type = type;
		this.alive = true;

		this.posX = posX;
		this.posY = posY;

		this.hp = hp;
		this.hpMax = hp;
		this.mp = mp;
		this.mpMax = mp;
		this.def = def;

		this.attack = attack;
		this.range = range;
		this.speed = speed;
		this.precision = precision;

		try {
			idle = ImageIO.read(new File("Assets/Berserker/Berserker.png"));
		} catch (IOException e) {}
	}

	public String toString(){
		return this.name + " a encore " + this.hp + "hp";
	}

	public void weaken(int damage){
		this.hp -= damage;
	}

	public void attack(Character adversary){
		int damage = (this.attack*4 - adversary.def*2) * (1 - (100-this.precision)/100 * (0.5 + (int)(Math.random())));
		/*
		Formule de base, à améliorer en regardant ce lien: http://www.rpgmakervx-fr.com/t21422-formules-de-degats
		*/
		adversary.weaken(damage);
	}

	/**** GETTERS & SETTERS ****/
	public int getPosX() {
		return this.posX;
	}

	public int getPosY() {
		return this.posY;
	}
	/***************************/
}
