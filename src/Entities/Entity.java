package Entities;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
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

  boolean noPath = false;
  Direction[] path = new Direction[5];

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

  public void aStarPath() {
    Point start = new Point(col, row);
    Point end = new Point(gp.player.col, gp.player.row);
    PriorityQueue<PathNode> openSet = new PriorityQueue<>((a, b) -> a.cost - b.cost);
    HashSet<Point> closedSet = new HashSet<>();
    HashMap<Point, Point> cameFrom = new HashMap<>();
    openSet.add(new PathNode(start, 0, pathHeuristic(start, end)));

    while (!openSet.isEmpty()) {
      PathNode currNode = openSet.poll();
      if (currNode.point.equals(end)) {
        // return path
        break;
      }

      closedSet.add(currNode.point);

      Point[] neighbors = new Point[4];
      neighbors[0] = new Point(currNode.point.x - 1, currNode.point.y);
      neighbors[1] = new Point(currNode.point.x + 1, currNode.point.y);
      neighbors[2] = new Point(currNode.point.x, currNode.point.y - 1);
      neighbors[3] = new Point(currNode.point.x, currNode.point.y + 1);

      int gscore = currNode.gScore + 1;

      for (Point p : neighbors) {
        if (gp.tileM.walkableTile(p.x, p.y) && !closedSet.contains(p)) {
          boolean inOpenSet = openSet.stream().anyMatch(a -> a.point.equals(p));
          if (!inOpenSet || gscore < getGScore(p, cameFrom)) {
            if (!cameFrom.containsKey(p) || !cameFrom.get(p).equals(currNode.point)) {
              cameFrom.put(p, currNode.point);
            }
            int hScore = pathHeuristic(p, end);
            if (!inOpenSet) {
              openSet.add(new PathNode(p, gscore, gscore + hScore));
            }
          }
        }
      }
    }
    List<Direction> pathAL = new ArrayList<>();
    Point currPoint = end;
    while (!currPoint.equals(start)) {
      if (!cameFrom.containsKey(currPoint)) {
        //no path;
        noPath = true;
        return;
      }
      Point prev = cameFrom.get(currPoint);
      pathAL.add(0, getDirection(currPoint, prev));
      currPoint = prev;
    }

    Direction[] newPath = new Direction[5];
    for (int i = 0; i < 5; i++) {
      if (i < pathAL.size()) {
        newPath[i] = pathAL.get(i);
      }
    }
    this.path = newPath;
  }

  private int getGScore(Point p, HashMap<Point, Point> cameFrom) {
    int gscore = 0;
    Point current = p;
    while (cameFrom.containsKey(current)) {
      gscore++;
      current = cameFrom.get(current);
    }
    return gscore;
  }

  private int pathHeuristic(Point start, Point end) {
    return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
  }

  private Direction getDirection(Point from, Point to) {
    if (to.x > from.x) {
      return Direction.LEFT;
    }
    if (to.x < from.x) {
      return Direction.RIGHT;
    }
    if (to.y > from.y) {
      return Direction.UP;
    }
    return Direction.DOWN;
  }
}



