package me.kegantu.boombox.init;

import com.mojang.serialization.Codec;
import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.item.components.BoomboxComponentDataType;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.UnaryOperator;

public class ModComponents {

    public static final HashMap<Identifier, ComponentType<?>> COMPONENT_TYPES = new HashMap<>();

    public static final ComponentType<BoomboxComponentDataType> BOOMBOX_COMPONENT = add(
            componentBuilder -> componentBuilder.codec(BoomboxComponentDataType.CODEC),
            Identifier.of(BoomBox.MOD_ID, "boombox_component"));

    private static <T>ComponentType<T> add(UnaryOperator<ComponentType.Builder<T>> builder, Identifier identifier){
        var component = builder.apply(ComponentType.builder()).build();
        COMPONENT_TYPES.put(identifier, component);
        return component;
    }

    public static void register(){
        COMPONENT_TYPES.forEach((identifier, componentType) ->
                Registry.register(Registries.DATA_COMPONENT_TYPE, identifier, componentType));
    }
}
