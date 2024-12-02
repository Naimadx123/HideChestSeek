package zone.vao.hideChestSeek.classes;

import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class HiddenChest {

  public final Chest chest;
  public List<ItemStack> items;
  @Setter
  public boolean spawned;

  public HiddenChest(Chest chest) {

    this.chest = chest;
    this.spawned = true;
  }

  public void addItem(ItemStack item) {
    items.add(item);
  }

  public void removeItem(ItemStack item) {
    items.remove(item);
  }

  public void remove(){
    chest.getLocation().getBlock().setType(Material.AIR);
  }
}
