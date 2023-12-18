package net.mineshock.rpg.questbase;

import net.mineshock.rpg.RPG;
import net.mineshock.rpg.profile.Profile;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class Quest {

    private static final RPG plugin = RPG.getInstance();

    public String load(UUID playerUUID, String questName) {
        Profile profile = plugin.getProfileManager().getProfiles().get(Bukkit.getPlayer(playerUUID));
        if (profile == null || profile.getQuestData() == null) {
            return null;
        }
        return profile.getQuestData().get(questName);
    }

    public void update(UUID playerUUID, String questName, String newStage) {
        Profile profile = plugin.getProfileManager().getProfiles().get(Bukkit.getPlayer(playerUUID));
        if (profile == null || profile.getQuestData() == null) {
            return;
        }
        // update the quest stage regardless of whether the quest exists or not
        profile.getQuestData().put(questName, newStage);
    }


}
