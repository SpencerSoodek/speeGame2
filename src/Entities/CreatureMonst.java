package Entities;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import main.Direction;
import main.GamePanel;

public class CreatureMonst extends Entity {

  int pathCounter = 0;
  int stepsToResetPath = 5;
  int noPathCooldown = 60;
  int noPathCooldownCounter = 0;


  public CreatureMonst(GamePanel gp, int x, int y) {
    super(gp);
    this.col = x;
    this.row = y;
    this.worldX = col * gp.tileSize;
    this.worldY = row * gp.tileSize;
    // gp.tileM.occupiedTiles[col][row] = true;
    this.moving = false;
    this.speed = 2;
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


  // prev attempt. check right angle paths then check bfs.
  public void bfsPath() {
    HashSet<Point> visitedTiles = new HashSet<>();
    Queue<Point> queue = new LinkedList<>();
    Point[][] prev = new Point[gp.worldCols][gp.worldRows];
    queue.add(new Point(col, row)); // Start from the monster's current position
    Point playerPoint = new Point(gp.player.col, gp.player.row);

    // Check right angle movement
    int xDiff = playerPoint.x - col;
    int yDiff = playerPoint.y - row;

    boolean xValid = true;
    boolean yValid = true;

    List<Direction> rAPath = new ArrayList<>();

    // Try moving along x-axis
    if (Math.abs(xDiff) > 0) {
      for (int i = 0; i < Math.abs(xDiff); i++) {
        int nextX = col + (xDiff > 0 ? 1 : -1) * (i + 1);
        if (gp.tileM.walkableTile(nextX, row)) {
          rAPath.add(xDiff > 0 ? Direction.RIGHT : Direction.LEFT);
        } else {
          xValid = false;
          break;
        }
      }
    }

    // Try moving along y-axis if x-axis was successful
    if (xValid && Math.abs(yDiff) > 0) {
      for (int i = 0; i < Math.abs(yDiff); i++) {
        int nextY = row + (yDiff > 0 ? 1 : -1) * (i + 1);
        if (gp.tileM.walkableTile(col, nextY)) {
          rAPath.add(yDiff > 0 ? Direction.DOWN : Direction.UP);
        } else {
          yValid = false;
          break;
        }
      }
    }

    // If right-angle path found, return it.
    if (xValid && yValid) {
      path = rAPath.toArray(new Direction[5]);
      return;
    }

    // check y then X
    rAPath.clear();
    xValid = true;
    yValid = true;

    // Try moving along y-axis
    if (Math.abs(yDiff) > 0) {
      for (int i = 0; i < Math.abs(yDiff); i++) {
        int nextY = row + (yDiff > 0 ? 1 : -1) * (i + 1);
        if (gp.tileM.walkableTile(nextY, col)) {
          rAPath.add(yDiff > 0 ? Direction.DOWN : Direction.UP);
        } else {
          yValid = false;
          break;
        }
      }
    }

    // Try moving along x-axis if x-axis was successful
    if (yValid && Math.abs(xDiff) > 0) {
      for (int i = 0; i < Math.abs(xDiff); i++) {
        int nextX = col + (xDiff > 0 ? 1 : -1) * (i + 1);
        if (gp.tileM.walkableTile(nextX, row)) {
          rAPath.add(yDiff > 0 ? Direction.RIGHT : Direction.LEFT);
        } else {
          yValid = false;
          break;
        }
      }
    }

    // If right-angle path found, return it.
    if (xValid && yValid) {
      path = rAPath.toArray(new Direction[5]);
      return;
    }

    while (!queue.isEmpty()) {
      Point p = queue.poll();
      visitedTiles.add(p);

      // Define adjacent tiles
      Point left = new Point(p.x - 1, p.y);
      Point right = new Point(p.x + 1, p.y);
      Point up = new Point(p.x, p.y - 1);
      Point down = new Point(p.x, p.y + 1);

      if (p.equals(playerPoint)) {
        break;
      }

      // Check adjacent tiles for valid moves (not out of bounds or occupied)
      if (gp.tileM.walkableTile(left.x, left.y) && !visitedTiles.contains(left)) {
        queue.add(left);
        prev[left.x][left.y] = p;
      }
      if (gp.tileM.walkableTile(right.x, right.y) && !visitedTiles.contains(right)) {
        queue.add(right);
        prev[right.x][right.y] = p;
      }
      if (gp.tileM.walkableTile(up.x, up.y) && !visitedTiles.contains(up)) {
        queue.add(up);
        prev[up.x][up.y] = p;
      }
      if (gp.tileM.walkableTile(down.x, down.y) && !visitedTiles.contains(down)) {
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

      if (noPath && noPathCooldownCounter >= noPathCooldown) {
        noPath = false;
        noPathCooldownCounter = 0;
      }
      if (noPath && noPathCooldownCounter < noPathCooldown) {
        noPathCooldownCounter++;
      } else {
        if (pathCounter >= this.stepsToResetPath || path[pathCounter] == null) {
          aStarPath();
          pathCounter = 0;
        }
        direction = path[pathCounter];
        pathCounter++;
        // Check for collision after setting direction.
        collisionOn =
            gp.cChecker.checkCollision(this) || gp.cChecker.objCollision(this) || gp.cChecker
                .checkOccupied(this);

        // Start moving in the direction if no collision.
        if (!collisionOn) {
          startMoving();
        }
      }
    }
  }
}
