package Levels;

import Entities.CreatureMonst;
import Entities.MonsterManager;
import Objects.DoorObj;
import Objects.KeyObj;
import Objects.ObjectManager;
import main.GamePanel;

public class World01 extends Level{
  public World01(GamePanel gp) {
    sizeX = 50;
    sizeY = 50;
    tilesFilePath = "/maps/world01.txt";
    playerStartX = 10;
    playerStartY = 13;
    this.gp = gp;

    this.om = new ObjectManager(gp);
    om.objects[1] = new KeyObj(14, 14);
    om.objects[2] = new DoorObj(10, 10);
    om.objects[3] = new DoorObj(10, 7);
    this.monstM = new MonsterManager(gp);
    monstM.monsters[0] = new CreatureMonst(gp, 10, 17);

  }


}
