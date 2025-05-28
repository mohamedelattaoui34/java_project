package main;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import mino.Block;
import mino.Mino;

public class PlayManager implements Observable, GameManager {
	final int WIDTH = 360;
	final int HEIGHT = 600;
	public static int left_x;
	public static int right_x;
	public static int top_y;
	public static int bottom_y;
	private boolean isPaused = false;

	public static int dropInterval = 60; // Vitesse initiale
	private static final int BASE_DROP_INTERVAL = 60; // Vitesse de base
	private static final int MIN_DROP_INTERVAL = 5;



	Mino currentMino;
	final int MINO_START_X;
	final int MINO_START_Y;
	Mino nextMino;
	final int NEXTMINO_X;
	final int NEXTMINO_Y;
	public static ArrayList<Block> staticBlocks = new ArrayList<>();

	boolean gameOver;
	private GameTheme currentTheme = GameTheme.DEFAULT;

	boolean effectConterOn;
	int effectConter;
	ArrayList<Integer> effectY = new ArrayList<>();

	int difficulty = 1;
	int completedLines;
	int totalScore;

	private List<Observer> observers = new ArrayList<>();
	private GameState gameState;

	public enum GameTheme {
		DEFAULT(new Color(34, 40, 49), new Color(57, 62, 70), new Color(0, 173, 181), new Color(238, 238, 238), new Color(27, 30, 35)),
		DARK(new Color(15, 15, 20), new Color(60, 60, 90), new Color(180, 0, 200), new Color(220, 220, 220), new Color(20, 20, 30)),
		RETRO(new Color(40, 20, 40), new Color(120, 60, 120), new Color(255, 215, 0), new Color(255, 250, 200), new Color(30, 15, 30)),
		NATURE(new Color(34, 139, 34), new Color(0, 100, 0), new Color(210, 180, 140), new Color(240, 240, 240), new Color(0, 80, 0));

		private final Color bgColor;
		private final Color frameColor;
		private final Color accentColor;
		private final Color textColor;
		private final Color gameAreaBg;

		GameTheme(Color bgColor, Color frameColor, Color accentColor, Color textColor, Color gameAreaBg) {
			this.bgColor = bgColor;
			this.frameColor = frameColor;
			this.accentColor = accentColor;
			this.textColor = textColor;
			this.gameAreaBg = gameAreaBg;
		}
	}

	public void togglePause() {
		isPaused = !isPaused;
		setGameState(isPaused ? GameState.PAUSED : GameState.RUNNING);
	}

	public void cycleTheme() {
		GameTheme[] themes = GameTheme.values();
		int currentOrdinal = currentTheme.ordinal();
		int nextOrdinal = (currentOrdinal + 1) % themes.length;
		currentTheme = themes[nextOrdinal];
	}

