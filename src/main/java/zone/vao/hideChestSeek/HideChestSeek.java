package zone.vao.hideChestSeek;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import zone.vao.hideChestSeek.classes.Region;
import zone.vao.hideChestSeek.listeners.PlayerInteractListener;
import zone.vao.hideChestSeek.utils.ConfigUtil;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class HideChestSeek extends JavaPlugin {

  public static HideChestSeek instance;
  public Map<String, Game> games = new HashMap<>();
  public ConfigUtil configUtil;
  public GameMonitor gameMonitor;

  @Override
  public void onEnable() {
    // Plugin startup logic
    instance = this;

    saveDefaultConfig();

    this.configUtil = new ConfigUtil(this.getConfig());

    this.gameMonitor = new GameMonitor();

    getServer().getPluginManager()
        .registerEvents(new PlayerInteractListener(), this);

    this.getLogger().info("HideChestSeek enabled!");
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    this.gameMonitor.stopMonitor();

    if(!games.isEmpty()) {
      for (Game game : games.values()) {
        game.destroy();
      }
    }

    this.getLogger().info("HideChestSeek disabled!");
  }
}
