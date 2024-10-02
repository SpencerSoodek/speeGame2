package Menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import main.GamePanel;

public class StartMenu {
  GamePanel gp;
  Font arial_40;

  public StartMenu(GamePanel gp) {
    this.gp = gp;
    arial_40 = new Font("Arial", Font.PLAIN, 40);
  }

  public void update() {
    if (gp.kh.spacePressed) {
      System.out.println("startrun");
      gp.startRun();
    }
  }

  public void draw(Graphics2D g2) {

    FontMetrics metrics = g2.getFontMetrics(arial_40);
    int x = (gp.screenWidth - metrics.stringWidth("Press SPACE to begin!")) / 2;
    g2.setFont(arial_40);
    g2.setColor(Color.white);
    g2.drawString("Press SPACE to begin!",  x, gp.screenHeight / 2);
  }

}
