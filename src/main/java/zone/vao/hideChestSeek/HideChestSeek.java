package zone.vao.hideChestSeek;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import zone.vao.hideChestSeek.classes.Region;
import zone.vao.hideChestSeek.listeners.PlayerInteractListener;
import zone.vao.hideChestSeek.utils.ConfigUtil;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class HideChestSeek extends JavaPlugin {

  public static HideChestSeek instance;
  public List<Game> games = new ArrayList<Game>();
  public ConfigUtil configUtil;

  @Override
  public void onEnable() {
    // Plugin startup logic
    instance = this;

    saveDefaultConfig();

    this.configUtil = new ConfigUtil(this.getConfig());

    Region region = new Region(configUtil.getMinLocation(), configUtil.getMaxLocation());
    Game game = new Game(region);

    getServer().getPluginManager()
        .registerEvents(new PlayerInteractListener(), this);

    this.getLogger().info("HideChestSeek enabled!");
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic

    if(!games.isEmpty()) {
      for (Game game : games) {
        game.destroy();
      }
    }

    this.getLogger().info("HideChestSeek disabled!");
  }
}
