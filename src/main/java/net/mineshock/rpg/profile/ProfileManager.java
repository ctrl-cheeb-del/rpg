package net.mineshock.rpg.profile;

import lombok.Getter;
import net.mineshock.rpg.RPG;
import net.mineshock.rpg.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ProfileManager {
    @Getter
    private final Map<String, Profile> profiles = new HashMap<>();
    private final RPG plugin;

    public ProfileManager(RPG plugin) {
        this.plugin = plugin;
    }

    public void saveProfile(String playerUUID, Player player) {
        List<ItemStack> contents = new ArrayList<>(Arrays.asList(player.getInventory().getContents()));
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                contents.add(item);
            }
        }
        Profile profile = new Profile(player, player.getInventory(), contents, player.getLocation());
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
            List<ItemStack> savedItems = (List<ItemStack>) config.get("playerInventory");
            if (savedItems == null || savedItems.isEmpty()) {
                // Handle the case where the saved inventory is empty or null
                return null;
            }
            Location location = config.getLocation("location");
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(playerUUID));
            if (offlinePlayer.hasPlayedBefore()) {
                return new Profile(offlinePlayer, offlinePlayer.getPlayer().getInventory(), savedItems, location);
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
