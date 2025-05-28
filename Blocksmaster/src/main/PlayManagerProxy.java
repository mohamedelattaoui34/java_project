package main;

import java.awt.Graphics2D;

public class PlayManagerProxy implements GameManager {
    private PlayManager realPlayManager;
    private boolean isGamePaused = false;

    public PlayManagerProxy(MinoPickingStrategy strategy) {
        this.realPlayManager = new PlayManager(strategy);
    }

    public void pauseGame() {
        isGamePaused = true;
    }

    public void resumeGame() {
        isGamePaused = false;
    }

    public boolean isGamePaused() {
        return isGamePaused;
    }

    @Override
    public void update() {
        if (!isGamePaused) {
            realPlayManager.update();
        }
        // Si le jeu est en pause, on n'appelle pas update() sur le vrai PlayManager
    }

    @Override
    public void draw(Graphics2D g2) {
        realPlayManager.draw(g2); // On dessine même si le jeu est en pause
    }

    @Override
    public void setMinoPickingStrategy(MinoPickingStrategy strategy) {
        realPlayManager.setMinoPickingStrategy(strategy);
    }

    @Override
    public GameState getGameState() {
        return realPlayManager.getGameState();
    }

    // Méthodes pour accéder aux fonctionnalités du PlayManager
    public void addObserver(Observer observer) {
        realPlayManager.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        realPlayManager.removeObserver(observer);
    }
}


