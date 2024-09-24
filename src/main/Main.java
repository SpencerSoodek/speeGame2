package main;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main {

  public static void main(String[] args) {
    JFrame window = new JFrame();
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    GamePanel gp = new GamePanel();
    window.setResizable(false);
    window.setTitle("new game");
    window.add(gp);
    window.pack();
    window.setLocationRelativeTo(null);
    window.setVisible(true);
    gp.startGameThread();
  }

}
