package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
  public boolean leftPressed, rightPressed, upPressed, downPressed, spacePressed;

  @Override
  public void keyTyped(KeyEvent e) {
  }

  @Override
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();
    switch(keyCode) {
      // a: 65
      case KeyEvent.VK_A:
        leftPressed = true;
        break;
      case KeyEvent.VK_W:
        upPressed = true;
        break;
      case KeyEvent.VK_S:
        downPressed = true;
        break;
      case KeyEvent.VK_D:
        rightPressed = true;
        break;
      case KeyEvent.VK_SPACE:
        spacePressed = true;
        break;
    }

  }

  @Override
  public void keyReleased(KeyEvent e) {
    int keyCode = e.getKeyCode();
    switch(keyCode) {
      // a: 65
      case KeyEvent.VK_A:
        leftPressed = false;
        break;
      case KeyEvent.VK_W:
        upPressed = false;
        break;
      case KeyEvent.VK_S:
        downPressed = false;
        break;
      case KeyEvent.VK_D:
        rightPressed = false;
        break;
      case KeyEvent.VK_SPACE:
        spacePressed = false;
        break;
    }

  }
}
