package Entities;

import java.awt.Graphics2D;
import java.awt.Point;
import main.GamePanel;

public class MonsterManager {

  GamePanel gp;
  public Entity monsters[] = new Entity[10];

  int allocationPoints = 0;
  // assign 1 point every 2 seconds, try to spawn monsters.
  int pointRate = 120;
  int pointCounter = 0;

  public MonsterManager(GamePanel gp) {
    this.gp = gp;
  }

  public void drawMonsters(Graphics2D g2) {
    for (Entity m : monsters) {
      if (m != null) {
        m.draw(g2);
      }
    }

    for (Entity m: monsters) {
      if (m != null) {
        m.drawHealthBar(g2);
      }
    }


  }

  public void damageMonster(int monsterIndex, int damage) {
    monsters[monsterIndex].health -= damage;
    monsters[monsterIndex].displayHealthCounter = 0;
    monsters[monsterIndex].displayHealth = true;
    if (monsters[monsterIndex].health <= 0) {
      killMonster(monsterIndex);
    }
  }

  public void spawnMonsters() {
    if (allocationPoints > 2) {
      double r = Math.random();
      if (r > 0.5) {
        for (int i = 0; i <= monsters.length; i++) {
          if (i == monsters.length) {
            return;
          }
          if (monsters[i] == null) {
            Point p = gp.tileM.randomSpawnablePoint();
            monsters[i] = new CreatureMonst(gp, p.x, p.y);
            return;
          }
        }
      }
    }
  }

  public void update() {
    for (Entity m : monsters) {
      if (m != null) {
        m.update();
      }
    }
    pointCounter++;
    if (pointCounter > pointRate) {
      allocationPoints++;
      pointCounter -= pointRate;
      spawnMonsters();
    }
  }

  // delete monster at index
  public void killMonster(int i) {
    Entity m = monsters[i];
    gp.tileM.occupiedTiles[m.col][m.row] = false;
    monsters[i] = null;
  }

}
