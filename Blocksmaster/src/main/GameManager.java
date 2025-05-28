package main;

import java.awt.Graphics2D;

public interface GameManager {
    enum GameState {
        RUNNING, PAUSED, GAME_OVER, LINE_CLEARED, RETURN_TO_DASHBOARD
    }

    void update();
    void draw(Graphics2D g2);
    void setMinoPickingStrategy(MinoPickingStrategy strategy);
    GameState getGameState();
}