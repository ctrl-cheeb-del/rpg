package net.mineshock.rpg.questbase;

import net.mineshock.rpg.RPG;
import net.mineshock.rpg.profile.Profile;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerQuestData {

    private Quest quest;

    public PlayerQuestData(Quest quest) {
        this.quest = quest;
    }

    public Map<String, String> loadQuestStages(PlayerJoinEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        Map<String, String> questData = new HashMap<>();

        // Get profile of the player
        Profile profile = RPG.getInstance().getProfileManager().getProfiles().get(event.getPlayer());

        if (profile != null) {
            questData = profile.getQuestData();
        }

        return questData;
    }



}
