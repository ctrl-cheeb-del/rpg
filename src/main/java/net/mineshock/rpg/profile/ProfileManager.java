package net.mineshock.rpg.profile;

import lombok.Getter;
import net.mineshock.rpg.RPG;
import net.mineshock.rpg.classes.ClassType;
import net.mineshock.rpg.questbase.QuestCreation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.File;
import java.io.IOException;

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
        if (!folder.exists() || !folder.isDirectory()) return profiles;

        File playerFile = new File(folder.getAbsolutePath() + File.separator + playerUUID + ".yml");
        if (!playerFile.exists()) return profiles;

        Player player = Bukkit.getPlayer(playerUUID);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        for (String profileUUIDAsString : config.getKeys(false)) {
            UUID profileId = UUID.fromString(profileUUIDAsString);
            ConfigurationSection profileSection = config.getConfigurationSection(profileUUIDAsString);
            Map<String, String> questData = (Map<String, String>) profileSection.get("quests");

            ClassType classType = ClassType.valueOf(profileSection.getString("class").toUpperCase());

            List<ItemStack> items = (List<ItemStack>) profileSection.get("inventory");
            assert items != null;
            ItemStack[] itemStacks = items.toArray(new ItemStack[0]);

            Location playerLocation = profileSection.getLocation("location");

            int level = profileSection.getInt("level");

            assert player != null;
            profiles.add(new Profile(profileId, player, classType, itemStacks, playerLocation, level, questData));
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
        int level = profile.getLevel();

        PlayerInventory playerInventory = player.getInventory();

        for (int i = 0; i < savedItems.size(); i++) {
            ItemStack item = savedItems.get(i);
            if (item != null) {
                playerInventory.setItem(i, item);
            }
        }

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            ItemStack[] armorContents = playerInventory.getArmorContents(); // Save current armour
            playerInventory.setArmorContents(new ItemStack[]{null, null, null, null}); // Remove armour
            Bukkit.getScheduler().runTaskLater(plugin, () -> playerInventory.setArmorContents(armorContents), 1L); // reequip the armour after 1 tick
        }, 1L);

        player.teleport(playerLocation);

        player.setLevel(level);

        File file = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        Map<String, String> questData = (Map<String, String>) config.get("quests");
        profile.setQuestData(questData);

        System.out.println("Loaded profile for player: " + player.getName());

        profiles.put(player, profile);
    }

    /**
     * Creates a new profile
     * Creates the yml file, if it doesn't exist
     * Generates random UUID for the profile
     * Assigns current inventory + location and level of 0
     * Puts profile into hashmap
     **/
    public void createProfile(UUID playerUUID, String classType) throws IOException {

        Player player = Bukkit.getPlayer(playerUUID);
        UUID profileId = UUID.randomUUID();

        File file = new File(plugin.getDataFolder() + File.separator + "players" + File.separator + playerUUID + ".yml");
        file.createNewFile();

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        assert player != null;
        config.set(profileId + ".class", classType);
        config.set(profileId + ".inventory", player.getInventory().getContents());
        config.set(profileId + ".location", player.getLocation());
        config.set(profileId + ".level", 1);

        Map<String, String> questData = QuestCreation.createQuests();
        config.set(profileId + ".quests", questData);

        config.save(file);
        player.setLevel(1);

        profiles.put(player, new Profile(profileId, player, ClassType.valueOf(classType.toUpperCase()), player.getInventory().getContents(), player.getLocation(), 1, new HashMap<>()));
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
        config.set(profileId + ".level", profile.getLevel());
        config.set(profileId + ".quests", profile.getQuestData());

        config.save(file);
        System.out.println("Saved profile for player: " + player.getName());

    }

}
