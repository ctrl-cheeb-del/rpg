package net.mineshock.rpg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Location;

public class Profile {
    private final String playerName;
    private final PlayerInventory playerInventory;
    private final Location location;

    public Profile(Player player, PlayerInventory playerInventory, Location location) {
        this.playerName = player.getName();
        this.playerInventory = playerInventory;
        this.location = location;
    }

    public String getPlayerName() {
        return playerName;
    }

    public PlayerInventory getPlayerInventory() {
        return playerInventory;
    }

    public Location getLocation() {
        return location;
    }
}