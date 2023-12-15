package net.mineshock.rpg.mobs;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class CustomMob {
    private final JavaPlugin plugin;
    @Getter
    private String name;
    private double baseHealth;
    @Getter
    private double health;
    @Getter
    private int level;
    private String colorCode = "&a"; // default color code

    public CustomMob(JavaPlugin plugin, String name, int level) {
        this.plugin = plugin;
        this.name = name;
        this.level = level;
        loadData();
        this.health = baseHealth * level;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    private void loadData() {
        File file = new File(plugin.getDataFolder(), "mobs" + File.separator + name + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        baseHealth = config.getDouble("baseHealth");

        // load name with color codes
        if (config.getString("name") != null) {
            this.name = config.getString("name");
            if (name.contains("&")) {
                colorCode = name.substring(name.indexOf("&"), name.indexOf("&") + 2);
            }
        }
    }
}

