package Entities;

import static main.Direction.DOWN;
import static main.Direction.LEFT;
import static main.Direction.RIGHT;
import static main.Direction.UP;

import Objects.GameObject;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.GamePanel;
import tile.Tile;

public class Player extends Entity {

  public int keys;

  public Player(GamePanel gp) {
    // this.row = 10;
    // this.col = 10;
    super(gp);
    this.col = gp.gameLevel.playerStartX;
    this.row = gp.gameLevel.playerStartY;
    this.speed = 4;
    this.worldX = (col * gp.tileSize);
    this.worldY = (row * gp.tileSize);
    gp.tileM.occupiedTiles[col][row] = true;
    this.screenX = (gp.screenWidth / 2) - gp.tileSize;
    this.screenY = (gp.screenHeight / 2) - gp.tileSize;
    this.direction = DOWN;
    getPlayerImage();
    this.keys = 0;
    this.stepRate = 30;

    moving = false;
  }

  public void update() {
    // If currently moving, continue moving until reaching the target position.
    if (moving) {
      switch (direction) {
        case UP:
          if (worldY <= targetY) {
            worldY = targetY;  // Snap to the grid.
            moving = false;
            row--;  // Update row after movement completes.
            checkInteractions();
            System.out.println("X: " + col + " Y: " + row + " occupied: " + gp.tileM.occupiedTiles[col][row]);
          } else {
            worldY -= speed;
          }
          break;
        case DOWN:
          if (worldY >= targetY) {
            worldY = targetY;
            moving = false;
            row++;
            checkInteractions();
            System.out.println("X: " + col + " Y: " + row + " occupied: " + gp.tileM.occupiedTiles[col][row]);
          } else {
            worldY += speed;
          }
          break;
        case LEFT:
          if (worldX <= targetX) {
            worldX = targetX;
            moving = false;
            col--;
            checkInteractions();
            System.out.println("X: " + col + " Y: " + row + " occupied: " + gp.tileM.occupiedTiles[col][row]);
          } else {
            worldX -= speed;
          }
          break;
        case RIGHT:
          if (worldX >= targetX) {
            worldX = targetX;
            moving = false;
            col++;
            checkInteractions();
            System.out.println("X: " + col + " Y: " + row + " occupied: " + gp.tileM.occupiedTiles[col][row]);
          } else {
            worldX += speed;
          }
          break;
      }
    }

    // If no movement and a key is pressed, set direction and check collision.
    if (!moving && (gp.kh.upPressed || gp.kh.downPressed || gp.kh.leftPressed
        || gp.kh.rightPressed)) {
      // Set direction based on key pressed.
      if (gp.kh.upPressed) {
        direction = UP;
      } else if (gp.kh.downPressed) {
        direction = DOWN;
      } else if (gp.kh.leftPressed) {
        direction = LEFT;
      } else if (gp.kh.rightPressed) {
        direction = RIGHT;
      }

      // Check for collision after setting direction.
      collisionOn =
          gp.cChecker.checkCollision(this) || gp.cChecker.objCollision(this) || gp.cChecker.checkOccupied(this);

      // Start moving in the direction if no collision.
      if (!collisionOn) {
        startMoving();
      }
    }
  }

  public void checkInteractions() {
    for (int i = 0; i < this.gp.gameLevel.om.objects.length; i++) {
      GameObject o = this.gp.gameLevel.om.objects[i];
      if (o != null) {
        if (o.interactable && o.x == this.col && o.y == this.row) {
          switch (o.objType) {
            case KEY:
              this.keys++;
              gp.ui.showMessage("Picked up key");
              this.gp.gameLevel.om.objects[i] = null;
              break;
            case DOOR:
              if (keys > 0 && o.collision == true) {
                o.collision = false;
                this.keys--;
                System.out.println("door opened");

              }
              break;
          }
        }
      }
    }
  }


  public void getPlayerImage() {
    up1 = setup("/player/spee_up_1");
    up2 = setup("/player/spee_up_2");
    left1 = setup("/player/spee_left_1");
    left2 = setup("/player/spee_left_2");
    right1 = setup("/player/spee_right_1");
    right2 = setup("/player/spee_right_2");
    down1 = setup("/player/spee_down_1");
    down2 = setup("/player/spee_down_2");
  }


  public void draw(Graphics2D g2) {

    secondStep();
    BufferedImage im = getCurrImage();
    /*
    BufferedImage im = null;
    switch (direction) {
      case DOWN:
        im = down1;
        break;
      case LEFT:
        im = left1;
        break;
      case RIGHT:
        im = right1;
        break;
      case UP:
        im = up1;
        break;

    }
     */
    g2.drawImage(im, screenX, screenY, null);

  }

}

