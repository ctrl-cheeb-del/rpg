package net.mineshock.rpg;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class MageStaff implements Listener {

    private static final NamespacedKey staffKey = new NamespacedKey(RPG.getInstance(), "staff");

    public static ItemStack create() {

        ItemStack stack = new ItemStack(Material.WOODEN_HOE);
        ItemMeta meta = stack.getItemMeta();

        meta.getPersistentDataContainer().set(staffKey, PersistentDataType.BOOLEAN, true);
        meta.displayName(Component.text("Mage Staff", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));

        stack.setItemMeta(meta);

        return stack;
    }

    public Location makeParticle(Location location, Particle particle, Sound sound, int distance) {
        float pitch = location.getPitch();
        float yaw = location.getYaw();

        double xAdd = Math.sin(Math.toRadians(-yaw)) * Math.cos(Math.toRadians(pitch));
        double yAdd = Math.sin(Math.toRadians(-pitch));
        double zAdd = Math.cos(Math.toRadians(-yaw)) * Math.cos(Math.toRadians(pitch));

        double x = location.getX() + xAdd * distance;
        double y = (location.getY() + 1) + yAdd * distance;
        double z = location.getZ() + zAdd * distance;

        Location targetLocation = new Location(location.getWorld(), x, y, z, yaw, pitch);

        location.getWorld().spawnParticle(particle, targetLocation, 5);
        if (sound != null) {
            location.getWorld().playSound(targetLocation, sound, 1, 10);
        }


        return targetLocation;
    }


    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {

        if (event.getAction() != Action.LEFT_CLICK_AIR) return;

        Player player = event.getPlayer();

        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getItemMeta() == null || !stack.getItemMeta().getPersistentDataContainer().has(staffKey)) return;

        Location location = player.getLocation();

        new BukkitRunnable() {
            @Override
            public void run() {
                makeParticle(location, Particle.ELECTRIC_SPARK, null, 1);
            }
        }.runTaskLater(RPG.getInstance(), 2);

        new BukkitRunnable() {
            @Override
            public void run() {
                makeParticle(location, Particle.ELECTRIC_SPARK, null, 3);
            }
        }.runTaskLater(RPG.getInstance(), 4);

        new BukkitRunnable() {
            @Override
            public void run() {
                makeParticle(location, Particle.ELECTRIC_SPARK, null, 5);
            }
        }.runTaskLater(RPG.getInstance(), 6);

        new BukkitRunnable() {
            @Override
            public void run() {
                Location particle = makeParticle(location, Particle.FLASH, Sound.ENTITY_GENERIC_EXPLODE, 10);
                Collection<LivingEntity> entities = particle.getNearbyLivingEntities(3);
                for (LivingEntity entity : entities) {
                    if (entity.getType() == EntityType.PLAYER) continue;
                    entity.damage(6);
                }
            }
        }.runTaskLater(RPG.getInstance(), 10);


    }

}
