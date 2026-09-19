package me.kegantu.boombox.item.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record BoomboxComponentDataType(String musicUUID, float volume) {

    public static final Codec<BoomboxComponentDataType> CODEC = RecordCodecBuilder.create(builder -> {
       return builder.group(Codec.STRING.fieldOf("musicUUID").forGetter(BoomboxComponentDataType::musicUUID),
               Codec.FLOAT.optionalFieldOf("volume", 1f).forGetter(BoomboxComponentDataType::volume)).apply(builder, BoomboxComponentDataType::new);
    });
}
