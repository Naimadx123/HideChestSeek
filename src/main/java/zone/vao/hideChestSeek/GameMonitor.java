package zone.vao.hideChestSeek;

import lombok.Getter;
import org.bukkit.scheduler.BukkitTask;

public class GameMonitor {

  Game game;
  private BukkitTask monitoringTask;

  public GameMonitor(Game game) {
    this.game = game;
  }

  public void startMonitor() {

  }

}
