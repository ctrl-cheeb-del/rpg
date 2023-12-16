package net.mineshock.rpg.profile;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.mineshock.rpg.RPG;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.window.Window;

import java.io.IOException;
import java.util.List;

public class ClassSelector {

    private static final class SorcererItem extends AbstractItem {

        private final RPG plugin = RPG.getInstance();

        private SorcererItem() {
        }

        @Override
        public ItemProvider getItemProvider() {
            ItemStack stack = new ItemStack(Material.ENDER_EYE);
            ItemMeta meta = stack.getItemMeta();

            meta.displayName(Component.text("Sorcerer Class", NamedTextColor.LIGHT_PURPLE).decorate(TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
            meta.lore(List.of(Component.text(""), Component.text("Starting stats:", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("Vigor: 0", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false), Component.text("Mana: 0", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("Mana: 0", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("Endurance: 0", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("Strength: 0", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("Dexterity: 0", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("Intelligence: 0", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text("Faith: 0", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false),
                    Component.text(""), Component.text("Click to select", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false)

            ));

            stack.setItemMeta(meta);


            return new ItemBuilder(stack);
        }

        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
            try {
                plugin.getProfileManager().createProfile(player.getUniqueId(), "sorcerer");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            player.removeMetadata("selecting-profile", plugin);
            player.removePotionEffect(PotionEffectType.BLINDNESS);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.closeInventory();
            }, 1L);
        }
    }

    public record MenuInstance(Window window, Gui gui) {
        public ClassSelector.MenuInstance open() {
            this.window.open();
            return this;
        }
    }

    public static ClassSelector.MenuInstance createGui(Player player) {


        Gui gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# # # A S W # # #",
                        "# # # # # # # # #")
                .addIngredient('S', new SorcererItem())
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle("Choose your class!")
                .setGui(gui)
                .build();

        return new ClassSelector.MenuInstance(window, gui);
    }


}
