package net.mineshock.rpg;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public class ProfileSelectionGUI implements Listener {
    private final ProfileManager profileManager;

    public ProfileSelectionGUI(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public void openInventory(Player player) {
        System.out.println("opening inventory for player" + player.getName());
        Map<String, Profile> profiles = profileManager.getProfiles();
        int size = 9 * ((profiles.size() + 8) / 9);
        if (size == 0) {
            size = 9; // default size
        }
        Inventory inv = Bukkit.createInventory(null, size, "Profile Selection");
        if (!profiles.isEmpty()) {
            for (Map.Entry<String, Profile> entry : profiles.entrySet()) {
                ItemStack item = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(entry.getKey());
                item.setItemMeta(meta);
                inv.addItem(item);
            }
        } else {
            // Add a default item if there are no profiles
            ItemStack item = new ItemStack(Material.EMERALD_BLOCK);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("Create Profile");
            item.setItemMeta(meta);
            inv.addItem(item);
        }
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof ProfileSelectionGUI) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item != null && item.getType() == Material.PLAYER_HEAD) {
                String profileUUID = item.getItemMeta().getDisplayName();
                Profile profile = profileManager.loadProfile(profileUUID);
                if (profile != null) {
                    Player player = (Player) event.getWhoClicked();
                    profile.applyToPlayer(player);
                }
            }
            if (item != null && item.getType() == Material.EMERALD_BLOCK) {
                // Code to create a new profile goes here
            }
        }
    }
}
