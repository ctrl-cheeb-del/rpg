package net.mineshock.rpg.profile;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

@Getter
public class Profile {

    private final UUID profileId;
    private final OfflinePlayer player;
    @Setter
    private ItemStack[] playerInventory;
    @Setter
    private Location location;
    @Setter
    private  double exp;

    public Profile(UUID profileId, OfflinePlayer player, ItemStack[] playerInventory, Location location, double exp) {
        this.profileId = profileId;
        this.player = player;
        this.playerInventory = playerInventory;
        this.location = location;
        this.exp = exp;
    }


//    public void applyToPlayer(Player player) {
//        player.teleport(location);
//        PlayerInventory playerInventory = player.getInventory();
//        playerInventory.clear();
//        for (int i = 0; i < savedItems.size(); i++) {
//            ItemStack item = savedItems.get(i);
//            if (item != null) {
//                playerInventory.setItem(i, item);
//            }
//        }
//    }

}
