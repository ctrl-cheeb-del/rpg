package net.mineshock.rpg.profile;

import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

@Getter
public class Profile {
    private UUID uuid;
    private final String playerName;
    private final List<ItemStack> savedItems;
    private final Location location;
    @Getter
    private final PlayerInventory playerInventory;

    public Profile(OfflinePlayer player, PlayerInventory playerInventory, List<ItemStack> savedItems, Location location) {
        this.uuid = player.getUniqueId();
        this.playerName = player.getName();
        this.playerInventory = playerInventory;
        this.savedItems = savedItems;
        this.location = location;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void applyToPlayer(Player player) {
        player.teleport(location);
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.clear();
        for (int i = 0; i < savedItems.size(); i++) {
            ItemStack item = savedItems.get(i);
            if (item != null) {
                playerInventory.setItem(i, item);
            }
        }
    }

}
