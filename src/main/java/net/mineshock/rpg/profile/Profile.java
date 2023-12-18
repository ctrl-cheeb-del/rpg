package net.mineshock.rpg.profile;

import lombok.Getter;
import lombok.Setter;
import net.mineshock.rpg.classes.ClassType;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;

@Getter
public class Profile {

    private final UUID profileId;
    private final OfflinePlayer player;
    private final ClassType classType;
    @Setter
    private ItemStack[] playerInventory;
    @Setter
    private Location location;
    @Setter
    private int level;
    @Setter
    private Map<String, String> questData;


    public Profile(UUID profileId, OfflinePlayer player, ClassType rpgClass, ItemStack[] playerInventory, Location location, int level, Map<String, String> questData) {
        this.profileId = profileId;
        this.player = player;
        this.classType = rpgClass;
        this.playerInventory = playerInventory;
        this.location = location;
        this.level = level;
        this.questData = questData;
    }


}
