package me.kegantu.boombox.init;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.UnaryOperator;

public class ModComponents {

    public static final HashMap<Identifier, ComponentType<?>> COMPONENT_TYPES = new HashMap<>();

    public static final ComponentType<String> MUSIC_UUID = add(componentBuilder -> componentBuilder.codec(Codec.STRING));
    public static final ComponentType<Float> VOLUME = add(componentBuilder -> componentBuilder.codec(Codec.FLOAT));

    private static <T>ComponentType<T> add(UnaryOperator<ComponentType.Builder<T>> builder){
        return builder.apply(ComponentType.builder()).build();
    }

    public static void register(){
        COMPONENT_TYPES.forEach((identifier, componentType) ->
                Registry.register(Registries.DATA_COMPONENT_TYPE, identifier, componentType));
    }
}
