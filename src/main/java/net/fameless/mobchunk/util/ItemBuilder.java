package net.fameless.mobchunk.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemBuilder {

    public static ItemStack buildItem(ItemStack itemStack, Component name, boolean applyFlags, Component ...lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(name.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));

        if (applyFlags) Arrays.stream(ItemFlag.values()).forEach(meta::addItemFlags);

        List<Component> lores = new ArrayList<>();
        for (Component c : lore) {
            lores.add(c.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        }

        meta.lore(lores);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static ItemStack fillerItem() {
        ItemStack stack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = stack.getItemMeta();
        meta.displayName(Component.empty());
        stack.setItemMeta(meta);
        return stack;
    }

}
