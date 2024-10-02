package Entities;

import java.awt.Color;
import java.awt.Font;
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

  // Position
  public int row;
  public int col;
  public int worldX;
  public int worldY;
  public int screenX;
  public int screenY;
  int speed;
  GamePanel gp;
  public Direction direction;
  public boolean betweenTiles;

  EntityState state;

  // Moving
  boolean collisionOn;
  int targetX;
  int targetY;

  // Health
  public int maxHealth;
  public int health;
  boolean displayHealth;
  int displayHealthFrames = 60;
  int displayHealthCounter;

  // Attacking
  public int attackCooldown;
  public int attackCooldownCounter;

  // Image
  public BufferedImage up1, up2, left1, left2, right1, right2, down1, down2, attackup, attackdown, attackleft, attackright;

  // For shifting between steps when walking.
  int stepCounter = 0;
  boolean step2 = false;
  int stepRate = 60;

  // Checking path
  boolean noPath = false;
  Direction[] path = new Direction[5];
  int pathCounter = 0;
  int stepsToResetPath = 5;
  int noPathCooldown = 60;
  int noPathCooldownCounter = 0;

  public Entity(GamePanel gp) {
    this.gp = gp;
  }

  public void setAction() {
  }

  public void update() {
  }

  public void secondStep() {
    if (this.state == EntityState.MOVE) {
      stepCounter++;
      if (stepCounter > stepRate) {
        stepCounter -= stepRate;
        step2 = !step2;
      }
    }
  }

  public BufferedImage getCurrImage() {
    BufferedImage im = null;
    if (this.state == EntityState.ATTACK && attackCooldownCounter < attackCooldown / 2) {
      switch (direction) {
        case UP:
          im = attackup;
          break;
        case LEFT:
          im = attackleft;
          break;
        case DOWN:
          im = attackdown;
          break;
        case RIGHT:
          im = attackright;
          break;
      }
    } else {
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
        default:
          im = down1;
      }
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
    int tS = gp.tileSize;
    if (screenX > gp.player.screenX - (gp.screenWidth / 2) - tS
        && screenX < gp.player.screenX + (gp.screenWidth / 2) + tS &&
        screenY < gp.player.screenY + (gp.screenHeight / 2) + tS
        && screenY > gp.player.screenY - (gp.screenHeight / 2) - tS) {
      g2.drawImage(im, screenX, screenY, null);
    }
  }

  public void drawHealthBar(Graphics2D g2) {
    if (displayHealth) {
      int screenX = worldX - gp.player.worldX + gp.player.screenX;
      int screenY = worldY - gp.player.worldY + gp.player.screenY;
      Font arial_20 = new Font("arial", Font.PLAIN, 20);
      g2.setFont(arial_20);
      g2.setColor(Color.RED);
      g2.drawString(health + "/" + maxHealth, screenX, screenY - 10);
    }
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
    switch (direction) {
      case UP:
        this.state = EntityState.MOVE;
        targetX = worldX;
        targetY = worldY - gp.tileSize;
        betweenTiles = true;
        gp.tileM.occupiedTiles[col][row - 1] = true;
        break;
      case DOWN:
        this.state = EntityState.MOVE;
        targetX = worldX;
        targetY = worldY + gp.tileSize;
        betweenTiles = true;
        gp.tileM.occupiedTiles[col][row + 1] = true;
        break;
      case LEFT:
        this.state = EntityState.MOVE;
        targetX = worldX - gp.tileSize;
        targetY = worldY;
        betweenTiles = true;
        gp.tileM.occupiedTiles[col - 1][row] = true;
        break;
      case RIGHT:
        this.state = EntityState.MOVE;
        targetX = worldX + gp.tileSize;
        targetY = worldY;
        betweenTiles = true;
        gp.tileM.occupiedTiles[col + 1][row] = true;
        break;
    }
  }

  // If currently moving to a tile, keep moving in same direction until reaching target tile.
  public void keepMovingInDirection() {
    switch (direction) {
      case UP:
        if (worldY <= targetY) {
          worldY = targetY;
          state = EntityState.IDLE;
          row--;
          betweenTiles = false;
          gp.tileM.occupiedTiles[col][row + 1] = false;
          onReachedTile();
        } else {
          worldY -= speed;
        }
        break;
      case DOWN:
        if (worldY >= targetY) {
          state = EntityState.IDLE;
          worldY = targetY;
          row++;
          betweenTiles = false;
          gp.tileM.occupiedTiles[col][row - 1] = false;
          onReachedTile();
        } else {
          worldY += speed;
        }
        break;
      case LEFT:
        if (worldX <= targetX) {
          state = EntityState.IDLE;
          worldX = targetX;
          col--;
          betweenTiles = false;
          gp.tileM.occupiedTiles[col + 1][row] = false;
          onReachedTile();
        } else {
          worldX -= speed;
        }
        break;
      case RIGHT:
        if (worldX >= targetX) {
          state = EntityState.IDLE;
          worldX = targetX;
          col++;
          betweenTiles = false;
          gp.tileM.occupiedTiles[col - 1][row] = false;
          onReachedTile();
        } else {
          worldX += speed;
        }
        break;
    }
  }

  // called once moving entity reaches a tile.
  public void onReachedTile() {}

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
        //System.out.print("No path");
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
    //System.out.print("path found");
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

  public boolean nextToPlayer() {
    int playerX = gp.player.col;
    int playerY = gp.player.row;
    int xDiff = Math.abs(playerX - col);
    int yDiff = Math.abs(playerY - row);

    return (xDiff == 0 && yDiff == 1) || (xDiff == 1 && yDiff == 0);
  }
}



