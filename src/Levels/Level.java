package Levels;

import Entities.Entity;
import Entities.MonsterManager;
import Objects.ObjectManager;
import main.GamePanel;
import tile.Tile;

public class Level {
  public int sizeX;
  public int sizeY;
  public String tilesFilePath;
  public int playerStartX;
  public int playerStartY;
  public GamePanel gp;
  public ObjectManager om;
  public MonsterManager monstM;

  public Tile[] levelTiles(){
    Tile[] tiles = new Tile[40];
    return tiles;
  }


}
