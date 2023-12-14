package net.mineshock.rpg.mobs;

import net.mineshock.rpg.mobs.MobDisplay;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class MobSummoner {
    private final MobDisplay mobDisplay;

    public MobSummoner(MobDisplay mobDisplay) {
        this.mobDisplay = mobDisplay;
    }

    public void summonMob(World world, EntityType entityType, Location location, String name, int level, double health) {
        LivingEntity mob = (LivingEntity) world.spawnEntity(location, entityType);
        mob.setCustomName(name);
        mob.setCustomNameVisible(true);
        mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health * level);
        mob.setHealth(health * level);
        System.out.println("Set mob health: " + mob.getHealth());  // Debugging line
        int intHealth = (int) health;
        mobDisplay.setMobDisplayName(mob, name, level, intHealth);
    }
}

