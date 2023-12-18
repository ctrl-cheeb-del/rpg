package net.mineshock.rpg.quests;

import net.mineshock.rpg.questbase.Quest;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class testQuest {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Quest quest = new Quest();

        String greetingQuestState = quest.load(player.getUniqueId(), "greetingQuest");
        if (greetingQuestState == null || !greetingQuestState.equals("completed")) {
            System.out.println("quest done");
            // Update the quest stage to "completed"
            quest.update(player.getUniqueId(), "villagerQuest", "completed");
        }
    }
}


