package net.mineshock.rpg.commands;

import net.mineshock.rpg.mobs.CustomMob;
import net.mineshock.rpg.mobs.MobSummoner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MobCommand implements CommandExecutor {
    private final MobSummoner mobSummoner;
    private final List<CustomMob> customMobs;
    private final JavaPlugin plugin;

    public MobCommand(JavaPlugin plugin, MobSummoner mobSummoner, List<CustomMob> customMobs) {
        this.plugin = plugin;
        this.mobSummoner = mobSummoner;
        this.customMobs = customMobs;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 2) {
                String mobName = args[0];
                int level = Integer.parseInt(args[1]);
                CustomMob mob = new CustomMob(plugin, mobName, level);
                double health = mob.getHealth();
                mob.setHealth(health); // Update the health variable
                mobSummoner.summonMob(player.getWorld(), EntityType.ZOMBIE, player.getLocation(), mobName, level, health);
                return true;
            }
        }
        return false;
    }
}
