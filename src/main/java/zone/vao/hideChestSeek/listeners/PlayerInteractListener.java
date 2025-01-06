package zone.vao.hideChestSeek.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;
import zone.vao.hideChestSeek.Game;
import zone.vao.hideChestSeek.HideChestSeek;
import zone.vao.hideChestSeek.classes.Region;
import zone.vao.hideChestSeek.events.HiddenChestFoundEvent;

public class PlayerInteractListener implements Listener {

  private final HideChestSeek plugin = HideChestSeek.instance;

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    Block block = event.getClickedBlock();

    if(block == null || block.getType() != Material.CHEST
        || event.getHand() != EquipmentSlot.HAND
        || player.getGameMode() == GameMode.CREATIVE
            && (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))
    ) return;

    Chest chest = (Chest) block.getState();

    if(Region.getGameId(block.getLocation()) == null
//        || player.getGameMode().equals(GameMode.CREATIVE)
    ) return;

    String gameId = chest.getPersistentDataContainer().get(
        new NamespacedKey(plugin, "chest"),
        PersistentDataType.STRING
    );

    event.setCancelled(true);
    if(gameId == null
        && Region.getGameId(block.getLocation()) != null
    ) {
      player.sendMessage("this is not a game chest");
      return;
    }

    Game game = HideChestSeek.instance.getGames().get(gameId);
    game.startMonitor();
    HiddenChestFoundEvent chestFoundEvent = new HiddenChestFoundEvent(player, game);
    Bukkit.getServer().getPluginManager().callEvent(chestFoundEvent);

  }
}
