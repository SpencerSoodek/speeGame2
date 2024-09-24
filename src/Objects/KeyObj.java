package Objects;

import java.io.IOException;
import javax.imageio.ImageIO;

public class KeyObj extends GameObject {
  public KeyObj(int x, int y) {
    this.x = x;
    this.y = y;
    getKeyImage();
    interactable = true;
    this.objType = OBJTypes.KEY;
  }

  public void getKeyImage() {
    try {
      image = ImageIO.read(getClass().getResourceAsStream("/objects/key.png"));
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void interact() {

  }

}
