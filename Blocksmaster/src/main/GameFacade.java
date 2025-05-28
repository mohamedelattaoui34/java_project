package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class GameFacade extends JPanel implements Observer, Runnable {
	public static final int WIDTH = 1300;
	public static final int HEIGHT = 700;
	final int FPS = 60;
	Thread gameThread;

	PlayManagerProxy pmProxy;
	KeyHandler keyhandler = new KeyHandler();
	MinoPickingStrategy minoPickingStrategy;
	private JButton returnToDashboardButton;

	public GameFacade(MinoPickingStrategy minoPickingStrategy) {
		this.minoPickingStrategy = minoPickingStrategy;
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(new Color(30, 30, 35));
		this.setLayout(null);
		this.addKeyListener(keyhandler);
		this.setFocusable(true);

		setupReturnButton();

		pmProxy = new PlayManagerProxy(minoPickingStrategy);
		pmProxy.addObserver(this);
	}

	private void setupReturnButton() {
		returnToDashboardButton = new JButton("← Retour au Menu");
		returnToDashboardButton.setBounds(WIDTH - 210, 10, 190, 40);

		// Style du bouton
		returnToDashboardButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		returnToDashboardButton.setForeground(Color.black);
		returnToDashboardButton.setBackground(new Color(70, 130, 180));
		returnToDashboardButton.setFocusPainted(false);
		returnToDashboardButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Bordure
		Border lineBorder = new LineBorder(new Color(100, 149, 237), 2);
		Border emptyBorder = new EmptyBorder(5, 15, 5, 15);
		returnToDashboardButton.setBorder(new CompoundBorder(lineBorder, emptyBorder));

		// Effets de survol
		returnToDashboardButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				returnToDashboardButton.setBackground(new Color(100, 149, 237));
				returnToDashboardButton.setBorder(new CompoundBorder(
						new LineBorder(Color.WHITE, 2),
						new EmptyBorder(5, 15, 5, 15)
				));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				returnToDashboardButton.setBackground(new Color(70, 130, 180));
				returnToDashboardButton.setBorder(new CompoundBorder(
						new LineBorder(new Color(100, 149, 237), 2),
						new EmptyBorder(5, 15, 5, 15)
				));
			}
		});

		returnToDashboardButton.addActionListener(e -> returnToDashboard());
		this.add(returnToDashboardButton);
	}

	public void setMinoPickingStrategy(MinoPickingStrategy minoPickingStrategy) {
		this.minoPickingStrategy = minoPickingStrategy;
		pmProxy.setMinoPickingStrategy(minoPickingStrategy);
	}

	public void launchGame() {
		InstructionsDialog.showInstructions(this);
		gameThread = new Thread(this);
		gameThread.start();
	}

	private void returnToDashboard() {
		gameThread = null;
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
		if (frame != null) {
			frame.dispose();
		}
		openDashboard();
	}

	private void openDashboard() {
		SwingUtilities.invokeLater(() -> {
			new DashboardScreen(Main.getLoggedInUserEmail()).setVisible(true);
		});
	}

	@Override
	public void run() {
		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;

		while (gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
			}
		}
	}

	private void update() {
		if (KeyHandler.isPaused) {
			pmProxy.pauseGame();
		} else {
			pmProxy.resumeGame();
		}

		if (KeyHandler.isRestartRequested) {
			restartGame();
			KeyHandler.isRestartRequested = false;
		}

		pmProxy.update();
	}

	private void restartGame() {
		pmProxy = new PlayManagerProxy(minoPickingStrategy);
		pmProxy.addObserver(this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		pmProxy.draw(g2);
	}

	@Override
	public void update(Observable observable) {
		// Mise à jour de l'état du jeu
	}
}