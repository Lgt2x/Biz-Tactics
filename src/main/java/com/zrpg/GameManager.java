package com.zrpg;

import java.util.Arrays;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.zrpg.characters.*;
import com.zrpg.display.*;
import com.zrpg.jsonloaders.BackgroundLoader;

public class GameManager {
    private  int selectedMap = 0;

    private  Display aff; // Référence à l'objet d'affichage
    public  int[][] overlay; // Tableau 2D qui représente les cases de l'overlay sur la map
    public  int mapX = 15; // Nombre de colonnes de la map
    public  int mapY = 10; // Nombre de lignes de la map
    public  int[][] background; // Fond, avec ses obstacles

    public  boolean gameOver = false; // Vrai lorsque la partie est finie
    public  Player[] players; // Liste de joueurs
    public  PblCharacter selectedChar; // Personnage joué ce tour, pour le déplacement et l'attaque

    /*
        tour%2 = 0 => Joueur 1
        tour%2 = 1 => Joueur 2
    */

    public  int tour;

    /*
        step%3 = 0 => Sélection du personnage à jouer
        step%3 = 1 => Sélection de la case de déplacement
        step%3 = 2 => Sélection du personnage à attaquer
    */

    public  int step;

    public GameManager() {

        tour = 0;
        step = 0;

        players = new Player[] {
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

        System.out.println(backgroundLoaded.name);

        mapX = backgroundLoaded.sizeX;
        mapY = backgroundLoaded.sizeY;

        background = new int[mapY][mapX];
        overlay = new int[mapY][mapX];

        for (int y = 0; y<mapY; y++) {
            for (int x = 0; x<mapX; x++) {
                background[y][x] = backgroundLoaded.map.get(y).charAt(x) - '0';
                /* charAt donne une représentation UTF-16 du caractère,
                 * si on y soustrait la valeur ASCII du caractère 0 (48),
                 * on trouve bien le chiffre que l'on veut de la chaine
                */
            }
        }

        aff = new Display(this); // Instanciation de la classe d'affichage

        setupCharSelect(); // Déclenchement du jeu en initialisant le premier tour
        aff.mapPanel.repaint();

    }

    /*
     * Méthode déclenchée au clic sur une case, déclenchée par le gestionnaire d'events de la MapDisplay
    */
    public  void clickHandle(int x, int y) {
        if (!gameOver) {
            switch (step%3) {
                case 0:
                    charSelect(x, y);
                    break;
                case 1:
                    moveSelect(x,y);
                    break;
                case 2:
                    attackSelect(x,y);
                    break;
                default:
                    System.out.println("Erreur");

            }
        }
    }

    /**** SELECTION DU PERSONNAGE ****/
    public  void charSelect(int x, int y) {
        if (overlay[y][x] == 1) { // Si la case cliquée est effectivement un personnage
            selectedChar = findChar(x, y, players[tour%2]); // Recherche le personnage positionné sur la case (x,y)
            step = 1;

            setupMoveSelect(); // Setup de l'étape suivante
            aff.mapPanel.repaint();
        }
    }

    public  void setupCharSelect() {
        cleanOverlay(); // Nettoyage de l'overlay de l'overlay

        PblCharacter character;
        int counter = 0;

        for (int i = 0; i < players[tour%2].characters.size(); i++) {
            character = players[tour%2].characters.get(i);
            if (character.isAlive()) {
                overlay[character.getPosY()][character.getPosX()] = 1; // Le personnage peut être sélectionné, on le met en vert
                counter++;
            }

            if (counter == 0) { // Si il n'y a plus aucun personnage vivant, la partie est terminée
                gameOver = true;
                aff.changeMessage("Jeu terminé : " + players[(tour+1)%2].name + " WINS");
                return;
            }

            aff.changeMessage(players[tour%2].name + " : quel personnage bouger?");
        }
    }


    /**** SELECTION DU MOUVEMENT ****/
    public  void moveSelect(int x, int y) {
        if (overlay[y][x] == 2 || overlay[y][x] == 3) { // Si le déplacement est possible vers la case sélectionnée
            selectedChar.moveTo(x, y); // Déplacement du personnage
            aff.repaint(); // Update de l'affichage

            step = 2; // Passage à l'étape suivante
            setupAttackSelect(); // Setup de l'étape suivante
        }
    }


    public  void setupMoveSelect() {
        cleanOverlay();

        int x;
        int y;

        for (int offsetX = -selectedChar.speed; offsetX <= selectedChar.speed; offsetX++) {
            for (int offsetY = Math.abs(offsetX) - selectedChar.speed; offsetY <= -Math.abs(offsetX) + selectedChar.speed; offsetY++) {

                x = selectedChar.getPosX()+offsetX;
                y = selectedChar.getPosY()+offsetY;

                if (x >= 0 && x < mapX
					&& y >= 0 && y < mapY
					&& findChar(x, y, players[(tour+1)%2]) == null // Vérification s'il n'y a pas de personnage ennemi sur la case de déplacement
					&& findChar(x, y, players[tour%2]) == null // Pas de personnage allié non plus
					&& background[y][x] == 0) { // Si c'est bien une case d'herbe
                        overlay[y][x] = 2; // La case devient bleue
                }
            }

            aff.changeMessage(players[tour%2].name + " : où bouger le personnage?");


        }

        overlay[selectedChar.getPosY()][selectedChar.getPosX()] = 2; // Le personnage peut aussi ne pas se déplacer

        aff.mapPanel.repaint();
    }


    /**** SELECTION DU PERSONNAGE ATTAQUE ****/
    public  void attackSelect(int x, int y) {
        if (overlay[y][x] == 4) {
            PblCharacter adversary = findChar(x, y, players[(tour+1)%2]); // Recherche du personnage ennemi placé sur la case sélectionné
            selectedChar.attack(adversary);

            step = 0; // Retour à la première étape
            tour++; // Au tour adverse

            setupCharSelect();
        }
    }

    public  void setupAttackSelect() {
        cleanOverlay();

        int x;
        int y;
        int counter = 0;

        for (int offsetX = -selectedChar.range; offsetX <= selectedChar.range; offsetX++) {
            for (int offsetY = Math.abs(offsetX) - selectedChar.range; offsetY <= -Math.abs(offsetX) + selectedChar.range; offsetY++) {
                x = selectedChar.getPosX()+offsetX;
                y = selectedChar.getPosY()+offsetY;

                if (x >= 0 && x < mapX && y >= 0 && y < mapY
                    && findChar(x, y, players[(tour+1)%2]) != null) {
                    overlay[y][x] = 4;
                    counter++;
                }
            }
        }

        aff.changeMessage(players[tour%2].name + " : quel personnage attaquer?");

        if (counter == 0) { // Si il n'y a personne à attaquer, passage à l'étape suivante
            step = 0;
            tour++;
            setupCharSelect();
        }

        aff.mapPanel.repaint();
    }

    /*
    Cherche si un personnage existe sur cette case
    */
    public  PblCharacter findChar (int x, int y, Player player) {
        PblCharacter character;
        for (int i = 0; i < player.characters.size(); i++) {
            character = player.characters.get(i);

            if (character.getPosX() == x && character.getPosY() == y) {
                return character;
            }
        }

        return null;
    }

    /*
    Nettoie l'overlay
    */
    public  void cleanOverlay() {
        for (int i=0; i<mapY; i++) {
            for (int j=0; j<mapX; j++) {
                overlay[i][j] = 0;
            }
        }
    }

    public  BackgroundLoader loadMap(int map) {
        try {
            InputStream a = getClass().getClassLoader().getResourceAsStream("Json/maps.json");
            Gson gson = new Gson();
            BackgroundLoader[] backgrounds = gson.fromJson(new InputStreamReader(a), BackgroundLoader[].class);
            return backgrounds[map];
        } catch (Exception e) {
            System.out.println("Erreur de chargement du décor");
            return null;
        }

    }

    public  void pause(int ms) {
        try {
            Thread.sleep(ms);
        } catch(InterruptedException e){}
    }
}
