package net.mineshock.rpg.questbase;

import java.util.HashMap;
import java.util.Map;

public class QuestCreation {

    public static Map<String, String> createQuests() {
        Map<String, String> questData = new HashMap<>();
        questData.put("miningQuest", "notStarted");
        questData.put("villagerQuest", "notStarted");
        questData.put("cookingQuest", "notStarted");
        // add more quests here
        return questData;
    }
}
