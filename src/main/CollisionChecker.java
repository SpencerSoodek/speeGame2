package main;

import Entities.Entity;
import Objects.GameObject;
import Objects.OBJTypes;
import tile.Tile;

public class CollisionChecker {

  GamePanel gp;

  CollisionChecker(GamePanel gp) {
    this.gp = gp;
  }

  public boolean checkCollision(Entity e) {
    int entityX = e.col;
    int entityY = e.row;
    int tileNum = 0;
    switch (e.direction) {
      case UP:
        tileNum = gp.tileM.mapTiles[entityX][entityY - 1];
        break;
      case DOWN:
        tileNum = gp.tileM.mapTiles[entityX][entityY + 1];
        break;
      case LEFT:
        tileNum = gp.tileM.mapTiles[entityX - 1][entityY];
        break;
      case RIGHT:
        tileNum = gp.tileM.mapTiles[entityX + 1][entityY];
        break;
    }
    Tile t = gp.tileM.tileTypes[tileNum];
    return gp.tileM.tileTypes[tileNum].collision;
  }

  public boolean objCollision(Entity e) {
    int objX = e.col;
    int objY = e.row;
    switch (e.direction) {
      case UP:
        objY--;
        break;
      case LEFT:
        objX--;
        break;
      case DOWN:
        objY++;
        break;
      case RIGHT:
        objX++;
        break;
    }
    for (GameObject o : gp.gameLevel.om.objects) {
      if (o != null) {
        if (o.x == objX && o.y == objY) {
          if (o.objType == OBJTypes.DOOR) {
            if (gp.player.keys > 0) {
              o.collision = false;
              gp.player.keys--;
            }
          }
          return o.collision;
        }
      }
    }
    return false;
  }

  public boolean checkOccupied(Entity e) {
    int eX = e.col;
    int eY = e.row;
    switch (e.direction) {
      case UP:
        eY--;
        break;
      case LEFT:
        eX--;
        break;
      case DOWN:
        eY++;
        break;
      case RIGHT:
        eX++;
        break;
    }
    return gp.tileM.occupiedTiles[eX][eY];
  }

}
