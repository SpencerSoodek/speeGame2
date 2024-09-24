package Entities;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import javax.imageio.ImageIO;
import main.Direction;
import main.GamePanel;
import main.UtilityTool;
import tile.Tile;

public class CreatureMonst extends Entity{

  int pathCounter = 0;
  int stepsToResetPath = 5;
  Direction[] path = new Direction[5];


  public CreatureMonst(GamePanel gp, int x, int y) {
    super(gp);
    this.col = x;
    this.row = y;
    this.worldX = col *gp.tileSize;
    this.worldY = row * gp.tileSize;
    // gp.tileM.occupiedTiles[col][row] = true;
    this.moving = false;
    this.speed = 1;
    this.direction = Direction.DOWN;
    this.stepRate = 60;
    getImage();
  }

  public void getImage() {
    up1 = setup("/monsters/creature/creature_up_1");
    up2 = setup("/monsters/creature/creature_up_2");
    left1 = setup("/monsters/creature/creature_left_1");
    left2 = setup("/monsters/creature/creature_left_2");
    right1 = setup("/monsters/creature/creature_right_1");
    right2 = setup("/monsters/creature/creature_right_2");
    down1 = setup("/monsters/creature/creature_down_1");
    down2 = setup("/monsters/creature/creature_down_2");
  }

  private boolean validTile(int x, int y) {
    return !(x < 0 || x >= gp.worldCols || y < 0 || y >= gp.worldRows);
  }

  public void setAction() {
    boolean visitedTiles[][] = new boolean[gp.worldCols][gp.worldRows];
    Queue<Point> queue = new LinkedList<>();
    Point[][] prev = new Point[gp.worldCols][gp.worldRows];
    queue.add(new Point(col, row)); // Start from the monster's current position
    Point playerPoint = new Point(gp.player.col, gp.player.row);

    while (!queue.isEmpty()) {
      Point p = queue.poll();
      visitedTiles[p.x][p.y] = true;

      // Define adjacent tiles
      Point left = new Point(p.x - 1, p.y);
      Point right = new Point(p.x + 1, p.y);
      Point up = new Point(p.x, p.y - 1);
      Point down = new Point(p.x, p.y + 1);

      if (p.equals(playerPoint)) {
        break;
      }

      // Check adjacent tiles for valid moves (not out of bounds or occupied)
      if (validTile(left.x, left.y) && !visitedTiles[left.x][left.y] && !gp.gameLevel.om.objectAt(left.x, left.y)) {
        queue.add(left);
        prev[left.x][left.y] = p;
      }
      if (validTile(right.x, right.y) && !visitedTiles[right.x][right.y] && !gp.gameLevel.om.objectAt(right.x, right.y)) {
        queue.add(right);
        prev[right.x][right.y] = p;
      }
      if (validTile(up.x, up.y) && !visitedTiles[up.x][up.y] && !gp.gameLevel.om.objectAt(up.x, up.y)) {
        queue.add(up);
        prev[up.x][up.y] = p;
      }
      if (validTile(down.x, down.y) && !visitedTiles[down.x][down.y] && !gp.gameLevel.om.objectAt(down.x, down.y)) {
        queue.add(down);
        prev[down.x][down.y] = p;
      }
    }

    List<Direction> newPath = new ArrayList<>();
    Point step = playerPoint;

    // Trace the path from the player back to the monster
    while (!step.equals(new Point(col, row)) && prev[step.x][step.y] != null) {
      Point previousStep = prev[step.x][step.y];

      // Determine the direction from the previous step to the current step
      if (previousStep.x < step.x) {
        newPath.add(0, Direction.RIGHT);
      } else if (previousStep.x > step.x) {
        newPath.add(0, Direction.LEFT);
      } else if (previousStep.y < step.y) {
        newPath.add(0, Direction.DOWN);
      } else if (previousStep.y > step.y) {
        newPath.add(0, Direction.UP);
      }

      step = previousStep; // Move back along the path
    }

    // Return an array with up to 5 directions
    Direction[] result = new Direction[5];
    for (int i = 0; i < 5 && i < newPath.size(); i++) {
      result[i] = newPath.get(i);
    }

    path = result; // Return the sequence of directions



  }


  public void update() {
    if (moving) {
      switch (direction) {
        case UP:
          if (worldY <= targetY) {
            worldY = targetY;
            moving = false;
            row--;
          } else {
            worldY -= speed;
          }
          break;
        case DOWN:
          if (worldY >= targetY) {
            worldY = targetY;
            moving = false;
            row++;
          } else {
            worldY += speed;
          }
          break;
        case LEFT:
          if (worldX <= targetX) {
            worldX = targetX;
            moving = false;
            col--;
          } else {
            worldX -= speed;
          }
          break;
        case RIGHT:
          if (worldX >= targetX) {
            worldX = targetX;
            moving = false;
            col++;
          } else {
            worldX += speed;
          }
          break;
      }
    }

    if (!moving) {
      if (pathCounter >= this.stepsToResetPath || path[pathCounter] == null) {
        setAction();
        pathCounter = 0;
      } else {
        direction = path[pathCounter];
        pathCounter++;
      }
      // Check for collision after setting direction.
      collisionOn = gp.cChecker.checkCollision(this) || gp.cChecker.objCollision(this) || gp.cChecker.checkOccupied(this);

      // Start moving in the direction if no collision.
      if (!collisionOn) {
        startMoving();
      }
    }
  }
}
