package zone.vao.hideChestSeek.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import zone.vao.hideChestSeek.Game;
import zone.vao.hideChestSeek.HideChestSeek;
import zone.vao.hideChestSeek.events.HiddenChestFoundEvent;
import zone.vao.hideChestSeek.utils.ConfigUtil;
import zone.vao.hideChestSeek.utils.FireworkUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerInteractListener implements Listener {

  private final HideChestSeek plugin = HideChestSeek.instance;
  private final FireworkUtil fireworkUtil = new FireworkUtil();

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    Block block = event.getClickedBlock();

    if(block == null || block.getType() != Material.CHEST) return;

    Chest chest = (Chest) block.getState();

    boolean isGameChest = chest.getPersistentDataContainer().has(
            new NamespacedKey(plugin, "chest"),
            PersistentDataType.STRING
            );

    if(!isGameChest) {
      player.sendMessage("this is not a game chest");
      return;
    }
    event.setCancelled(true);

    String gameId = chest.getPersistentDataContainer().get(
        new NamespacedKey(plugin, "chest"),
        PersistentDataType.STRING
    );

    ConfigUtil configUtil = HideChestSeek.instance.getConfigUtil();

    List<String> commands = configUtil.getChestCommands();
    for (String command : commands) {
      String parsedCommand = parseCommand(command, player, configUtil);
      HideChestSeek.instance.getServer()
          .dispatchCommand(HideChestSeek.instance.getServer().getConsoleSender(), parsedCommand);
    }

    chest.getBlock().setType(Material.AIR);

    Game game = HideChestSeek.instance.getGames().get(gameId);

    HiddenChestFoundEvent chestFoundEvent = new HiddenChestFoundEvent(player, game);
    Bukkit.getServer().getPluginManager().callEvent(chestFoundEvent);

    this.fireworkUtil.spawnFirework(chest.getBlock().getLocation());

    game.destroy();
  }

  private String parseCommand(String command, Player player, ConfigUtil configUtil) {
    Map<String, String> placeholders = new HashMap<>();

    placeholders.put("player", player.getName());

    for (String variableName : configUtil.getVariables().keySet()) {
      String value = configUtil.processVariable(variableName);
      placeholders.put(variableName, value);
    }

    return configUtil.parsePlaceholders(command, placeholders);
  }
}
