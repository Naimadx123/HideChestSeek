package zone.vao.hideChestSeek.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Map;

public class ConfigUtil {
  private final FileConfiguration config;

  public ConfigUtil(FileConfiguration config) {
    this.config = config;
  }

  // General settings
  public int getSpawnIntervalTicks() {
    return config.getInt("spawn.interval_ticks", 72000); // Default to 72000 if not set
  }

  // Spawn location settings
  public Location getMinLocation() {
    String worldName = config.getString("location.world", "world");
    World world = Bukkit.getWorld(worldName);
    if (world == null) {
      throw new IllegalArgumentException("World '" + worldName + "' not found!");
    }

    double x = config.getDouble("location.minx", 0);
    double y = config.getDouble("location.miny", 64);
    double z = config.getDouble("location.minz", 0);

    return new Location(world, x, y, z);
  }

  public Location getMaxLocation() {
    String worldName = config.getString("location.world", "world");
    World world = Bukkit.getWorld(worldName);
    if (world == null) {
      throw new IllegalArgumentException("World '" + worldName + "' not found!");
    }

    double x = config.getDouble("location.maxx", 50);
    double y = config.getDouble("location.maxy", 80);
    double z = config.getDouble("location.maxz", 50);

    return new Location(world, x, y, z);
  }

  // Chest configuration
  public List<Integer> getRandomNumberRange() {
    return config.getIntegerList("chest.variables.rand.random_number");
  }

  public List<String> getChestCommands() {
    return config.getStringList("chest.commands");
  }

  // Block restrictions
  public List<String> getBlockedBlocks() {
    return config.getStringList("blocked-blocks");
  }

  // Fireworks settings
  public boolean areFireworksEnabled() {
    return config.getBoolean("fireworks.enabled", true);
  }

  public String getFireworkType() {
    return config.getString("fireworks.type", "BURST");
  }

  public List<String> getFireworkColors() {
    return config.getStringList("fireworks.colors");
  }

  public int getFireworkPower() {
    return config.getInt("fireworks.power", 1);
  }

  // Messages and broadcasts
  public String getFoundMessage() {
    return ChatColor.translateAlternateColorCodes('&', config.getString("messages.found_message", "Congratulations {player}, you found the treasure!"));
  }

  public boolean isBroadcastMessageEnabled() {
    return config.getBoolean("messages.broadcast_message", true);
  }

  public Map<String, List<String>> getClues() {
    return (Map<String, List<String>>) config.getMapList("messages.clues");
  }

  // Helper method to handle placeholders in commands or messages
  public String parsePlaceholders(String input, Map<String, String> placeholders) {
    String result = input;
    for (Map.Entry<String, String> placeholder : placeholders.entrySet()) {
      result = result.replace("{" + placeholder.getKey() + "}", placeholder.getValue());
    }
    return result;
  }
}
