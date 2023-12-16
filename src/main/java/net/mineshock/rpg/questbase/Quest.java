package net.mineshock.rpg.questbase;

import net.mineshock.rpg.RPG;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Quest {

    private static final String QUESTS_FOLDER = "questdata";
    private static final RPG plugin = RPG.getInstance();

    public String load(UUID playerUUID, String questName) {
        File questFile = new File(plugin.getDataFolder() + File.separator + QUESTS_FOLDER + File.separator + playerUUID + ".yml");
        YamlConfiguration questData = YamlConfiguration.loadConfiguration(questFile);

        return questData.getString(questName);
    }

    public void update(UUID playerUUID, String questName, String newStage) {
        File questFile = new File(plugin.getDataFolder() + File.separator + QUESTS_FOLDER + File.separator + playerUUID + ".yml");
        YamlConfiguration questData = YamlConfiguration.loadConfiguration(questFile);

        questData.set(questName, newStage);

        try {
            questData.save(questFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
