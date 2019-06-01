package com.zrpg;

import com.google.gson.Gson;
import com.zrpg.characters.PblCharacter;
import com.zrpg.characters.Player;
import com.zrpg.display.Display;
import com.zrpg.jsonloaders.BackgroundLoader;

import java.io.InputStream;
import java.io.InputStreamReader;

public class GameManager {
	public String mode = "title";
	public int[][] background; // Tableau 2D qui représente la map de jeu, chaque nombre correspond à un élément distinct du fond
	private boolean[][] backgroundObstacles; // Calculé à partir de background, donne la possibilité ou non de se déplacer sur telle case
	public int[][] overlay; // Tableau 2D qui représente les cases de l'overlay sur la map (possibilité de déplacement, d'attaque etc.
	private int selectedMap = 0; // Map sélectionnée, le numéro correspond à l'ordre dans le fichier JSON maps.json
	private Display aff; // Référence à l'objet d'affichage
	private final String[] CHAR_TYPES = new String[]{"Bard", "Demon", "Mercenary", "Priestess", "Queen", "Sage", "Summoner", "Witch"}; // Listes des types disponibles

	public int sizeX; // Nombre de colonnes de la map
	public int sizeY; // Nombre de lignes de la map

	private boolean gameOver = false; // Vrai lorsque la partie est finie
	public Player[] players; // Liste de joueurs
	private PblCharacter selectedChar; // Personnage joué ce tour, pour le déplacement et l'attaque
	private int turn;
	private int step;

    /*
        turn%2 == 0 => Joueur 1
        turn%2 == 1 => Joueur 2
    */


    /*
        step%3 == 0 => Sélection du personnage à jouer
        step%3 == 1 => Sélection de la case de déplacement
        step%3 == 2 => Sélection du personnage à attaquer
    */


	public GameManager() {

		turn = (int)(Math.random()*2);
		System.out.println(turn);
		step = 0;

		BackgroundLoader backgroundLoaded = loadMap(selectedMap);

		sizeX = backgroundLoaded.sizeX;
		sizeY = backgroundLoaded.sizeY;

		background = new int[sizeY][sizeX];
		overlay = new int[sizeY][sizeX];
		backgroundObstacles = new boolean[sizeY][sizeX];
		
		fillBg(backgroundLoaded);

		players = new Player[]{
				new Player("Joueur 1", false),
				new Player("Joueur 2", true)
		};

		addChars(backgroundLoaded);

		aff = new Display(this); // Instanciation de la classe d'affichage
	}

	/**
	 * Méthode déclenchée par MapDisplay au clic sur la map
	 * @param x abscisse de la case cliquée
	 * @param y ordonnée de la case cliquée
	 */
	public void clickHandle(int x, int y) {
		if (!gameOver) {
			switch (step % 3) {
				case 0:
					charSelect(x, y);
					break;
				case 1:
					moveSelect(x, y);
					break;
				case 2:
					attackSelect(x, y);
					break;
				default:
					System.out.println("Erreur");

			}
		} else { // Si la partie est terminée et qu'un nouveau clic est reçu, on relance le jeu
			aff.exitApp(); // Fermeture de la fenêtre
			new GameManager(); // Création d'un nouveau jeu
		}
	}

	public void clickTitleScreen(String buttonValue) {
		if (buttonValue.equals("play")) {
			aff.initGame();
			this.mode = "game";
			aff.mapPanel.repaint();
		}
	}

	/**
	 * Méthode délenchée lors du clic sur une case lors de la sélection du personnage (étape 0)
	 * @param x abscisse
	 * @param y ordonnée
	 */
	private void charSelect(int x, int y) {
		if (overlay[y][x] == 1) { // Si la case cliquée est effectivement un personnage
			selectedChar = findChar(x, y, players[turn % 2]); // Recherche le personnage positionné sur la case (x,y)
			step = 1;

			setupMoveSelect(); // Setup de l'étape suivante
			aff.mapPanel.repaint();
		}
	}

	/**
	 * Mise en place de la sélection des personnages, mise à jour de l'overlay
	 */
	public void setupCharSelect() {
		cleanOverlay(); // Nettoyage de l'overlay de l'overlay

		PblCharacter character;

		for (int i = 0; i < players[turn % 2].characters.size(); i++) {
			character = players[turn % 2].characters.get(i);
			if (character.isAlive()) {
				overlay[character.getPosY()][character.getPosX()] = 1; // Le personnage peut être sélectionné, on le met en vert
			}
		}

		aff.changeMessage(players[turn % 2].getName() + " : quel personnage bouger?");
	}

