package net.mineshock.rpg.profile;

import lombok.Getter;
import net.mineshock.rpg.RPG;
import net.mineshock.rpg.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

public class ProfileManager {

    @Getter
    private final Map<Player, Profile> profiles = new HashMap<>();
    private final RPG plugin;

    public ProfileManager(RPG plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets a list of Profiles that a user has in their yml
     * Returns empty list if the file doesn't exist or is empty
     * Used when making GUI
     **/
    public List<Profile> getProfiles(UUID playerUUID) {
        List<Profile> profiles = new ArrayList<>();

        File folder = new File(plugin.getDataFolder() + File.separator + "players");
        if (!folder.exists() || !folder.isDirectory()) return null;

        File playerFile = new File(folder.getAbsolutePath() + File.separator + playerUUID + ".yml");
        if (!playerFile.exists()) return profiles;

        Player player = Bukkit.getPlayer(playerUUID);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        for (String profileUUIDAsString : config.getKeys(false)) {
            UUID profileId = UUID.fromString(profileUUIDAsString);
            ConfigurationSection profileSection = config.getConfigurationSection(profileUUIDAsString);

            List<ItemStack> items = (List<ItemStack>) profileSection.get("inventory");
            ItemStack[] itemStacks = items.toArray(new ItemStack[0]);

            Location playerLocation = profileSection.getLocation("location");

            double exp = profileSection.getDouble("exp");

            assert player != null;
            profiles.add(new Profile(profileId, player, itemStacks, playerLocation, exp));

        }

        return profiles;
    }


    /**
     * Used when a profile is chosen on the GUI
     * Sets the players inventory to the inventory of the profile
     * Teleports the player
     * Updates level of player
     * Puts profile into hashmap
     **/
    public void loadProfile(Profile profile) {

        Player player = (Player) profile.getPlayer();
        List<ItemStack> savedItems = Arrays.asList(profile.getPlayerInventory());
        Location playerLocation = profile.getLocation();
        double exp = profile.getExp();

        PlayerInventory playerInventory = player.getInventory();
        for (int i = 0; i < savedItems.size(); i++) {
            ItemStack item = savedItems.get(i);
            if (item != null) {
                playerInventory.setItem(i, item);
            }
        }

        player.teleport(playerLocation);

        //insert logic for leveling system

        profiles.put(player, profile);

    }


    /**
     * Creates a new profile
     * Creates the yml file, if it doesn't exist
     * Generates random UUID for the profile
     * Assigns current inventory + location and exp of 0
     * Puts profile into hashmap
     **/
    public void createProfile(UUID playerUUID) throws IOException {

        Player player = Bukkit.getPlayer(playerUUID);
        UUID profileId = UUID.randomUUID();

        File file = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + playerUUID + ".yml");
        file.createNewFile();

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        assert player != null;
        config.set(profileId + ".inventory", player.getInventory().getContents());
        config.set(profileId + ".location", player.getLocation());
        config.set(profileId + ".exp", 0.0D);

        config.save(file);

        profiles.put(player, new Profile(profileId, player, player.getInventory().getContents(), player.getLocation(), 0.0D));
    }

    /**
     * Updates profile information in the yml file
     * Takes current inventory and location, updates profile in map
     * Then changes yml
     **/
    public void saveProfile(UUID playerUUID, Profile profile) throws IOException {

        Player player = Bukkit.getPlayer(playerUUID);
        UUID profileId = profile.getProfileId();

        File file = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + playerUUID + ".yml");

        assert player != null;
        profile.setPlayerInventory(player.getInventory().getContents());
        profile.setLocation(player.getLocation());

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set(profileId + ".inventory", profile.getPlayerInventory());
        config.set(profileId + ".location", profile.getLocation());
        config.set(profileId + ".exp", profile.getExp());

        config.save(file);
    }





    //Profile class has Player, ItemStack[], Location, Double exp, UUID profileId - done

    //YAML file for each players UUID - done
    //Different sections for each profile, named the UUID of the section - done
    //Section contains inventory, exp, location sub-section - done

    //Player joins - checks if file for them exists - done
    //If not - displays the create profile icon, which makes a new profile for them (loads this into hashmap) - creates file for them and puts this new profile in it - done
    //If yes - displays all the profiles in their file, which all give items, tp etc (loads this into hashmap) + also the new profile button
    //Player leaves - updates that profile in the file and removes it from the hashmap - done

    //Method to fetch profiles for a player, returns list - done
    //Method to load a chosen profile (puts in hashmap and handles inv etc) - done
    //Method create a new profile - done
    //Method to save a profile - done


}
