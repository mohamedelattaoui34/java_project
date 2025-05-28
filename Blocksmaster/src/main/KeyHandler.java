package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{

	public static boolean upPressed,downPressed,leftPressed,rightPressed, isPaused;
	public static boolean isRestartRequested = false;

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
	  int code =e.getKeyCode();
	  if(code== KeyEvent.VK_UP) {
		  upPressed= true;
	  }
	  if(code== KeyEvent.VK_LEFT) {
		  leftPressed= true;
	  }
	  if(code== KeyEvent.VK_DOWN) {
		  downPressed= true;
	  }
	  if(code== KeyEvent.VK_RIGHT) {
		  rightPressed= true;
	  }
	  if(code== KeyEvent.VK_0) {
		  if(isPaused) {
			  isPaused=false;
		  }else {
			  isPaused= true;
	  }

		  }
		if (code == KeyEvent.VK_R) {
			isRestartRequested = true;
		}

	}



	@Override
	public void keyReleased(KeyEvent e) {}

}
