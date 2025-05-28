package mino;

import java.awt.Color;
import java.awt.Graphics2D;

import main.KeyHandler;
import main.PlayManager;

public class Mino {
	private BlockGroup blockGroup = new BlockGroup();
	public Block[] b = new Block[4];
	public Block[] tempB = new Block[4];
	int autoDropCounter = 0;
	public int direction = 1; // 4 directions: 1, 2, 3, 4
	boolean leftCollision, rightCollision, bottomCollision;
	public boolean active = true;
	public boolean deactivating;
	int deactivateCounter = 0;

	public void create(Color c) {
		for (int i = 0; i < 4; i++) {
			b[i] = new Block(c);
			tempB[i] = new Block(c);
			blockGroup.addComponent(b[i]);
		}
	}

	public void setXY(int x, int y) {
		// À implémenter selon le type de pièce
	}

	public void updateXY(int direction) {
		checkRotationCollision();
		if (!leftCollision && !rightCollision && !bottomCollision) {
			this.direction = direction;
			for (int i = 0; i < 4; i++) {
				b[i].x = tempB[i].x;
				b[i].y = tempB[i].y;
			}
		}
	}

	public void getDirection1() {}
	public void getDirection2() {}
	public void getDirection3() {}
	public void getDirection4() {}

	public void checkMovementCollision() {
		leftCollision = false;
		rightCollision = false;
		bottomCollision = false;

		checkStaticBlocCollision();

		for (Block block : b) {
			if (block.x == PlayManager.left_x) leftCollision = true;
			if (block.x + Block.SIZE == PlayManager.right_x) rightCollision = true;
			if (block.y + Block.SIZE == PlayManager.bottom_y) bottomCollision = true;
		}
	}

	public void checkRotationCollision() {
		leftCollision = false;
		rightCollision = false;
		bottomCollision = false;

		checkStaticBlocCollision();

		for (int i = 0; i < b.length; i++) {
			if (tempB[i].x < PlayManager.left_x) leftCollision = true;
			if (tempB[i].x + Block.SIZE > PlayManager.right_x) rightCollision = true;
			if (tempB[i].y + Block.SIZE > PlayManager.bottom_y) bottomCollision = true;
		}
	}

	private void checkStaticBlocCollision() {
		for (Block staticBlock : PlayManager.staticBlocks) {
			int targetX = staticBlock.x;
			int targetY = staticBlock.y;

			for (Block block : b) {
				if (block.y + Block.SIZE == targetY && block.x == targetX) bottomCollision = true;
				if (block.x - Block.SIZE == targetX && block.y == targetY) leftCollision = true;
				if (block.x + Block.SIZE == targetX && block.y == targetY) rightCollision = true;
			}
		}
	}

	public void update() {
		if (deactivating) {
			handleDeactivation();
		}

		if (KeyHandler.upPressed) {
			switch (direction) {
				case 1 -> getDirection2();
				case 2 -> getDirection3();
				case 3 -> getDirection4();
				case 4 -> getDirection1();
			}
			KeyHandler.upPressed = false;
		}

		checkMovementCollision();

		if (KeyHandler.downPressed) {
			if (!bottomCollision) {
				for (Block block : b) {
					block.y += Block.SIZE;
				}
				autoDropCounter = 0;
			}
			KeyHandler.downPressed = false;
		}

		if (KeyHandler.leftPressed && !leftCollision) {
			for (Block block : b) {
				block.x -= Block.SIZE;
			}
			KeyHandler.leftPressed = false;
		}

		if (KeyHandler.rightPressed && !rightCollision) {
			for (Block block : b) {
				block.x += Block.SIZE;
			}
			KeyHandler.rightPressed = false;
		}

		if (bottomCollision) {
			deactivating = true;
		} else {
			autoDropCounter++;
			if (autoDropCounter >= PlayManager.dropInterval) {
				for (Block block : b) {
					block.y += Block.SIZE;
				}
				autoDropCounter = 0;
			}
		}
	}

	private void handleDeactivation() {
		deactivateCounter++;
		if (deactivateCounter >= 45) {
			deactivateCounter = 0;
			checkMovementCollision();
			if (bottomCollision) {
				active = false;
			}
		}
	}

	public void draw(Graphics2D g2) {
		int margin = 2;
		g2.setColor(b[0].c);
		for (Block block : b) {
			g2.fillRect(block.x + margin, block.y + margin,
					Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
		}
	}
}
