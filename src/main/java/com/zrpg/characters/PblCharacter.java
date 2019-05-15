package com.zrpg.characters;

import java.awt.image.BufferedImage;
import com.zrpg.display.ImgLib;

public class PblCharacter {

	private String name; // Nom du personnage
	private boolean alive; // Est-ce que le personnage est en vie
	private String type; // Classe du personnage

	private int posX; // colonne sur la map
	private int posY; // ligne

	private int hp; // Points de vie restants
	private int hpMax; // Points de vie totaux
	private int mp; // Points de magie restants
	private int mpMax; // Points de vie totaux
	private int defense; // Points de défense/bouclier
	private int attack; // Points de dégats moyen

	private int range; // Portée d'attaque en nb de cases
	private int speed; // Portée de déplacement
	private int precision; // Variation des dégats d'attaque, l'attaque va de attack - precision à attack + precision

	public BufferedImage idle;

	public PblCharacter(String name, int posX, int posY, String type, int hp, int mp, int defense, int attack, int range, int precision, int speed){
		this.name = name;
		this.type = type;
		this.alive = true;

		this.posX = posX;
		this.posY = posY;

		this.hp = hp;
		this.hpMax = hp;
		this.mp = mp;
		this.mpMax = mp;
		this.defense = defense;

		this.attack = attack;
		this.range = range;
		this.speed = speed;
		this.precision = precision;

		ImgLib lib = new ImgLib();
		this.idle = lib.loadImage("Chars/" + this.type + "/idle.png");

	}

	public String toString(){
		return this.name + " a encore " + this.hp + "hp";
	}

	/**
	 * Affaiblit le personnage d'un nombre de points de vie donné
	 * @param damage Points de vie enlevés
	 */
	private void weaken(int damage){
		this.hp -= damage;

		if (this.hp <= 0) {
			this.alive = false;
		}
	}

	/**
	 * Calcule les dégâts et attaque un adversaire avec l'attaque de base
	 * @param adversary Ennemi attaqué
	 */
	public void attack(PblCharacter adversary){
		int damage = this.attack;

		/*
		Ta formule empêche la compilation, à revoir...

		int damage = (this.attack*4 - adversary.defense*2) * (1 - (100-this.precision)/100 * (0.5 + (int)(Math.random())));
		*/

		/*
		Formule de base, à améliorer en regardant ce lien: http://www.rpgmakervx-fr.com/t21422-formules-de-degats
		*/

		adversary.weaken(damage);
	}

	/**
	 * Déplace le personnage vers la case indiquée
	 * @param x Colonne d'arrivée
	 * @param y Ligne d'arrivée
	 */
	public void moveTo(int x, int y) {
		this.posX = x;
		this.posY = y;
	}

	public boolean isAlive () {
		return this.alive;
	}
	public int getPosX() 	{ return this.posX; }
	public int getPosY() 	{ return this.posY; }
	public int getHp() 		{ return this.hp; }
	public int getHpMax() 	{ return this.hpMax; }
	public int getRange() 	{ return this.range; }
	public int getSpeed()	{ return this.speed; }
}
