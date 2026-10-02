package me.kegantu.boombox.init;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.item.BoomBoxItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.function.Function;

public class ModItems {

    public static final Item BOOMBOX = add("boombox", BoomBoxItem::new, new Item.Settings().maxCount(1).rarity(Rarity.COMMON));

    public static void register(){
        //Registry.register(Registries.ITEM, Identifier.of(BoomBox.MOD_ID, "boombox"), BOOMBOX);
    }

    public static <T extends Item> T add(String name, Function<Item.Settings, T> itemFactory, Item.Settings settings) {
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(BoomBox.MOD_ID, name));

        // Create the item instance.
        T item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item.
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }
}
