package me.kegantu.boombox;

import me.kegantu.boombox.init.ModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import java.util.concurrent.CompletableFuture;

public class BoomBoxDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(BoomBoxRecipeGenerator::new);
	}

	private static class BoomBoxRecipeGenerator extends FabricRecipeProvider {
        public BoomBoxRecipeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
            super(output, registriesFuture);
        }

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput exporter) {
			return new RecipeProvider(registries, exporter) {
				@Override
				public void buildRecipes() {
					shaped(RecipeCategory.MISC, ModItems.BOOMBOX)
							.pattern(" r ")
							.pattern("njn")
							.pattern("iii")
							.define('r', Items.REDSTONE_BLOCK)
							.define('n', Blocks.NOTE_BLOCK)
							.define('j', Blocks.JUKEBOX)
							.define('i', Items.IRON_INGOT)
							.unlockedBy(getHasName(Blocks.NOTE_BLOCK.asItem()), has(Blocks.NOTE_BLOCK.asItem()))
							.unlockedBy(getHasName(Blocks.JUKEBOX.asItem()), has(Blocks.JUKEBOX.asItem()))
							.save(output);
				}
			};
		}

		@Override
		public String getName() {
			return "Boombox Recipes";
		}
	}
}
