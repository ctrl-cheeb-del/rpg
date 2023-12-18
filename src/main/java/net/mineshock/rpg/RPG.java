package net.mineshock.rpg;

import lombok.Getter;
import net.mineshock.rpg.commands.MobCommand;
import net.mineshock.rpg.commands.QuestUpdateCommand;
import net.mineshock.rpg.commands.TestCommand;
import net.mineshock.rpg.mobs.MobSummoner;
import net.mineshock.rpg.mobs.MobDisplay;
import net.mineshock.rpg.profile.Profile;
import net.mineshock.rpg.profile.ProfileManager;
import net.mineshock.rpg.profile.ProfileSelection;
import net.mineshock.rpg.classes.Sorcerer;
import net.mineshock.rpg.questbase.PlayerQuestData;
import net.mineshock.rpg.questbase.Quest;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import net.mineshock.rpg.mobs.CustomMob;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class RPG extends JavaPlugin implements Listener {

    @Getter
    private static RPG instance;


    @Getter
    private final ProfileManager profileManager = new ProfileManager(this);
    private final List<CustomMob> customMobs = new ArrayList<>();
    private final MobDisplay mobDisplay = new MobDisplay();
    private final ProfileSelection profileSelectionGUI = new ProfileSelection(this);

    @Override
    public void onEnable() {

        instance = this;

        // Plugin startup logic
        System.out.println("were in");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new Sorcerer(), this);
        
        MobDisplay mobDisplay = new MobDisplay();
        MobSummoner mobSummoner = new MobSummoner(this, mobDisplay);
        getCommand("summonmob").setExecutor(new MobCommand(this, mobSummoner, customMobs));
        getCommand("test").setExecutor(new TestCommand());
        getCommand("questUpdate").setExecutor(new QuestUpdateCommand());

        // Create plugin folder
        File pluginFolder = new File(getDataFolder() + File.separator + "players");
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }

        File mobsFolder = new File(getDataFolder() + File.separator + "mobs");
        if (!mobsFolder.exists()) {
            mobsFolder.mkdirs();
        }

        // Load custom mobs
        if (mobsFolder.exists()) {
            for (File file : mobsFolder.listFiles()) {
                String mobName = file.getName().substring(0, file.getName().lastIndexOf('.'));
                customMobs.add(new CustomMob(this, mobName, 1)); // Set a default level of 1
                System.out.println("Added CustomMob to list. Size of list: " + customMobs.size());  // Debugging line
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.ADVENTURE);
        player.teleport(player.getWorld().getSpawnLocation());
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 5));
        player.setExp(0);
        player.setLevel(0);
        Profile profile = profileManager.getProfiles().get(player);
        RPG.getInstance().getProfileManager().getProfiles().put(player, profile);

        Quest quest = new Quest();
        PlayerQuestData playerQuestData = new PlayerQuestData(quest);
        Map<String, String> questData = playerQuestData.loadQuestStages(event);
        if (profile != null) {
            profile.setQuestData(questData);
        }

        Bukkit.getScheduler().runTask(this, () -> {
            player.getInventory().clear();
            System.out.println("Cleared inventory for player: " + player.getName());  // Debugging statement
            profileSelectionGUI.createGui(player).open();
            player.setMetadata("selecting-profile", new FixedMetadataValue(this, true));
        });
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) throws IOException {
        Player player = event.getPlayer();
        Profile profile = profileManager.getProfiles().get(player);
        if (profile != null) {
            System.out.println("Player data before saving profile: " + player);
            profileManager.saveProfile(player.getUniqueId(), profile);
            profileManager.getProfiles().remove(player);
            System.out.println("Player data after saving profile: " + player);
        }
    }


    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) throws IOException {
        Player player = event.getPlayer();
        Profile profile = profileManager.getProfiles().get(player);
        if (profile != null) {
            profileManager.saveProfile(player.getUniqueId(), profile);
            profileManager.getProfiles().remove(player);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (player.hasMetadata("selecting-profile")) {

            Bukkit.getScheduler().runTaskLater(this, () -> {
                profileSelectionGUI.createGui(player).open();
            }, 1L);
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity entity) {
            if (entity.hasMetadata("CustomMob")) {
                List<MetadataValue> metadata = entity.getMetadata("CustomMob");
                for (MetadataValue value : metadata) {
                    // check if metadata is from this plugin and is from aCustomMob instance
                    if (value.getOwningPlugin().getDescription().getName().equals(this.getDescription().getName()) && value.value() instanceof CustomMob mob) {
                        mobDisplay.setMobDisplayName(entity, mob.getName(), mob.getLevel());
                        break;
                    }
                }
            }
        }
    }
}