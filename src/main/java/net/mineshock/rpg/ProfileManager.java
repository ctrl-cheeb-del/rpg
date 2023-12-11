package net.mineshock.rpg;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

public class ProfileManager {
    private final Map<String, Profile> profiles = new HashMap<>();

    public void saveProfile(Player player) {
        Profile profile = new Profile(player, player.getInventory().getContents(), player.getLocation());
        profiles.put(player.getName(), profile);
    }

    public Profile loadProfile(String playerName) {
        return profiles.get(playerName);
    }
}
