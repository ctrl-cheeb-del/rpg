package net.mineshock.rpg.questbase;

import net.mineshock.rpg.RPG;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerQuestData {

    private Quest quest;

    public PlayerQuestData(Quest quest) {
        this.quest = quest;
    }

    public void loadQuestStages(PlayerJoinEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();

        // Check if a quest file exists for the player
        File questFile = new File(RPG.getInstance().getDataFolder() + File.separator + "questdata" + File.separator + playerUUID + ".yml");
        if (!questFile.exists()) {
            // If not, create a new quest file with default quest stages
            questFile.getParentFile().mkdirs();
            try {
                questFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            YamlConfiguration questData = YamlConfiguration.loadConfiguration(questFile);
            questData.set("villagerQuest", "notStarted");
            questData.set("miningQuest", "notStarted");

            try {
                questData.save(questFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String villagerQuestState = quest.load(playerUUID, "villagerQuest");
        String miningQuestState = quest.load(playerUUID, "miningQuest");
        String cookingQuestState = quest.load(playerUUID, "cookingQuest");

    }


}
