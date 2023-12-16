package net.mineshock.rpg.quests;

import net.mineshock.rpg.questbase.Quest;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class testQuest {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Quest quest = new Quest();
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (message.equalsIgnoreCase("test")) {
            String villagerQuestState = quest.load(player.getUniqueId(), "villagerQuest");
            if (villagerQuestState.equals("notStarted")) {
                System.out.println("quest done");
                // Update the quest stage to "completed"
                quest.update(player.getUniqueId(), "villagerQuest", "completed");
            }
        }
    }


}
