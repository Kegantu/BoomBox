package me.kegantu.boombox.init;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.item.BoomBoxItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import java.util.function.Function;

public class ModItems {

    public static final Item BOOMBOX = add("boombox", BoomBoxItem::new, new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));

    public static void register(){
        //Registry.register(Registries.ITEM, Identifier.of(BoomBox.MOD_ID, "boombox"), BOOMBOX);
    }

    public static <T extends Item> T add(String name, Function<Item.Properties, T> itemFactory, Item.Properties settings) {
        // Create the item key.
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, name));

        // Create the item instance.
        T item = itemFactory.apply(settings.setId(itemKey));

        // Register the item.
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }
}
