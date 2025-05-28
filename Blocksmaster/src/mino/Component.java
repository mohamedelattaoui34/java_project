package mino;

import java.awt.Graphics2D;

public interface Component {
    void draw(Graphics2D g2);
    void update();
    void setPosition(int x, int y);
}