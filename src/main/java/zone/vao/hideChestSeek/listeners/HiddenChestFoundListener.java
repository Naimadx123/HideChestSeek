package zone.vao.hideChestSeek.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import zone.vao.hideChestSeek.Game;
import zone.vao.hideChestSeek.HideChestSeek;
import zone.vao.hideChestSeek.events.HiddenChestFoundEvent;
import zone.vao.hideChestSeek.utils.ConfigUtil;
import zone.vao.hideChestSeek.utils.FireworkUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HiddenChestFoundListener implements Listener {

  private final FireworkUtil fireworkUtil = new FireworkUtil();

  @EventHandler
  public void onHiddenChestFound(HiddenChestFoundEvent event) {
    Game game = event.getGame();
    Player player = event.getPlayer();

    ConfigUtil configUtil = HideChestSeek.instance.getConfigUtil();

    List<String> commands = configUtil.getChestCommands();
    for (String command : commands) {
      String parsedCommand = parseCommand(command, player, configUtil);
      HideChestSeek.instance.getServer()
          .dispatchCommand(HideChestSeek.instance.getServer().getConsoleSender(), parsedCommand);
    }

    game.getHiddenChest().getChest().getBlock().setType(Material.AIR);

    this.fireworkUtil.spawnFirework(game.getHiddenChest().getChest().getBlock().getLocation());

    game.removeHiddenChest();

    if(configUtil.isBroadcastMessageEnabled()){
      Bukkit.getServer().broadcastMessage(parseCommand(configUtil.getFoundMessage(), player, configUtil));
    }
  }

  private String parseCommand(String command, Player player, ConfigUtil configUtil) {
    Map<String, String> placeholders = new HashMap<>();

    placeholders.put("player", player.getName());

    for (String variableName : configUtil.getVariables().keySet()) {
      String value = configUtil.processVariable(variableName);

      if (value.contains(":")) {
        String[] parts = value.split(":");
        if (parts.length == 2) {
          placeholders.put(variableName + ".item", parts[0]);
          placeholders.put(variableName + ".quantity", parts[1]);
        } else {
          placeholders.put(variableName, value);
        }
      } else {
        placeholders.put(variableName, value);
      }
    }

    // Replace placeholders in the command
    return configUtil.parsePlaceholders(command, placeholders);
  }

}
