package zone.vao.hideChestSeek.utils;

import org.bukkit.Bukkit;

public class ExpressionFunctions {

  public String toHex(Byte number) {
    return toHex(number.intValue());
  }

  public String toHex(Short number) {
    return toHex(number.intValue());
  }

  public String toHex(Long number) {
    return toHex(number.intValue());
  }

  public String toHex(Float number) {
    return toHex(number.intValue());
  }

  public String toHex(Double number) {
    return toHex(number.intValue());
  }

  // Fallback method
  public String toHex(Object number) {
    Bukkit.getLogger().info("toHex method called with argument: " + number);
    try {
      int intValue = Integer.parseInt(number.toString());
      String hexString = Integer.toHexString(intValue);
      Bukkit.getLogger().info("toHex method returning: " + hexString);
      return hexString;
    } catch (Exception e) {
      Bukkit.getLogger().warning("Exception in toHex: " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }
}

