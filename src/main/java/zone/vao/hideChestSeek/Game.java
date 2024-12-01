package zone.vao.hideChestSeek;

import lombok.Getter;
import zone.vao.hideChestSeek.classes.HiddenChest;
import zone.vao.hideChestSeek.classes.Region;

@Getter
public class Game {
  public Region region;
  public HiddenChest hiddenChest;

  public Game(Region region) {

    this.region = region;
  }

  public void destroy(){
    hiddenChest.remove();
  }
}
