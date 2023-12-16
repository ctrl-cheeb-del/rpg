package net.mineshock.rpg.classes;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class WeaponUtil {

    public static Map<Player, Instant> lastAttackMap = new HashMap<>();


    /**
     * Returns a Location in the direction the Player is looking
     * int distanceFromPlayer is how many blocks away from the Player, the Location is set to
     **/
    private static Location getLookingLocation(Location location, int distanceFromPlayer) {

        double x = location.getX() + (Math.sin(Math.toRadians(-location.getYaw())) * Math.cos(Math.toRadians(location.getPitch()))) * distanceFromPlayer;
        double y = (location.getY() + 1) + (Math.sin(Math.toRadians(-location.getPitch()))) * distanceFromPlayer;
        double z = location.getZ() + (Math.cos(Math.toRadians(-location.getYaw())) * Math.cos(Math.toRadians(location.getPitch()))) * distanceFromPlayer;

        return new Location(location.getWorld(), x, y, z, location.getYaw(), location.getPitch());

    }

    /**
     * Creates a particle effect in the direction the Player is looking
     * Input the Player's Location, the Particle type, a Sound (if none, put null),
     * the distance from the Player, that the particle will spawn
     * Also input the radius around the Particle that will inflict damage to LivingEntities, and the amount of damage it will do
     **/
    public static Location makeDamageParticle(Location location, Particle particle, Sound sound, int distance, int damageRadius, int damage) {
        Location targetLocation = getLookingLocation(location, distance);

        location.getWorld().spawnParticle(particle, targetLocation, 5);
        if (sound != null) {
            location.getWorld().playSound(targetLocation, sound, 1, 10);
        }

        if (damageRadius != 0) {
            Collection<LivingEntity> entities = targetLocation.getNearbyLivingEntities(damageRadius);
            for (LivingEntity entity : entities) {
                if (entity.getType() == EntityType.PLAYER) continue;
                entity.damage(damage);
            }
        }


        return targetLocation;
    }




}
