package me.kegantu.boombox;

import me.kegantu.boombox.init.*;
import me.kegantu.boombox.utils.DenoDownloader;
import me.kegantu.boombox.utils.FFmpegDownloader;
import me.kegantu.boombox.utils.YTDLPDownloader;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoomBox implements ModInitializer {
	public static final String MOD_ID = "boombox";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.register();
		ModEntities.register();
		ModPackets.registerC2SPackets();
		ModPackets.registerS2CPackets();
		ModParticles.register();
		ModComponents.register();
		FFmpegDownloader.download();
		YTDLPDownloader.download();
		DenoDownloader.download();

		ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> entries.accept(ModItems.BOOMBOX));
	}
}