package net.mineshock.rpg;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileManager {
    @Getter
    private final Map<String, Profile> profiles = new HashMap<>();
    private final RPG plugin;

    public ProfileManager(RPG plugin) {
        this.plugin = plugin;
    }

    public void saveProfile(String playerUUID, Player player) {
        Profile profile = new Profile(player, player.getInventory(), player.getLocation());
        File file = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + playerUUID + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("playerName", profile.getPlayerName());
        config.set("playerInventory", profile.getPlayerInventory().getContents());
        config.set("location", profile.getLocation());
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        profiles.put(playerUUID, profile);
    }

    public Profile loadProfile(String playerUUID) {
        File file = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + playerUUID + ".yml");
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            List<ItemStack> playerInventoryItems = new ArrayList<>();
            ConfigurationSection inventorySection = config.getConfigurationSection("playerInventory");
            if (inventorySection != null) {
                for (String key : inventorySection.getKeys(false)) {
                    playerInventoryItems.add(inventorySection.getItemStack(key));
                }
            }
            Inventory playerInventory = null;
            if (!playerInventoryItems.isEmpty()) {
                playerInventory = Bukkit.createInventory(null, playerInventoryItems.size());
                for (ItemStack item : playerInventoryItems) {
                    playerInventory.addItem(item);
                }
            }
            Location location = config.getLocation("location");
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                return new Profile(player, (PlayerInventory) playerInventory, location);
            }
        } else {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                return new Profile(player, player.getInventory(), player.getLocation());
            }
        }
        return null;
    }

    public Map<String, Profile> getProfiles() {
        File folder = new File(plugin.getDataFolder() + File.separator + "players");
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".yml")) {
                    String playerUUID = file.getName().replace(".yml", "");
                    Profile profile = loadProfile(playerUUID);
                    if (profile != null) {
                        profiles.put(playerUUID, profile);
                    }
                }
            }
        }
        return profiles;
    }
}
