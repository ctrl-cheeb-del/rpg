package net.mineshock.rpg.commands;

import net.mineshock.rpg.profile.Profile;
import net.mineshock.rpg.questbase.Quest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.mineshock.rpg.RPG;

import java.io.IOException;


public class QuestUpdateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be run by a player.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            player.sendMessage("Usage: /questUpdate <questName> <stage>");
            return true;
        }

        String questName = args[0];
        String stage = args[1];

        // fetch the profile from the ProfileManager class
        Profile profile = RPG.getInstance().getProfileManager().getProfiles().get(player);
        if (profile != null) {
            // use the fetched profile to update the quest
            profile.getQuestData().put(questName, stage);
            player.sendMessage("Quest updated: " + questName + " stage set to " + stage);

            RPG.getInstance().getProfileManager().getProfiles().put(player, profile);
            try {
                RPG.getInstance().getProfileManager().saveProfile(player.getUniqueId(), profile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Profile not found for player: " + player.getName());
        }

        return true;
    }

}