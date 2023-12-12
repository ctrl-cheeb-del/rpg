package net.mineshock.rpg;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.util.Map;

public final class RPG extends JavaPlugin implements Listener {
    private final ProfileManager profileManager = new ProfileManager(this);
    private final ProfileSelectionGUI profileSelectionGUI = new ProfileSelectionGUI(profileManager);

    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("were in");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(profileSelectionGUI, this);

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
        getLogger().info("Player joined: " + event.getPlayer().getName());
        Map<String, Profile> profiles = profileManager.getProfiles();
        String playerUUID = event.getPlayer().getUniqueId().toString();
        if (profiles.containsKey(playerUUID)) {
            this.profileSelectionGUI.openInventory(event.getPlayer());
        } else {
            // Code to create a new profile goes here
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        profileManager.saveProfile(player.getUniqueId().toString(), player);
    }
}