	/**
	 * Déplacement du personnage quand la case sélectionnée est une case de déplacement possible
	 * @param x abscisse de la case
	 * @param y ordonnée de la case
	 */
	private void moveSelect(int x, int y) {
		if (overlay[y][x] == 2 || overlay[y][x] == 3) { // Si le déplacement est possible vers la case sélectionnée
			selectedChar.moveTo(x, y); // Déplacement du personnage
			aff.repaint(); // Update de l'affichage

			step = 2; // Passage à l'étape suivante
			setupAttackSelect(); // Setup de l'étape suivante
		}
	}

	/**
	 * Mise en place de la sélection du mouvement
	 */
	private void setupMoveSelect() {
		cleanOverlay();

		findPaths(selectedChar.getPosX(), selectedChar.getPosY(), selectedChar.getSpeed() + 1, 2); // Recherche des cases de déplacement possible
		overlay[selectedChar.getPosY()][selectedChar.getPosX()] = 2; // Le personnage peut aussi ne pas se déplacer

		aff.mapPanel.repaint();
		aff.changeMessage(players[turn % 2].getName() + " : où bouger le personnage?");
	}

	/**
	 * Vérification que l'attaque est possible sur la case donnée, action en conséquence
	 * @param x abscisse de la case
	 * @param y ordonnée de la case
	 */
	private void attackSelect(int x, int y) {
		if (overlay[y][x] == 4) {
			PblCharacter adversary = findChar(x, y, players[(turn + 1) % 2]); // Recherche du personnage ennemi placé sur la case sélectionné

			if (adversary != null)
				selectedChar.attack(adversary);

			selectedChar.changeSprite("idle");
			aff.mapPanel.repaint();

			if (players[(turn+1)%2].isDed()) {
				gameOver = true;
				cleanOverlay();
				aff.changeMessage("<html>" + players[turn%2].getName() + " gagne la partie!<br>Cliquez n'importe où pour recommencer</html>");
				return;
			}

			step = 0; // Retour à la première étape
			turn++; // Au tour adverse

			setupCharSelect();
		}
	}

	/**
	 * Mise en place de la sélection de l'attaque
	 */
	private void setupAttackSelect() {
		cleanOverlay();

		// Coloriage en rouge des cases où se trouvent des personnages adverses à portée d'attaque
		findPaths(selectedChar.getPosX(), selectedChar.getPosY(), selectedChar.getRange() + 1, 4);

		// Vérification qu'il y ait bien un personnage à attaquer
		boolean canAttack = false;
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++) {
				if (overlay[y][x] == 4) {
					canAttack = true;
					break;
				}
			}

