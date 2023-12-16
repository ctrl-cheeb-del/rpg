package net.mineshock.rpg.classes;


import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

@Getter
public enum ClassType {

    SORCERER(Component.text("Sorcerer", NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));

    private final TextComponent name;

    ClassType(TextComponent name) {
        this.name = name;
    }


}
