package me.kegantu.boombox.init;

import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.entity.BoomBoxEntity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<BoomBoxEntity> BOOMBOX_ENTITY = Registry.register(Registries.ENTITY_TYPE, Identifier.of(BoomBox.MOD_ID, "boombox_entity"),
            FabricEntityTypeBuilder.<BoomBoxEntity>create(SpawnGroup.MISC, BoomBoxEntity::new).dimensions(EntityDimensions.fixed(0.5f, 0.5f)).build());

    public static void register(){

    }
}
