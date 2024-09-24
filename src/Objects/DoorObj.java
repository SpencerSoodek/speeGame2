package Objects;

import static Objects.OBJTypes.DOOR;

import java.io.IOException;
import javax.imageio.ImageIO;

public class DoorObj extends GameObject{
  public DoorObj(int x, int y) {
    this.x = x;
    this.y = y;
    getKeyImage();
    this.collision = true;
    this.objType = DOOR;
  }

  public void getKeyImage() {
    try {
      image = ImageIO.read(getClass().getResourceAsStream("/objects/door.png"));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

}