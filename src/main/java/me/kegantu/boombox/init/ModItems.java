package me.kegantu.boombox.init;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.item.BoomBoxItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {

    public static final Item BOOMBOX = new BoomBoxItem(new FabricItemSettings().maxCount(1).rarity(Rarity.EPIC));

    public static void register(){
        Registry.register(Registries.ITEM, Identifier.of(BoomBox.MOD_ID, "boombox"), BOOMBOX);
    }
}
