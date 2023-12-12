package net.mineshock.rpg;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

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
        for (ItemStack item : inv.getContents()) {
            System.out.println("Inventory item: " + item);
        }
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        System.out.println("Inventory click event triggered");
        if (event.getView().getTitle().equals("Profile Selection")) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item != null) {
                System.out.println("Clicked item type: " + item.getType());
            }
            if (item != null && item.getType() == Material.PLAYER_HEAD) {
                System.out.println("profile clicked");
                String profileUUID = item.getItemMeta().getDisplayName();
                System.out.println("Clicked profile UUID: " + profileUUID);
                Profile profile = profileManager.loadProfile(profileUUID);
                if (profile != null) {
                    System.out.println("Loaded profile UUID: " + profile.getUUID());
                    Player player = (Player) event.getWhoClicked();
                    profile.applyToPlayer(player);
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    player.closeInventory();
                }
            }
            if (item != null && item.getType() == Material.EMERALD_BLOCK) {
                System.out.println("create profile clicked");
                String playerUUID = event.getWhoClicked().getUniqueId().toString();
                Player player = (Player) event.getWhoClicked();
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                Profile profile = new Profile(player, player.getInventory(), player.getLocation());
                profileManager.saveProfile(playerUUID, player);
                player.closeInventory();
            }
        }
    }




}
