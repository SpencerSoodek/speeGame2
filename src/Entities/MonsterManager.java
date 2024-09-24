package Entities;

import java.awt.Graphics2D;
import main.GamePanel;

public class MonsterManager {
  GamePanel gp;
  public Entity monsters[] = new Entity[10];

  public MonsterManager(GamePanel gp) {
    this.gp = gp;
  }

  public void drawMonsters(Graphics2D g2) {
    for (Entity m: monsters) {
      if (m != null) {
        m.draw(g2);
      }
    }
  }

  public void update() {
    for (Entity m: monsters) {
      if (m != null) {
        m.update();
      }
    }
  }

}
