package com.zrpg.display;

import javax.swing.*;
import java.awt.*;

import com.zrpg.GameManager;

public class Display extends JFrame {

	private GameManager gm; // Référence à l'objet de gestion du jeu
	public MapDisplay mapPanel; // Dessin de la map
	private  JSplitPane splitPane; // Conteneur de tous les panels
	private JPanel bottomPanel; // Panel du bas qui contient le texte
	private  JLabel message; // Message affiché en bas de l'écran
	public int res; // Taille d'un côté d'une case de la map, calculée à partir de la résolution de l'écran

	public Display(GameManager gm) {
		super("T-RPG"); // Nom de la fenêtre

		this.gm = gm;
		res = calcRes(); // Calcul de la résolution du plateau de jeu en fonction de la taille de l'écran
		mapPanel = new MapDisplay(this, gm);
		splitPane = new JSplitPane();
		bottomPanel = new JPanel();
		message = new JLabel("");


		int messageHeight = 100; // Hauteur du Label
		setPreferredSize(new Dimension(res * gm.sizeX,
				res * gm.sizeY + messageHeight)); // Réglage de la taille de la fenêtre

		getContentPane().setLayout(new GridLayout()); /* Layout en forme de grille qui va recevoir les panels
                                                        (ici 1 colonne, 2 lignes) */
		getContentPane().add(splitPane); // Ajout du panel diviseur contenant les autres dans la fenêtre

		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setTopComponent(mapPanel); // Composant du haut: la map
		splitPane.setBottomComponent(bottomPanel); // Composant du bas: le message
		splitPane.setDividerSize(0); // Taille en px du séparateur
		splitPane.setDividerLocation(res*gm.sizeY); // Emplacement du séparateur depuis le haut
		splitPane.setEnabled(false); // Le séparateur ne peut plus bouger

		Font textFont = new Font("SansSerif", Font.BOLD, 20);
		message.setFont(textFont); // Changement de la police d'écriture
		message.setVerticalAlignment(SwingConstants.CENTER); // Centrage vertical du texte
		message.setForeground(Color.BLACK); // Couleur du texte

		bottomPanel.setBackground(new Color(255, 255, 255)); // Couleur de fond du texte
		bottomPanel.setMaximumSize(new Dimension(res*gm.sizeY, 100)); // Taille max du Label
		bottomPanel.add(message);

		pack(); // Arrange la fenêtre

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Sort du jeu lors du clic sur le bouton de fermeture
		setLocationRelativeTo(null); // Centre
		setResizable(false); // Le fenêtre ne peut pas être redimensionnée
		setVisible(true); // La fenêtre est visible


	}

	/**
	 * Calcul de la résolution de la map à partir de la taille de l'écran
	 * @return taille d'une case en px
	 */
	private int calcRes() {
		final double p = .8; // % de la zone utile a occuper
		int resC;
		Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds(); // Espace dispo de l'écran

		resC = Math.max(
				Math.min((int) (bounds.height * p) / gm.sizeY,
						(int) (bounds.width * p) / gm.sizeX),
				1); // Valeur plancher de 1

		return resC;
	}

	/**
	 * Initialise le jeu lors du clic sur l'écran de titre
	 */
	public void initGame() {
		gm.setupCharSelect();
		bottomPanel.setBackground(new Color(210, 186, 55)); // Met à jour le fond du Label
	}


	/**
	 * Changement du message en bas de l'écran
	 * @param text texte du message
	 */
	public  void changeMessage(String text) {
		message.setText("<html><div style='text-align : center;'>" + text + "</div></html>");
	}

	/**
	 * Ferme la fenêtre courante
	 */
	public void exitApp()
	{
		dispose(); // Fermeture de la fenêtre
		setVisible(false); // Cache la fenêtre
	}

}