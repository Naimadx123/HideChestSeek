package zone.vao.hideChestSeek;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import zone.vao.hideChestSeek.classes.HiddenChest;
import zone.vao.hideChestSeek.classes.Region;
import zone.vao.hideChestSeek.utils.ConfigUtil;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.bukkit.scheduler.BukkitRunnable;

public class Game {
  private final HideChestSeek plugin = HideChestSeek.instance;
  private final ConfigUtil configUtil = plugin.getConfigUtil();
  private BukkitTask task;
  @Getter
  public Region region;
  @Getter
  public HiddenChest hiddenChest;
  @Getter
  public final String id = UUID.randomUUID().toString();

  private static final int TICKS_PER_SECOND = 20;
  private static final int MILLISECONDS_PER_SECOND = 1000;
  private final long spawnInterval = (long) (plugin.getConfigUtil().getSpawnIntervalTicks() / TICKS_PER_SECOND) * MILLISECONDS_PER_SECOND;

  private boolean monitoringStarted = false;

  /**
   * @param region Region of game
   */
  public Game(Region region) {
    this.region = region;

    this.createHiddenChest();
  }

  public void createHiddenChest(){
    Location loc = this.getAvailableLocation();
    if(loc == null) {
      HideChestSeek.getInstance().getLogger().warning("Cannot get available location!");
      return;
    }
    Block block = loc.getBlock();
    block.setType(Material.CHEST);

    Chest chest = (Chest) block.getState();
    chest.getPersistentDataContainer().set(
        new NamespacedKey(plugin, "chest"),
        PersistentDataType.STRING, this.id
    );
    chest.update();

    this.hiddenChest = new HiddenChest(chest);
  }

  public void removeHiddenChest(){
    hiddenChest.remove();
  }

  public void destroy() {
    stopMonitor();
    removeHiddenChest();
    plugin.getGames().remove(id);
  }

  public void stopMonitor(){
    if(monitoringStarted){
      monitoringStarted = false;
      if(task != null && !task.isCancelled())
        task.cancel();
    }
  }

  public void startMonitor() {
    if (monitoringStarted || task != null && !task.isCancelled()) return;

    monitoringStarted = true;

    final long lastSpawn = System.currentTimeMillis();
    this.task = new BukkitRunnable() {
      @Override
      public void run() {
        long nextSpawnTime = lastSpawn + spawnInterval;
        if (nextSpawnTime > System.currentTimeMillis()) {
          return;
        }

        createHiddenChest();
        monitoringStarted = false;
        this.cancel();
      }
    }.runTaskTimer(plugin, 0L, 1L);
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
          return location;
        }
      }
      attempts++;
    }

    plugin.getLogger().warning("No suitable location found for the hidden chest after " + maxAttempts + " attempts.");
    return null;
  }
}
