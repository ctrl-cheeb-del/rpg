package net.mineshock.rpg;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Location;

@Getter
public class Profile {
    private final String playerName;
    private final PlayerInventory playerInventory;
    private final Location location;

    public Profile(Player player, PlayerInventory playerInventory, Location location) {
        this.playerName = player.getName();
        this.playerInventory = playerInventory;
        this.location = location;
    }

    public void applyToPlayer(Player player) {
        player.getInventory().setContents(this.playerInventory.getContents());
        player.teleport(this.location);
    }
}
