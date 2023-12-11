package net.mineshock.rpg;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;

public class Profile {
    private final String playerName;
    private final ItemStack[] inventoryContents;
    private final Location location;

    public Profile(Player player, ItemStack[] inventoryContents, Location location) {
        this.playerName = player.getName();
        this.inventoryContents = inventoryContents;
        this.location = location;
    }

    public String getPlayerName() {
        return playerName;
    }

    public ItemStack[] getInventoryContents() {
        return inventoryContents;
    }

    public Location getLocation() {
        return location;
    }
}