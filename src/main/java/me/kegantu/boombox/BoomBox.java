package me.kegantu.boombox;

import me.kegantu.boombox.init.ModEntities;
import me.kegantu.boombox.init.ModItems;
import me.kegantu.boombox.init.ModPackets;
import me.kegantu.boombox.soundsystem.MusicManager;
import me.kegantu.boombox.utils.AudioDownloaderExecutor;
import me.kegantu.boombox.utils.FFmpegDownloader;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoomBox implements ModInitializer {
	public static final String MOD_ID = "boombox";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final Identifier TEST_IDENTIFIER = new Identifier(MOD_ID, "test");
	public static final SoundEvent TEST = SoundEvent.of(TEST_IDENTIFIER);

	@Override
	public void onInitialize() {
		ModItems.register();
		ModEntities.register();
		ModPackets.registerC2SPackets();
		ModPackets.registerS2CPackets();
		FFmpegDownloader.download();
	}
}