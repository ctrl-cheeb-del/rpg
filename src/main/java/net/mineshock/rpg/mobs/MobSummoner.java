package net.mineshock.rpg.mobs;

import net.mineshock.rpg.mobs.MobDisplay;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public class MobSummoner {
    private final MobDisplay mobDisplay;
    private final JavaPlugin plugin;

    public MobSummoner(JavaPlugin plugin, MobDisplay mobDisplay) {
        this.plugin = plugin;
        this.mobDisplay = mobDisplay;
    }

    public void summonMob(World world, EntityType entityType, Location location, CustomMob mob) {
        LivingEntity entity = (LivingEntity) world.spawnEntity(location, entityType);
        entity.setMetadata("CustomMob", new FixedMetadataValue(plugin, mob)); // attach CustomMob instance to entity as metadata
        entity.setCustomName(mob.getName().toString());
        entity.setCustomNameVisible(true);
        entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(mob.getHealth());
        entity.setHealth(mob.getHealth());
        int intHealth = (int) mob.getHealth();
        mobDisplay.setMobDisplayName(entity, mob.getName(), mob.getLevel());
    }
}
