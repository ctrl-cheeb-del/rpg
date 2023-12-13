package net.mineshock.rpg.profile;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.mineshock.rpg.RPG;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.window.Window;

import java.io.IOException;
import java.util.ArrayList;

public class ProfileSelection {

    private final RPG plugin;


    public ProfileSelection(RPG plugin) {
        this.plugin = plugin;
    }

    /**
     * Makes a new item class that you can put in the GUI
     **/
    private static final class ProfileItem extends AbstractItem {

        private final Profile profile;
        private final RPG plugin;

        private ProfileItem(Profile profile, RPG plugin) {
            this.profile = profile;
            this.plugin = plugin;
        }

        /**
         * Where the actual ItemStack is made
         * You create an ItemBuilder
         * It's best to make an ItemStack, then put it in a builder (rather than using the builders own methods)
         **/
        @Override
        public ItemProvider getItemProvider() {

            String profileId = profile.getProfileId().toString();

            ItemStack stack = new ItemStack(Material.PLAYER_HEAD);
            ItemMeta meta = stack.getItemMeta();

            meta.displayName(Component.text(profileId, NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));
            stack.setItemMeta(meta);


            return new ItemBuilder(stack);
        }

        /**
         * Like an @EventHandler for InventoryClickEvent
         **/
        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {

            plugin.getProfileManager().loadProfile(profile);

            player.removePotionEffect(PotionEffectType.BLINDNESS);
            player.closeInventory();
        }
    }

    private static final class NewProfileItem extends AbstractItem {

        private final RPG plugin;

        private NewProfileItem(RPG plugin) {
            this.plugin = plugin;
        }

        @Override
        public ItemProvider getItemProvider() {

            ItemStack stack = new ItemStack(Material.EMERALD_BLOCK);
            ItemMeta meta = stack.getItemMeta();

            meta.displayName(Component.text("Create a new profile!", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false));

            stack.setItemMeta(meta);

            return new ItemBuilder(stack);
        }

        @Override
        public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {

            try {
                plugin.getProfileManager().createProfile(player.getUniqueId());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            player.removePotionEffect(PotionEffectType.BLINDNESS);
            player.closeInventory();
        }
    }

    /**
     * Handles the actual opening of the GUI
     * To open you use the next method to get a "MenuInstance" then use this .open() method
     * e.g. ProfileSelection.createGui(player).open();
     **/
    public record MenuInstance(Window window, Gui gui) {
        public MenuInstance open() {
            this.window.open();
            return this;
        }
    }

    /**
     * Where you set the actual structure of the GUI
     * Each character is an item
     **/
    public ProfileSelection.MenuInstance createGui(Player player) {

        ArrayList<Item> items = new ArrayList<>();

        for (Profile profile : plugin.getProfileManager().getProfiles(player.getUniqueId())) {
            items.add(new ProfileItem(profile, plugin));
        }

        Gui gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# # # # # # # # #",
                        "# # # # # # # # N")
                .addIngredient('#', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('N', new NewProfileItem(plugin))
                .setContent(items)
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle("Select your profile")
                .setGui(gui)
                .build();

        return new ProfileSelection.MenuInstance(window, gui);
    }


}
