package zone.vao.hideChestSeek.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import zone.vao.hideChestSeek.Game;

@Getter
public class HiddenChestFoundEvent extends Event {

  private static final HandlerList handlers = new HandlerList();

  private final Player player;
  private final Game game;

  public HiddenChestFoundEvent(Player player, Game game) {
    this.player = player;
    this.game = game;
  }

  @Override
  @NotNull
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
