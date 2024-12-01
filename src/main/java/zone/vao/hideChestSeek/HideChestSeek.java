package zone.vao.hideChestSeek;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class HideChestSeek extends JavaPlugin {

  public static HideChestSeek instance;
  public List<Game> games;

  @Override
  public void onEnable() {
    // Plugin startup logic
    instance = this;

    this.getLogger().info("HideChestSeek enabled!");
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic

    for (Game game : games) {
      game.destroy();
    }

    this.getLogger().info("HideChestSeek disabled!");
  }
}
