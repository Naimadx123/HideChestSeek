package zone.vao.hideChestSeek.utils;

import org.bukkit.Bukkit;
import java.util.Random;

public class ExpressionFunctions {

  public Integer random() {
    int randomNumber = new Random().nextInt(Integer.MAX_VALUE);
    Bukkit.getLogger().info("ExpressionFunctions.random() called, returning: " + randomNumber);
    return randomNumber;
  }

  public String toHex(Integer number) {
    String hexString = Integer.toHexString(number);
    Bukkit.getLogger().info("ExpressionFunctions.toHex(" + number + ") called, returning: " + hexString);
    return hexString;
  }
}
