package net.mineshock.rpg.mobs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class MobDisplay {
    public void setMobDisplayName(LivingEntity entity, String name, int level, int health) {
        Component displayName = Component.text("Lv", NamedTextColor.GRAY)
                .append(Component.text(level, NamedTextColor.GRAY))
                .append(Component.text(" "))
                .append(Component.text(name, NamedTextColor.GREEN))
                .append(Component.text(" [", NamedTextColor.GRAY))
                .append(Component.text(health, NamedTextColor.RED))
                .append(Component.text("/", NamedTextColor.GRAY))
                .append(Component.text((int) entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), NamedTextColor.RED))
                .append(Component.text("]", NamedTextColor.GRAY));
        entity.customName(displayName);
        entity.setCustomNameVisible(true);
    }
}
