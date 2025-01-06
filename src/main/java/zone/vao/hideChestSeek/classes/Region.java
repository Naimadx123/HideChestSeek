package zone.vao.hideChestSeek.classes;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import zone.vao.hideChestSeek.Game;
import zone.vao.hideChestSeek.HideChestSeek;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class Region {
  public World world;
  public int minx;
  public int miny;
  public int minz;
  public int maxx;
  public int maxy;
  public int maxz;
  public List<Block> blocks;

  public Region(Location loc1, Location loc2) {
    this.world = loc1.getWorld();
    this.minx = Math.min(loc1.getBlockX(), loc2.getBlockX());
    this.miny = Math.min(loc1.getBlockY(), loc2.getBlockY());
    this.minz = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
    this.maxx = Math.max(loc1.getBlockX(), loc2.getBlockX());
    this.maxy = Math.max(loc1.getBlockY(), loc2.getBlockY());
    this.maxz = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
  }

  public Location getMaxLocation(){
    return new Location(world, maxx, maxy, maxz);
  }

  public Location getMinLocation(){
    return new Location(world, minx, miny, minz);
  }


  public boolean isInside(Location loc) {
    if (loc == null || loc.getWorld() == null) {
      return false;
    }

    if (!loc.getWorld().equals(world)) {
      return false;
    }

    int x = loc.getBlockX();
    int y = loc.getBlockY();
    int z = loc.getBlockZ();

    return loc.getBlockX() >= minx && loc.getBlockX() <= maxx &&
        loc.getBlockY() >= miny && loc.getBlockY() <= maxy &&
        loc.getBlockZ() >= minz && loc.getBlockZ() <= maxz;
  }

  public List<Block> loadBlocks(){
    blocks = new ArrayList<Block>();
    for (int x = minx; x <= maxx; x++) {
      for (int y = miny; y <= maxy; y++) {
        for (int z = minz; z <= maxz; z++) {
          Block block = world.getBlockAt(x, y, z);
          blocks.add(block);
        }
      }
    }
    return blocks;
  }

  public static String getGameId(Location loc) {

    String result = null;
    for (Map.Entry<String, Game> entry : HideChestSeek.getInstance().getGames().entrySet()) {
      Game game = entry.getValue();
      String key = entry.getKey();

      if(game.getRegion().isInside(loc)) {
        result = key;
      }
    }


    return result;
  }
}
