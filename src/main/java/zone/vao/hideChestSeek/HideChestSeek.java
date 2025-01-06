package zone.vao.hideChestSeek;

import co.aikar.commands.PaperCommandManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import zone.vao.hideChestSeek.classes.Region;
import zone.vao.hideChestSeek.commands.HCSCommand;
import zone.vao.hideChestSeek.listeners.HiddenChestFoundListener;
import zone.vao.hideChestSeek.listeners.PlayerInteractListener;
import zone.vao.hideChestSeek.utils.ConfigUtil;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class HideChestSeek extends JavaPlugin {

  @Getter
  public static HideChestSeek instance;
  public Map<String, Game> games = new HashMap<>();
  public ConfigUtil configUtil;

  @Override
  public void onEnable() {
    // Plugin startup logic
    instance = this;

    saveDefaultConfig();

    this.configUtil = new ConfigUtil(this.getConfig());

    // Load command
    PaperCommandManager commandManager = new PaperCommandManager(this);
    commandManager.registerCommand(new HCSCommand());

    getServer().getPluginManager()
        .registerEvents(new PlayerInteractListener(), this);
    getServer().getPluginManager()
        .registerEvents(new HiddenChestFoundListener(), this);

    this.getLogger().info("HideChestSeek enabled!");

    Game newGame = new Game(new Region(getConfigUtil().getMinLocation(), getConfigUtil().getMaxLocation()));
    getGames().put(newGame.getId(), newGame);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic

    if(!games.isEmpty()) {
      for (Game game : games.values()) {
        game.destroy();
      }
    }

    this.getLogger().info("HideChestSeek disabled!");
  }

  public void reload(){
    this.reloadConfig();
    this.configUtil = new ConfigUtil(this.getConfig());

    if(!games.isEmpty()) {
      for (Game game : games.values()) {
        game.destroy();
      }
    }
    games.clear();
    Game newGame = new Game(new Region(getConfigUtil().getMinLocation(), getConfigUtil().getMaxLocation()));
    getGames().put(newGame.getId(), newGame);
  }
}
