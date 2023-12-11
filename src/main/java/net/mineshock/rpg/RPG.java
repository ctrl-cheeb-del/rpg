package net.mineshock.rpg;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class RPG extends JavaPlugin implements Listener {
    private final ProfileManager profileManager = new ProfileManager();
    private final ProfileSelectionGUI profileSelectionGUI = new ProfileSelectionGUI(profileManager);



    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("were in");
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(profileSelectionGUI, this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Profile profile = profileManager.loadProfile(player.getName());
        if (profile != null) {
            player.getInventory().setContents(profile.getInventoryContents());
            player.teleport(profile.getLocation());
        }
        profileSelectionGUI.openInventory(player);


    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        profileManager.saveProfile(player);
    }
}
