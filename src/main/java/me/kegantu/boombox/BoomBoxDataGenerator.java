package me.kegantu.boombox;

import me.kegantu.boombox.init.ModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class BoomBoxDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(BoomBoxRecipeGenerator::new);
	}

	private static class BoomBoxRecipeGenerator extends FabricRecipeProvider {
		private BoomBoxRecipeGenerator(FabricDataOutput generator) {
			super(generator);
		}

		@Override
		public void generate(Consumer<RecipeJsonProvider> exporter) {
			ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.BOOMBOX)
					.pattern(" r ")
					.pattern("njn")
					.pattern("iii")
					.input('r', Items.REDSTONE_BLOCK)
					.input('n', Blocks.NOTE_BLOCK)
					.input('j', Blocks.JUKEBOX)
					.input('i', Items.IRON_INGOT)
					.criterion(FabricRecipeProvider.hasItem(Blocks.NOTE_BLOCK.asItem()), FabricRecipeProvider.conditionsFromItem(Blocks.NOTE_BLOCK.asItem()))
					.criterion(FabricRecipeProvider.hasItem(Blocks.JUKEBOX.asItem()), FabricRecipeProvider.conditionsFromItem(Blocks.JUKEBOX.asItem()))
					.offerTo(exporter);
		}
	}
}
