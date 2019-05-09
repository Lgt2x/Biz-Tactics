package com.zrpg;

import com.google.gson.Gson;
import com.zrpg.characters.PblCharacter;
import com.zrpg.characters.Player;
import com.zrpg.display.Display;
import com.zrpg.jsonloaders.BackgroundLoader;

import java.io.InputStream;
import java.io.InputStreamReader;

public class GameManager {
    public int[][] background; // Fond, avec ses obstacles
    public int[][] overlay; // Tableau 2D qui représente les cases de l'overlay sur la map
    private int selectedMap = 0;
    private Display aff; // Référence à l'objet d'affichage

    public int mapX; // Nombre de colonnes de la map
    public int mapY; // Nombre de lignes de la map


    private boolean gameOver = false; // Vrai lorsque la partie est finie
    public Player[] players; // Liste de joueurs
    private PblCharacter selectedChar; // Personnage joué ce tour, pour le déplacement et l'attaque
    private int tour;
    private int step;

    /*
        tour%2 = 0 => Joueur 1
        tour%2 = 1 => Joueur 2
    */


    /*
        step%3 = 0 => Sélection du personnage à jouer
        step%3 = 1 => Sélection de la case de déplacement
        step%3 = 2 => Sélection du personnage à attaquer
    */


    public GameManager() {

        tour = 0;
        step = 0;

        players = new Player[]{
                new Player("Player 1"),
                new Player("Player 2")
        };

        /*Scanner sc = new Scanner (System.in);

        System.out.println("Quels noms?");
        players[0].name = sc.nextLine();
        players[1].name = sc.nextLine();*/

        players[0].addChar("Perso1", "Berserker", 11, 1);
        players[0].addChar("Perso1.2", "Sniper", 10, 5);
        players[1].addChar("Perso2", "Knight", 13, 1);


        BackgroundLoader backgroundLoaded = loadMap(selectedMap);

        mapX = backgroundLoaded.sizeX;
        mapY = backgroundLoaded.sizeY;

        background = new int[mapY][mapX];
        fillBg(backgroundLoaded);

        overlay = new int[mapY][mapX];


        aff = new Display(this); // Instanciation de la classe d'affichage

        setupCharSelect(); // Déclenchement du jeu en initialisant le premier tour
        aff.mapPanel.repaint();

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
        }
    }

    /**
     * Méthode délenchée lors du clic sur une case lors de la sélection du personnage (étape 0)
     * @param x abscisse
     * @param y ordonnée
     */
    private void charSelect(int x, int y) {
        if (overlay[y][x] == 1) { // Si la case cliquée est effectivement un personnage
            selectedChar = findChar(x, y, players[tour % 2]); // Recherche le personnage positionné sur la case (x,y)
            step = 1;

            setupMoveSelect(); // Setup de l'étape suivante
            aff.mapPanel.repaint();
        }
    }

    /**
     * Mise en place de la sélection des personnages: mise à jour de l'overlay
     */
    private void setupCharSelect() {
        cleanOverlay(); // Nettoyage de l'overlay de l'overlay

        PblCharacter character;
        int counter = 0;

        for (int i = 0; i < players[tour % 2].characters.size(); i++) {
            character = players[tour % 2].characters.get(i);
            if (character.isAlive()) {
                overlay[character.getPosY()][character.getPosX()] = 1; // Le personnage peut être sélectionné, on le met en vert
                counter++;
            }

            if (counter == 0) { // Si il n'y a plus aucun personnage vivant, la partie est terminée
                gameOver = true;
                aff.changeMessage("Jeu terminé : " + players[(tour + 1) % 2].name + " WINS");
                return;
            }

            aff.changeMessage(players[tour % 2].name + " : quel personnage bouger?");
        }
    }


    private void moveSelect(int x, int y) {
        if (overlay[y][x] == 2 || overlay[y][x] == 3) { // Si le déplacement est possible vers la case sélectionnée
            selectedChar.moveTo(x, y); // Déplacement du personnage
            aff.repaint(); // Update de l'affichage

            step = 2; // Passage à l'étape suivante
            setupAttackSelect(); // Setup de l'étape suivante
        }
    }


    private void setupMoveSelect() {
        cleanOverlay();

        findPaths(selectedChar.getPosX(), selectedChar.getPosY(), selectedChar.speed + 1, 2);


        overlay[selectedChar.getPosY()][selectedChar.getPosX()] = 2; // Le personnage peut aussi ne pas se déplacer

        aff.mapPanel.repaint();
        aff.changeMessage(players[tour % 2].name + " : où bouger le personnage?");
    }

    private void attackSelect(int x, int y) {
        if (overlay[y][x] == 4) {
            PblCharacter adversary = findChar(x, y, players[(tour + 1) % 2]); // Recherche du personnage ennemi placé sur la case sélectionné
            selectedChar.attack(adversary);

            step = 0; // Retour à la première étape
            tour++; // Au tour adverse

            setupCharSelect();
        }
    }

    private void setupAttackSelect() {
        cleanOverlay();

        int x;
        int y;
        int counter = 0;

        for (int offsetX = -selectedChar.range; offsetX <= selectedChar.range; offsetX++) {
            for (int offsetY = Math.abs(offsetX) - selectedChar.range; offsetY <= -Math.abs(offsetX) + selectedChar.range; offsetY++) {
                x = selectedChar.getPosX() + offsetX;
                y = selectedChar.getPosY() + offsetY;

                if (x >= 0 && x < mapX && y >= 0 && y < mapY
                        && findChar(x, y, players[(tour + 1) % 2]) != null) {
                    overlay[y][x] = 4;
                    counter++;
                }
            }
        }

        aff.changeMessage(players[tour % 2].name + " : quel personnage attaquer?");

        if (counter == 0) { // Si il n'y a personne à attaquer, passage à l'étape suivante
            step = 0;
            tour++;
            setupCharSelect();
        }

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
        for (int i = 0; i < player.characters.size(); i++) {
            character = player.characters.get(i);

            if (character.getPosX() == x && character.getPosY() == y) {
                return character;
            }
        }

        return null;
    }

    private void findPaths(int x, int y, int distanceLeft, int color) {
        if (distanceLeft > 0 && x >= 0 && x < mapX && y >= 0 && y < mapY) {
            if (color == 2) {

                if (background[y][x] == 0 && (findChar(x, y, players[tour%2]) == selectedChar || findChar(x, y, players[tour%2]) == null) && findChar(x, y, players[(tour+1)%2]) == null) {
                    overlay[y][x] = 2;
                } else {
                    return;
                }
            } else if (color == 4) {
                if (background[y][x] == 0) {
                    if (findChar(x, y, players[(tour+1)%2]) != null) {
                        overlay[y][x] = 4;
                    }
                } else {
                    return;
                }
            }

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
        for (int i = 0; i < mapY; i++) {
            for (int j = 0; j < mapX; j++) {
                overlay[i][j] = 0;
            }
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
            InputStream jsonStream = getClass().getClassLoader().getResourceAsStream("Json/maps.json");

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

    private void fillBg (BackgroundLoader backgroundLoaded) {
        for (int y = 0; y < backgroundLoaded.sizeY; y++) {
            for (int x = 0; x < backgroundLoaded.sizeX; x++) {
                background[y][x] = backgroundLoaded.map.get(y).charAt(x) - '0';
                /* charAt donne une représentation UTF-16 du caractère,
                 * si on y soustrait la valeur ASCII du caractère 0 (48),
                 * on trouve bien le chiffre que l'on veut de la chaine
                 */
            }
        }
    }

    public void pause (int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }
}