	public void setTheme(GameTheme theme) {
		this.currentTheme = theme;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void addObserver(Observer o) {
		observers.add(o);
	}

	public void removeObserver(Observer o) {
		observers.remove(o);
	}

	public void setGameState(GameState state) {
		if (this.gameState != state) {
			this.gameState = state;
			System.out.println("Game state updated: " + state);
			notifyObservers();
		}
	}

	public GameState getGameState() {
		return gameState;
	}

	public void notifyObservers() {
		for (Observer observer : observers) {
			observer.update(null);
		}
	}

	private MinoPickingStrategy minoPickingStrategy;

	public PlayManager(MinoPickingStrategy minoPickingStrategy) {
		left_x = (GameFacade.WIDTH / 2) - (WIDTH / 2);
		right_x = left_x + WIDTH;
		top_y = 50;
		bottom_y = top_y + HEIGHT;
		MINO_START_X = left_x + (WIDTH / 2) - Block.SIZE;
		NEXTMINO_X = right_x + 175;
		MINO_START_Y = top_y + Block.SIZE;
		NEXTMINO_Y = top_y + 500;

		if (minoPickingStrategy != null) {
			this.minoPickingStrategy = minoPickingStrategy;
			currentMino = pickMino();
			currentMino.setXY(MINO_START_X, MINO_START_Y);
			nextMino = pickMino();
			nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
		} else {
			throw new IllegalArgumentException("MinoPickingStrategy cannot be null");
		}
	}

	public void setMinoPickingStrategy(MinoPickingStrategy strategy) {
		this.minoPickingStrategy = strategy;
	}

	private Mino pickMino() {
		return minoPickingStrategy.pickMino();
	}

	public void update() {
		if (gameOver) {
			setGameState(GameState.GAME_OVER);
			return;
		}

		if (isPaused) return;

		if (!currentMino.active) {
			for (Block b : currentMino.b) {
				staticBlocks.add(b);
			}

			if (currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
				gameOver = true;
			}

			currentMino.deactivating = false;
			currentMino = nextMino;
			currentMino.setXY(MINO_START_X, MINO_START_Y);
			nextMino = pickMino();
			nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

			checkDelete();
		} else {
			currentMino.update();
		}
	}

	private void checkDelete() {
		int x = left_x;
		int y = top_y;
		int blockCount = 0;
		int lineCount = 0;

		while (x < right_x && y < bottom_y) {
			for (Block b : staticBlocks) {
				if (b.x == x && b.y == y) {
					blockCount++;
				}
			}

			x += Block.SIZE;

			if (x == right_x) {
				if (blockCount == 12) {
					effectConterOn = true;
					effectY.add(y);
					int finalY = y;
					staticBlocks.removeIf(b -> b.y == finalY);
					lineCount++;
					completedLines++;

					// NOUVELLE LOGIQUE : Mise à jour de la vitesse basée sur la difficulté
					if (completedLines % 10 == 0) {
						difficulty++;
						updateDropSpeed(); // Nouvelle méthode
					}

					for (Block b : staticBlocks) {
						if (b.y < y) b.y += Block.SIZE;
					}

					playLineClearSound();
				}

				blockCount = 0;
				x = left_x;
				y += Block.SIZE;
			}
		}

		if (lineCount > 0) {
			totalScore += (10 * difficulty) * lineCount;
			setGameState(GameState.LINE_CLEARED);
		}
	}

	// NOUVELLE MÉTHODE : Met à jour la vitesse de chute
	private void updateDropSpeed() {
		// Formule simple : vitesse diminue avec la difficulté
		dropInterval = Math.max(MIN_DROP_INTERVAL, BASE_DROP_INTERVAL - (difficulty * 5));

		System.out.println("Difficulté: " + difficulty + " - Vitesse: " + dropInterval);
	}

	private void playLineClearSound() {
		try {
			File soundFile = new File("line_clear_sound.wav");
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public void draw(Graphics2D g2) {
		Color bgColor = currentTheme.bgColor;
		Color frameColor = currentTheme.frameColor;
		Color accentColor = currentTheme.accentColor;
		Color textColor = currentTheme.textColor;
		Color gameAreaBg = currentTheme.gameAreaBg;

		g2.setColor(bgColor);
		g2.fillRect(0, 0, GameFacade.WIDTH, GameFacade.HEIGHT);

		g2.setColor(gameAreaBg);
		g2.fillRoundRect(left_x, top_y, WIDTH, HEIGHT, 20, 20);

		g2.setColor(frameColor);
		g2.setStroke(new BasicStroke(5f));
		g2.drawRoundRect(left_x, top_y, WIDTH, HEIGHT, 20, 20);

		// Side Panel - Next Block & Stats
		int infoX = right_x + 100;
		int infoY = top_y;

		g2.setColor(bgColor);
		g2.fillRoundRect(infoX, infoY, 250, 300, 20, 20);
		g2.setColor(frameColor);
		g2.drawRoundRect(infoX, infoY, 250, 300, 20, 20);

		g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
		g2.setColor(accentColor);
		g2.drawString("NEXT", infoX + 80, infoY + 40);

		g2.setColor(textColor);
		g2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		g2.drawString("Difficulty: " + difficulty, infoX + 30, infoY + 100);
		g2.drawString("Lines:     " + completedLines, infoX + 30, infoY + 150);
		g2.drawString("Score:     " + totalScore, infoX + 30, infoY + 200);

		// Next mino preview
		g2.setColor(bgColor);
		g2.fillRoundRect(infoX, bottom_y - 200, 200, 200, 20, 20);
		g2.setColor(frameColor);
		g2.drawRoundRect(infoX, bottom_y - 200, 200, 200, 20, 20);

		currentMino.draw(g2);
		nextMino.draw(g2);

		for (Block b : staticBlocks) {
			b.draw(g2);
		}

		// Line clear animation
		if (effectConterOn) {
			effectConter++;
			g2.setColor(new Color(255, 255, 255, 100));
			for (int ey : effectY) {
				g2.fillRect(left_x, ey, WIDTH, Block.SIZE);
			}
			if (effectConter == 10) {
				effectConterOn = false;
				effectConter = 0;
				effectY.clear();
			}
		}

		// Status Texts
		g2.setFont(new Font("Segoe UI", Font.BOLD, 50));
		g2.setColor(accentColor);
		if (gameOver) {
			g2.drawString("GAME OVER", left_x + 25, top_y + 320);
		} else if (isPaused) {
			g2.drawString("PAUSED", left_x + 80, top_y + 320);
		}

		// Title
		g2.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 36));
		g2.drawString("BLOCK MASTER", left_x + 30, top_y - 20);
	}
}
