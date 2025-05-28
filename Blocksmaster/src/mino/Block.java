package mino;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Block extends Rectangle implements Component {

	public int x, y;
	public static final int SIZE = 30;    //30x30 block
	public Color c;

	public Block(Color c) {
		this.c = c;
	}

	@Override
	public void draw(Graphics2D g2) {
		int margin = 2;
		g2.setColor(c);
		g2.fillRect(x + margin, y + margin, SIZE - (margin * 2), SIZE - (margin * 2));
	}

	@Override
	public void update() {
		// Block-specific update logic if needed
	}

	@Override
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
}