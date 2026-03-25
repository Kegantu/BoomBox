package me.kegantu.boombox;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import me.kegantu.boombox.cca.BoomboxComponent;
import me.kegantu.boombox.init.ModEntities;
import me.kegantu.boombox.init.ModItems;
import me.kegantu.boombox.init.ModPackets;
import me.kegantu.boombox.init.ModParticles;
import me.kegantu.boombox.utils.FFmpegDownloader;
import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoomBox implements ModInitializer, EntityComponentInitializer {
	public static final String MOD_ID = "boombox";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier TEST_IDENTIFIER = new Identifier(MOD_ID, "test");
	public static final SoundEvent TEST = SoundEvent.of(TEST_IDENTIFIER);

	public static final ComponentKey<BoomboxComponent> BOOMBOX_COMPONENT =
			ComponentRegistry.getOrCreate(new Identifier(MOD_ID, "boombox_component"), BoomboxComponent.class);

	public static BlockPos lastUsedChestBlockPos;

	@Override
	public void onInitialize() {
		ModItems.register();
		ModEntities.register();
		ModPackets.registerC2SPackets();
		ModPackets.registerS2CPackets();
		ModParticles.register();
		FFmpegDownloader.download();
	}

	@Override
	public void registerEntityComponentFactories(EntityComponentFactoryRegistry entityComponentFactoryRegistry) {
		entityComponentFactoryRegistry.beginRegistration(PlayerEntity.class, BOOMBOX_COMPONENT).respawnStrategy(RespawnCopyStrategy.NEVER_COPY).end(BoomboxComponent::new);
	}
}