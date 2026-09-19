package me.kegantu.boombox.init;

import com.mojang.serialization.Codec;
import me.kegantu.boombox.item.components.BoomboxComponentDataType;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.function.UnaryOperator;

public class ModComponents {

    public static final HashMap<Identifier, ComponentType<?>> COMPONENT_TYPES = new HashMap<>();

    public static final ComponentType<BoomboxComponentDataType> BOOMBOX_COMPONENT = add(componentBuilder -> componentBuilder.codec(BoomboxComponentDataType.CODEC));

    private static <T>ComponentType<T> add(UnaryOperator<ComponentType.Builder<T>> builder){
        return builder.apply(ComponentType.builder()).build();
    }

    public static void register(){
        COMPONENT_TYPES.forEach((identifier, componentType) ->
                Registry.register(Registries.DATA_COMPONENT_TYPE, identifier, componentType));
    }
}
