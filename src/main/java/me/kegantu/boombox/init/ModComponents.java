package me.kegantu.boombox.init;

import com.mojang.serialization.Codec;
import me.kegantu.boombox.BoomBox;
import me.kegantu.boombox.item.components.BoomboxComponentDataType;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import java.util.HashMap;
import java.util.function.UnaryOperator;

public class ModComponents {

    public static final HashMap<Identifier, DataComponentType<?>> COMPONENT_TYPES = new HashMap<>();

    public static final DataComponentType<BoomboxComponentDataType> BOOMBOX_COMPONENT = add(
            componentBuilder -> componentBuilder.persistent(BoomboxComponentDataType.CODEC),
            Identifier.fromNamespaceAndPath(BoomBox.MOD_ID, "boombox_component"));

    private static <T>DataComponentType<T> add(UnaryOperator<DataComponentType.Builder<T>> builder, Identifier identifier){
        var component = builder.apply(DataComponentType.builder()).build();
        COMPONENT_TYPES.put(identifier, component);
        return component;
    }

    public static void register(){
        COMPONENT_TYPES.forEach((identifier, componentType) ->
                Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, identifier, componentType));
    }
}
