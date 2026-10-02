package me.kegantu.boombox;

import me.kegantu.boombox.init.ModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeGenerator;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class BoomBoxDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(BoomBoxRecipeGenerator::new);
	}

	private static class BoomBoxRecipeGenerator extends FabricRecipeProvider {
        public BoomBoxRecipeGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

		@Override
		protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
			return new RecipeGenerator(registries, exporter) {
				@Override
				public void generate() {
					createShaped(RecipeCategory.MISC, ModItems.BOOMBOX)
							.pattern(" r ")
							.pattern("njn")
							.pattern("iii")
							.input('r', Items.REDSTONE_BLOCK)
							.input('n', Blocks.NOTE_BLOCK)
							.input('j', Blocks.JUKEBOX)
							.input('i', Items.IRON_INGOT)
							.criterion(hasItem(Blocks.NOTE_BLOCK.asItem()), conditionsFromItem(Blocks.NOTE_BLOCK.asItem()))
							.criterion(hasItem(Blocks.JUKEBOX.asItem()), conditionsFromItem(Blocks.JUKEBOX.asItem()))
							.offerTo(exporter);
				}
			};
		}

		@Override
		public String getName() {
			return "Boombox Recipies";
		}
	}
}
