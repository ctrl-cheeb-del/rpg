package net.mineshock.rpg;

import lombok.Getter;
import net.mineshock.rpg.profile.Profile;
import net.mineshock.rpg.profile.ProfileManager;
import net.mineshock.rpg.profile.ProfileSelection;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
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

public final class RPG extends JavaPlugin implements Listener {
    @Getter
    private final ProfileManager profileManager = new ProfileManager(this);

    private final ProfileSelection profileSelectionGUI = new ProfileSelection(this);

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("were in");
        getServer().getPluginManager().registerEvents(this, this);

        // Create plugin folder
        File pluginFolder = new File(getDataFolder() + File.separator + "players");
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(player.getWorld().getSpawnLocation());
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 5));

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
            profileManager.saveProfile(player.getUniqueId(), profile);
            profileManager.getProfiles().remove(player);
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

}