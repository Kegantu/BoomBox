package me.kegantu.boombox;

import me.kegantu.boombox.init.ModEntities;
import me.kegantu.boombox.init.ModItems;
import me.kegantu.boombox.init.ModPackets;
import me.kegantu.boombox.init.ModParticles;
import me.kegantu.boombox.utils.FFmpegDownloader;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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
		FFmpegDownloader.download();

		ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> entries.add(ModItems.BOOMBOX));
	}
}