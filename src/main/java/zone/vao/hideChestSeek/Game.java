package zone.vao.hideChestSeek;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.persistence.PersistentDataType;
import zone.vao.hideChestSeek.classes.HiddenChest;
import zone.vao.hideChestSeek.classes.Region;
import zone.vao.hideChestSeek.utils.ConfigUtil;

import java.util.List;
import java.util.Random;

@Getter
public class Game {
  public Region region;
  public HiddenChest hiddenChest;
  private final HideChestSeek plugin = HideChestSeek.instance;
  private final ConfigUtil configUtil = plugin.getConfigUtil();

  public Game(Region region) {

    this.region = region;

    Location loc = this.getAvailableLocation();
    Block block = loc.getBlock();
    block.setType(Material.CHEST);

    Chest chest = (Chest) block.getState();
    chest.getPersistentDataContainer().set(
        new NamespacedKey(plugin, "chest"),
        PersistentDataType.BOOLEAN, true
    );
    chest.update();

  }

  public void destroy(){
    hiddenChest.remove();
  }

  private Location getAvailableLocation() {
    World world = region.getMaxLocation().getWorld();
    if (world == null) {
      plugin.getLogger().severe("World not found!");
      return null;
    }

    int minX = Math.min(region.getMinLocation().getBlockX(), region.getMaxLocation().getBlockX());
    int minY = Math.min(region.getMinLocation().getBlockY(), region.getMaxLocation().getBlockY());
    int minZ = Math.min(region.getMinLocation().getBlockZ(), region.getMaxLocation().getBlockZ());
    int maxX = Math.max(region.getMinLocation().getBlockX(), region.getMaxLocation().getBlockX());
    int maxY = Math.max(region.getMinLocation().getBlockY(), region.getMaxLocation().getBlockY());
    int maxZ = Math.max(region.getMinLocation().getBlockZ(), region.getMaxLocation().getBlockZ());

    List<String> blockedBlocks = configUtil.getBlockedBlocks();

    Random random = new Random();

    int attempts = 0;
    int maxAttempts = 100;

    while (attempts < maxAttempts) {
      int x = minX + random.nextInt(maxX - minX + 1);
      int y = minY + random.nextInt(maxY - minY + 1);
      int z = minZ + random.nextInt(maxZ - minZ + 1);

      Location location = new Location(world, x + 0.5, y, z + 0.5);

      Block block = location.getBlock();
      Material blockType = block.getType();

      if (blockType == Material.AIR || block.isPassable()) {
        Block blockBelow = location.clone().add(0, -1, 0).getBlock();
        Material blockBelowType = blockBelow.getType();
        if (blockBelowType.isSolid() && !blockedBlocks.contains(blockBelowType.name())) {
          // Location is acceptable
          return location;
        }
      }
      attempts++;
    }

    plugin.getLogger().warning("No suitable location found for the hidden chest after " + maxAttempts + " attempts.");
    return null;
  }


}
