package net.mineshock.rpg;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Location;
import java.util.UUID;


@Getter
public class Profile {
    private UUID uuid;
    private final String playerName;
    private final PlayerInventory playerInventory;
    private final Location location;

    public Profile(Player player, PlayerInventory playerInventory, Location location) {
        this.playerName = player.getName();
        this.playerInventory = playerInventory;
        this.location = location;
    }

    public void applyToPlayer(Player player) {
        System.out.println("Applying profile to player");
        player.getInventory().setContents(this.playerInventory.getContents());
        player.teleport(this.location);
    }

    public UUID getUUID() {
        return uuid;
    }
}
