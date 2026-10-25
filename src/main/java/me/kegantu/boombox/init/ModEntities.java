package me.kegantu.boombox.init;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.entity.BoomBoxEntity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {

    public static final EntityType<BoomBoxEntity> BOOMBOX_ENTITY = Registry.register(BuiltInRegistries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "boombox_entity"),
            EntityType.Builder.<BoomBoxEntity>of(BoomBoxEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "boombox_entity"))));

    public static void register(){

    }
}
