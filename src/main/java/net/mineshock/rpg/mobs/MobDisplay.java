package net.mineshock.rpg.mobs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class MobDisplay {
    public void setMobDisplayName(LivingEntity entity, String name, int level) {
        NamedTextColor nameColor = convertColorCode(name.substring(0, 2)); // get color code from name
        String actualName = name.substring(2); // get actual name without color code

        Component displayName = Component.text("Lv", NamedTextColor.GRAY)
                .append(Component.text(level, NamedTextColor.GRAY))
                .append(Component.text(" "))
                .append(Component.text(actualName, nameColor))
                .append(Component.text(" [", NamedTextColor.GRAY))
                .append(Component.text((int) entity.getHealth(), NamedTextColor.RED))
                .append(Component.text("/", NamedTextColor.GRAY))
                .append(Component.text((int) entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(), NamedTextColor.RED))
                .append(Component.text("]", NamedTextColor.GRAY));
        entity.customName(displayName);
        entity.setCustomNameVisible(true);
    }


    // MobDisplay.java
    public NamedTextColor convertColorCode(String colorCode) {
        char code = colorCode.charAt(1); // get the character after '&'
        switch (code) {
            case '0':
                return NamedTextColor.BLACK;
            case '1':
                return NamedTextColor.DARK_BLUE;
            case '2':
                return NamedTextColor.DARK_GREEN;
            case '3':
                return NamedTextColor.DARK_AQUA;
            case '4':
                return NamedTextColor.DARK_RED;
            case '5':
                return NamedTextColor.DARK_PURPLE;
            case '6':
                return NamedTextColor.GOLD;
            case '7':
                return NamedTextColor.GRAY;
            case '8':
                return NamedTextColor.DARK_GRAY;
            case '9':
                return NamedTextColor.BLUE;
            case 'a':
                return NamedTextColor.GREEN;
            case 'b':
                return NamedTextColor.AQUA;
            case 'c':
                return NamedTextColor.RED;
            case 'd':
                return NamedTextColor.LIGHT_PURPLE;
            case 'e':
                return NamedTextColor.YELLOW;
            case 'f':
                return NamedTextColor.WHITE;
            default:
                return NamedTextColor.WHITE; // default color
        }
    }



}
