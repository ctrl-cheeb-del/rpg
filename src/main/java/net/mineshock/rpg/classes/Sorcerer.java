package net.mineshock.rpg.classes;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.mineshock.rpg.RPG;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;

import static net.mineshock.rpg.classes.WeaponUtil.lastAttackMap;
import static net.mineshock.rpg.classes.WeaponUtil.makeDamageParticle;

public class Sorcerer implements Listener {

    private static final NamespacedKey mageKey = new NamespacedKey(RPG.getInstance(), "mage");

    /**
     * Simple method that returns an ItemStack with a PersistentDataContainer of a String
     **/
    public static ItemStack create() {

        ItemStack stack = new ItemStack(Material.WOODEN_HOE);
        ItemMeta meta = stack.getItemMeta();

        meta.getPersistentDataContainer().set(mageKey, PersistentDataType.STRING, "testSpell");
        meta.displayName(Component.text("Mage Staff", NamedTextColor.LIGHT_PURPLE).decoration(TextDecoration.ITALIC, false));

        stack.setItemMeta(meta);

        return stack;
    }


    /**
     * Handles when a Player left-clicks with a "mage weapon"
     * Checks if the held item has the PersistentDataContainer
     * Then calls a method corresponding to the String in the container
     * This method will be a spell
     **/
    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {

        if (!event.getAction().isLeftClick()) return;

        Player player = event.getPlayer();

        ItemStack stack = player.getInventory().getItemInMainHand();
        if (stack.getItemMeta() == null || !stack.getItemMeta().getPersistentDataContainer().has(mageKey, PersistentDataType.STRING)) return;

        String spell = stack.getItemMeta().getPersistentDataContainer().get(mageKey, PersistentDataType.STRING);

        event.setCancelled(true);

        Location location = player.getLocation();

        Instant lastAttack = lastAttackMap.get(player);

        Instant currentTime = Instant.now();

        if (lastAttack != null) {
            double timeSinceLastAttack = Duration.between(lastAttack, currentTime).getSeconds();

            if (timeSinceLastAttack < 1) {
                return;
            }
        }




        try {
            assert spell != null;
            Sorcerer.class.getMethod(spell, Location.class).invoke(Sorcerer.class.getDeclaredConstructor().newInstance(), location);
        } catch (NoSuchMethodException e) {
            Bukkit.getConsoleSender().sendMessage("There is no method called " +  spell + " with a Location parameter.");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        lastAttackMap.put(player, Instant.now());

    }

    public void testSpell(Location location) {
        new BukkitRunnable() {
            @Override
            public void run() {
                makeDamageParticle(location, Particle.ELECTRIC_SPARK, null, 1, 1, 4);
            }
        }.runTaskLater(RPG.getInstance(), 2);

        new BukkitRunnable() {
            @Override
            public void run() {
                makeDamageParticle(location, Particle.ELECTRIC_SPARK, null, 3, 1, 4);
            }
        }.runTaskLater(RPG.getInstance(), 4);

        new BukkitRunnable() {
            @Override
            public void run() {
                makeDamageParticle(location, Particle.ELECTRIC_SPARK, null, 5, 1, 4);
            }
        }.runTaskLater(RPG.getInstance(), 6);

        new BukkitRunnable() {
            @Override
            public void run() {
                makeDamageParticle(location, Particle.FLASH, Sound.ENTITY_GENERIC_EXPLODE, 10, 3, 6);
            }
        }.runTaskLater(RPG.getInstance(), 10);
    }

}
