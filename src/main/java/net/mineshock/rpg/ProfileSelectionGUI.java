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

public class ProfileSelectionGUI implements Listener {
    private final ProfileManager profileManager;
    private Inventory inv;

    public ProfileSelectionGUI(ProfileManager profileManager) {
        this.profileManager = profileManager;
        this.inv = Bukkit.createInventory(null, 9, "Select Profile");

        // Add profiles to the inventory
        ItemStack item1 = new ItemStack(Material.DIAMOND, 1);
        ItemMeta meta1 = item1.getItemMeta();
        meta1.setDisplayName("Profile 1");
        item1.setItemMeta(meta1);
        inv.addItem(item1);

        // Add more items for more profiles
    }

    public void openInventory(Player player) {
        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == inv) {
            event.setCancelled(true);

            String profileName = event.getCurrentItem().getItemMeta().getDisplayName();
            Profile profile = profileManager.loadProfile(profileName);

            if (profile != null) {
                Player player = (Player) event.getWhoClicked();
                player.getInventory().setContents(profile.getInventoryContents());
                player.teleport(profile.getLocation());
                player.closeInventory();
            }
        }
    }
}