			if (canAttack)
				break;
		}

		if (!canAttack) { // Si il n'y a personne à attaquer, passage à l'étape suivante
			step = 0;
			turn++;
			setupCharSelect();
			return;
		}

		aff.changeMessage(players[turn % 2].getName() + " : quel personnage attaquer?");
		selectedChar.changeSprite("attack");
		aff.mapPanel.repaint();
	}


	/**
	 * Cherche si un personnage existe sur cette case, le retourne le cas échéant
	 * @param x abscisse dans la map
	 * @param y ordonnée dans la map
	 * @param player joueur à qui appartient le personnage
	 * @return Personnage se trouvant sur la case, null sinon
	 */
	private PblCharacter findChar(int x, int y, Player player) {
		PblCharacter character;

		// Itération sur tous les personnage du joueur
		for (int i = 0; i < player.characters.size(); i++) {
			character = player.characters.get(i);
			if (character.getPosX() == x && character.getPosY() == y && character.isAlive())
				return character;
		}

		// Si aucun personnage n'est trouvé, on retourne null
		return null;
	}

	/**
	 * Fonction récursive de recherche des cases accessibles à partir d'un point donné pour une distance donnée
	 * @param x Abscisse du point considéré au départ
	 * @param y Ordonnée
	 * @param distanceLeft Distance restante = vitesse du personnage
	 * @param color couleur correspondant au type de chemin recherché: 2 = bleu pour le déplacement ou 4 = rouge pour l'attaque
	 */
	private void findPaths(int x, int y, int distanceLeft, int color) {
		if (distanceLeft > 0 && x >= 0 && x < sizeX && y >= 0 && y < sizeY) { // Si il reste de la distance à parcourir et que la case est bien sur le plateau
			if (color == 2) { // Recherche du déplacement
				// Si la case n'est pas un obstacle, et qu'il n'y a pas de personnage déjà dessus hors le personnage sélectionné
				if (!backgroundObstacles[y][x] && (findChar(x, y, players[turn%2]) == selectedChar || findChar(x, y, players[turn%2]) == null) && findChar(x, y, players[(turn+1)%2]) == null)
					overlay[y][x] = 2; // Bleu clair = déplacement possible
				else
					return;
			} else if (color == 4) { // Recherche d'attaque
				if (findChar(x, y, players[(turn+1)%2]) != null) // Si il y a un personnage adverse sur cette case
					overlay[y][x] = 4; // On colorie la case en rouge
				if (selectedChar.isMelee()) {
					if (! (!backgroundObstacles[y][x] && (findChar(x, y, players[turn%2]) == selectedChar
							|| findChar(x, y, players[turn%2]) == null) && findChar(x, y, players[(turn+1)%2]) == null))
						// Pas de récursion si la case est un obstacle
						return;
				}

			}

			// Récursion sur les cases autour, en diminuant la portée
			findPaths(x-1, y, distanceLeft - 1, color);
			findPaths(x+1, y, distanceLeft - 1, color);
			findPaths(x, y-1, distanceLeft - 1, color);
			findPaths(x, y+1, distanceLeft - 1, color);
		}
	}

	/**
	 * Nettoie l'overlay
	 */
	private void cleanOverlay() {
		for (int y = 0; y < sizeY; y++) {
			for (int x = 0; x < sizeX; x++)
				overlay[y][x] = 0;
		}
	}

	/**
	 * Charge le fond depuis un fichier json
	 * @param mapNb la map choisie pour le jeu
	 * @return retourne un BackgroundLoader contenant les infos serializées du json
	 */
	private BackgroundLoader loadMap(int mapNb) {
		try {
			// Récupération du fichier json sous forme de stream
			InputStream jsonStream = getClass().getClassLoader().getResourceAsStream("maps.json");

			// De-serialization du json
			Gson gson = new Gson();
			BackgroundLoader[] backgrounds = gson.fromJson(new InputStreamReader(jsonStream), BackgroundLoader[].class);

			// Remplissage du tableau background avec les infos du BackgroundLoader
			return backgrounds[mapNb];

		} catch (Exception e) {
			System.out.println("Erreur de chargement du décor");
			return null;
		}

	}

	/**
	 * Transfert des données du BackgroundLoader sous forme de tableau exploitable
	 * @param backgroundLoaded Fond chargé depuis une fichier JSON
	 */
	private void fillBg (BackgroundLoader backgroundLoaded) {
		for (int y = 0; y < backgroundLoaded.sizeY; y++) {
			for (int x = 0; x < backgroundLoaded.sizeX; x++) {
				backgroundObstacles[y][x] = !(backgroundLoaded.map.get(y).charAt(x) == '0'); // Vrai s'il y a un obstacle

				// Les sprites des éléments de décor sont choisis aléatoirement parmi ceux d'une même catégorie, i.e. déplacement possible ou impossible
				double rand = Math.random();
				if (!backgroundObstacles[y][x]) {
					if (rand < 0.90)
						background[y][x] = 0; // Herbe (90% de chance)
					else
						background[y][x] = 1; // Herbe avec fleur (10%)
				} else {
					if (rand < 0.45)
						background[y][x] = 2; // Rocher 1 (45%)
					else if (rand < 0.9)
						background[y][x] = 3; // Rocher 2 (45%)
					else
						background[y][x] = 4; // Petit rocher (10%)
				}
			}
		}
	}

	/**
	 * Ajoute les personnages associés à un joueur, tel qu'un type de personnage est attribué une seule fois au maximum
	 * @param backgroundLoaded référence à la map chargée pour un positionnement correct
	 */
	private void addChars(BackgroundLoader backgroundLoaded) {
		boolean[] pickedChars = new boolean[8];
		int type;
		for (int i=0;i<6;i++) {
			do {
				type = (int)(Math.random() * 8);
			} while (pickedChars[type]);
			pickedChars[type] = true;

			players[i < 3 ? 0 : 1].addChar(CHAR_TYPES[type], backgroundLoaded.charPos[i][0], backgroundLoaded.charPos[i][1]);
		}
	}
}
