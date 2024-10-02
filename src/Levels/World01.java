package Levels;

import Entities.CreatureMonst;
import Entities.MonsterManager;
import Objects.DoorObj;
import Objects.KeyObj;
import Objects.ObjectManager;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.GamePanel;
import main.UtilityTool;
import tile.Tile;

public class World01 extends Level{
  public World01(GamePanel gp) {
    sizeX = 30;
    sizeY = 30;
    tilesFilePath = "/maps/world-01.txt";
    playerStartX = 10;
    playerStartY = 13;
    this.gp = gp;

    this.om = new ObjectManager(gp);
    om.objects[1] = new KeyObj(14, 14);
    om.objects[2] = new DoorObj(10, 10);
    om.objects[3] = new DoorObj(10, 7);
    this.monstM = new MonsterManager(gp);
    monstM.monsters[0] = new CreatureMonst(gp, 10, 17);
    monstM.monsters[1] = new CreatureMonst(gp, 12, 19);

  }

  public Tile[] levelTiles() {
    Tile[] tiles = new Tile[40];
    // grass
    tiles[0] = getTile("world1/world1_grass", false);
    // water
    tiles[1] = getTile("world1/world1_water", true);
    // water with top left corner grass border
    tiles[2] = getTile( "world1/world1_waterTLC", true);
    // water with top grass border
    tiles[3] = getTile("world1/world1_waterTOP", true);
    // water with left grass border
    tiles[4] = getTile("world1/world1_water_left", true);
    // water with bottom right notch grass
    tiles[5] = getTile("world1/world1_water_BRN", true);
    // water with bottom left notch sand
    tiles[6] = getTile("world1/world1_water_BLNSand", true);
    // water with bottom left corner sand border
    tiles[7] = getTile("world1/world1_water_BLCSAND", true);
    // water with bottom sand border
    tiles[8] = getTile("world1/world1_water_bottom", true);
    // water with bottom right corner grass border
    tiles[9] = getTile("world1/world1_waterBRC", true);
    // water with right grass border
    tiles[10] = getTile( "world1/world1_water_right", true);
    tiles[11] = getTile( "world1/world1_water_TRC", true);
    tiles[12] = getTile( "world1/world1_water_TRN", true);
    tiles[13] = getTile( "world1/world1_water_TLN", true);
    tiles[14] = getTile( "world1/world1_dirt", false);
    tiles[15] = getTile( "world1/world1_grassLD", false);
    tiles[16] = getTile( "world1/world1_grass_BD", false);
    tiles[17] = getTile( "world1/world1_grassBLND", false);
    tiles[18] = getTile( "world1/world1_grass_BRND", false);
    tiles[19] = getTile( "world1/world1_grass_RD", false);
    tiles[20] = getTile( "world1/world1_grass_TD", false);
    tiles[21] = getTile( "world1/world1_grassTRDN", false);
    tiles[22] = getTile( "world1/world1_grass_TLDN", false);
    tiles[23] = getTile( "world1/world1_grass_TRDC", false);
    tiles[24] = getTile( "world1/world1_grassBRDC", false);
    tiles[25] = getTile( "world1/world1_grassTLDC", false);
    tiles[26] = getTile( "world1/world1_tree", true);
    tiles[27] = getTile( "world1/world1_sand", false);
    tiles[28] = getTile( "world1/world1_waterTRCSand", true);
    tiles[29] = getTile( "world1/world1_water_RSand", true);
    tiles[30] = getTile( "world1/world1_water_TRNSand", true);
    // there are 2 TRC Sands for some reason :/ gp.tileM.setup(31, "world1/world1_waterTRCSand")
    tiles[32] = getTile("world1/world1_water_TSand", true);
    tiles[33] = getTile("world1/world1_water_TLNSand", true);
    tiles[34] = getTile("world1/world1_water_LSand", true);
    tiles[35] = getTile("world1/world1_water_TLCSand", true);
    tiles[36] = getTile("world1/world1_grass_BSand", false);
    tiles[37] = getTile("world1/world1_waterBLC", true);
    tiles[38] = getTile("world1/world1_water_BLN", true);
    return tiles;
  }

  public Tile getTile(String imagePath, boolean collision) {
    UtilityTool uTool = new UtilityTool();
    Tile t= new Tile();

    try {
      t.image = ImageIO.read(getClass().getResourceAsStream("/tile/" + imagePath + ".png"));
      t.image = uTool.scaleImage(t.image, gp.tileSize, gp.tileSize);
      t.collision = collision;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return t;
  }


}
