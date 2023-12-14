package net.mineshock.rpg.mobs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class MobDisplay {
    public void setMobDisplayName(LivingEntity entity, String name, int level, int health) {
        String customName = "Lv" + level + " " + name + " [" + health + "/" + (int) entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + "]";
        entity.setCustomName(customName);
        entity.setCustomNameVisible(true);
    }



}

