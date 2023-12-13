package net.mineshock.rpg.profile;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.io.IOException;
import java.util.*;

public class ProfileSelectionGUI implements Listener {
    private final ProfileManager profileManager;

    public ProfileSelectionGUI(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public void openInventory(Player player) {
        List<Profile> profiles = profileManager.getProfiles(player.getUniqueId());
        int size = 9 * ((profiles.size() + 8) / 9);
        if (size == 0) {
            size = 9; // default size
        }
        Inventory inv = Bukkit.createInventory(null, size, "Profile Selection");
        if (!profiles.isEmpty()) {
            for (Profile profile : profiles) {
                ItemStack item = new ItemStack(Material.PLAYER_HEAD);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(profile.getProfileId().toString());
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
    public void onInventoryClick(InventoryClickEvent event) throws IOException {

        if (!event.getView().getTitle().equals("Profile Selection")) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();

        ItemStack item = event.getCurrentItem();
        if (item != null) {
            if (item.getType() == Material.PLAYER_HEAD) {

                UUID profileId = UUID.fromString(item.getItemMeta().getDisplayName());

                Profile profile = null;

                for (Profile profileInList : profileManager.getProfiles(player.getUniqueId())) {
                    if (profileInList.getProfileId().equals(profileId)) {
                        profile = profileInList;
                        System.out.println("test 1");
                    }
                    System.out.println(profileId +  "         " + profileInList.getProfileId().toString());
                }

                assert profile != null;
                profileManager.loadProfile(profile);

                player.removePotionEffect(PotionEffectType.BLINDNESS);
                player.closeInventory();

            }
            if (item.getType() == Material.EMERALD_BLOCK) {

                profileManager.createProfile(player.getUniqueId());

                player.removePotionEffect(PotionEffectType.BLINDNESS);
                player.closeInventory();
            }
        }

    }
}
