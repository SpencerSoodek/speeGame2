package Objects;

import java.awt.Graphics2D;
import main.GamePanel;

public class ObjectManager {

  public GameObject[] objects = new GameObject[10];
  GamePanel gp;

  public ObjectManager(GamePanel gp) {
    this.gp = gp;
  }

  public boolean objectAt(int x, int y) {
    for (GameObject o : objects) {
      if (o != null) {
        if (o.x == x && o.y == y) {
          return true;
        }
      }
    }
    return false;
  }


  public void drawObjects(Graphics2D g2) {
    for (GameObject o : objects) {
      if (o != null) {
        int worldX = o.x * gp.tileSize;
        int worldY = o.y * gp.tileSize;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        if (screenX > gp.player.screenX - (gp.screenWidth / 2) - 48
            && screenX < gp.player.screenX + (gp.screenWidth / 2) + 48 &&
            screenY < gp.player.screenY + (gp.screenHeight / 2) + 48
            && screenY > gp.player.screenY - (gp.screenHeight / 2) - 48) {
          g2.drawImage(o.image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
      }
    }
  }


}
