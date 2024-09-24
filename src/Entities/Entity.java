package Entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.Direction;
import main.GamePanel;
import main.UtilityTool;

public class Entity {

  public int row;
  public int col;
  public int worldX;
  public int worldY;
  public int screenX;
  public int screenY;
  int speed;
  GamePanel gp;
  public Direction direction;

  boolean collisionOn;
  boolean moving;
  int targetX;
  int targetY;

  public BufferedImage up1, up2, left1, left2, right1, right2, down1, down2;

  int stepCounter = 0;
  boolean step2 = false;
  int stepRate = 60;

  public Entity(GamePanel gp) {
    this.gp = gp;
  }

  public void setAction() {
  }

  public void update() {
  }

  public void secondStep() {
    if (moving) {
      stepCounter++;
      if (stepCounter > stepRate) {
        stepCounter -= stepRate;
        step2 = !step2;
      }
    }
  }

  public BufferedImage getCurrImage() {
    BufferedImage im = null;
    switch (direction) {
      case DOWN:
        if (!step2) {
          im = down1;
        } else {
          im = down2;
        }
        break;
      case LEFT:
        if (!step2) {
          im = left1;
        } else {
          im = left2;
        }
        break;
      case RIGHT:
        if (!step2) {
          im = right1;
        } else {
          im = right2;
        }
        break;
      case UP:
        if (!step2) {
          im = up1;
        } else {
          im = up2;
        }
        break;

    }
    return im;
  }

  public void draw(Graphics2D g2) {
    secondStep();
    BufferedImage im = getCurrImage();

    int screenX = worldX - gp.player.worldX + gp.player.screenX;
    int screenY = worldY - gp.player.worldY + gp.player.screenY;

    /*
    if (screenX > gp.player.screenX - (gp.screenWidth / 2) - 48
        && screenX < gp.player.screenX + (gp.screenWidth / 2) + 48 &&
        screenY < gp.player.screenY + (gp.screenHeight / 2) + 48
        && screenY > gp.player.screenY - (gp.screenHeight / 2) - 48) {
      switch (direction) {
        case DOWN:
          if (!step2) {
            im = down1;
          } else {
            im = down2;
          }
          break;
        case LEFT:
          if (!step2) {
            im = left1;
          } else {
            im = left2;
          }
          break;
        case RIGHT:
          if (!step2) {
            im = right1;
          } else {
            im = right2;
          }
          break;
        case UP:
          if (!step2) {
            im = up1;
          } else {
            im = up2;
          }
          break;

      }
      */
    g2.drawImage(im, screenX, screenY, null);

  }

  public BufferedImage setup(String imageName) {
    UtilityTool utilityTool = new UtilityTool();
    BufferedImage image = null;
    try {
      image = ImageIO.read(getClass().getResourceAsStream(imageName + ".png"));
      image = utilityTool.scaleImage(image, gp.tileSize, gp.tileSize);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return image;
  }

  // Start moving at entity's speed in its direction.
  public void startMoving() {
    gp.tileM.occupiedTiles[col][row] = false;
    switch (direction) {
      case UP:
        moving = true;
        targetX = worldX;
        targetY = worldY - gp.tileSize;
        gp.tileM.occupiedTiles[col][row - 1] = true;
        break;
      case DOWN:
        moving = true;
        targetX = worldX;
        targetY = worldY + gp.tileSize;
        gp.tileM.occupiedTiles[col][row + 1] = true;
        break;
      case LEFT:
        moving = true;
        targetX = worldX - gp.tileSize;
        targetY = worldY;
        gp.tileM.occupiedTiles[col - 1][row] = true;
        break;
      case RIGHT:
        moving = true;
        targetX = worldX + gp.tileSize;
        targetY = worldY;
        gp.tileM.occupiedTiles[col + 1][row] = true;
        break;
    }
  }
}



