package tile;

import Entities.Entity;
import Levels.Level;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;

public class TileManager {

  public Tile[] tileTypes;
  GamePanel gp;
  public int mapTiles[][];
  public boolean occupiedTiles[][];

  public TileManager(GamePanel gp, Level level) {
    this.gp = gp;
    tileTypes = new Tile[40];
    mapTiles = new int[level.sizeX][level.sizeY];
    occupiedTiles = new boolean[gp.worldCols][gp.worldRows];
    getTileTypes(level);
    loadMap(level.tilesFilePath);
    setOccupiedTiles();
  }

  public void getTileTypes(Level level) {
    /*
    setup(0, "grass", false);
    setup(1, "wall", true);

    setup(2, "water", true);

    setup(3, "dirt", false);

    setup(4, "tree", true);
    setup(5, "sand", false);*/
    //tileTypes = new Tile[40];
    Tile[] levelTiles = level.levelTiles();
    for (int i = 0; i <levelTiles.length; i++) {
      tileTypes[i] = levelTiles[i];
    }
  }

  public void setup(int index, String imagePath, boolean collision) {
    UtilityTool uTool = new UtilityTool();

    try {
      tileTypes[index] = new Tile();
      tileTypes[index].image = ImageIO
          .read(getClass().getResourceAsStream("/tile/" + imagePath + ".png"));
      tileTypes[index].image = uTool.scaleImage(tileTypes[index].image, gp.tileSize, gp.tileSize);
      tileTypes[index].collision = collision;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void loadMap(String filePath) {
    try {
      InputStream is = getClass().getResourceAsStream(filePath);
      BufferedReader br = new BufferedReader(new InputStreamReader(is));

      // Loop through rows
      for (int i = 0; i < gp.worldRows; i++) {
        String line = br.readLine();  // Read each line (which represents a row)
        String numbers[] = line.split(",");  // Split the line into columns (space-separated)

        // Loop through columns
        for (int j = 0; j < gp.worldCols; j++) {
          mapTiles[j][i] = Integer
              .parseInt(numbers[j]);  // Assign each number to the correct tile in the array
        }
      }

      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setOccupiedTiles() {
    for (Entity m : gp.gameLevel.monstM.monsters) {
      if (m != null) {
        this.occupiedTiles[m.col][m.row] = true;
      }
    }
  }

  public boolean collisionAt(int x, int y) {
    //System.out.println("collisionat");
    return tileTypes[mapTiles[x][y]].collision;
  }


  public void draw(Graphics2D g2) {
    for (int i = 0; i < gp.worldRows; i++) {  // Loop through rows first
      for (int j = 0; j < gp.worldCols; j++) {  // Then loop through columns
        int t = mapTiles[j][i];  // Correct row/column access
        int worldX = j * gp.tileSize;  // j represents columns, which are X
        int worldY = i * gp.tileSize;  // i represents rows, which are Y
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        int tS = gp.tileSize;

        if (screenX > gp.player.screenX - (gp.screenWidth / 2) - tS
            && screenX < gp.player.screenX + (gp.screenWidth / 2) + tS &&
            screenY < gp.player.screenY + (gp.screenHeight / 2) + tS
            && screenY > gp.player.screenY - (gp.screenHeight / 2) - tS) {  // Correct the screen bounds check
          g2.drawImage(tileTypes[t].image, screenX, screenY, null);
        }
      }
    }

  }

  // Check if tile can be walked on by an entity, by whether it exists, is occupied by an object, or is a collidable tile
  public boolean walkableTile(int x, int y) {
    if (x < 0 || x >= gp.worldCols || y < 0 || y >= gp.worldRows) {
      return false;
    }
    if (gp.gameLevel.om.objectAt(x, y)) {
      return false;
    }
    if (collisionAt(x, y)) {
      return false;
    }
    //if (occupiedTiles[x][y]) {
    //  return false;
    //}
    return true;
  }

  public Point randomSpawnablePoint() {
    ArrayList<Point> spawnablePoints = new ArrayList<>();
    for (int x = 0; x < gp.worldCols; x++) {
      for (int y = 0; y < gp.worldCols; y++) {
        if (walkableTile(x, y)) {
          spawnablePoints.add(new Point(x, y));
        }
      }
    }
    Random rand = new Random();
    int pointIndex = rand.nextInt(spawnablePoints.size());
    return spawnablePoints.get(pointIndex);
  }
}
