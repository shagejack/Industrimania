package shagejack.industrimania.registries.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registries.tags.AllTags;

public abstract class IndustrimaniaRecipeProvider extends RecipeProvider {

	protected final List<GeneratedRecipe> all = new ArrayList<>();

	public IndustrimaniaRecipeProvider(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> p_200404_1_) {
		all.forEach(c -> c.register(p_200404_1_));
		Industrimania.LOGGER.info(getName() + " registered " + all.size() + " recipe" + (all.size() == 1 ? "" : "s"));
	}

	protected GeneratedRecipe register(GeneratedRecipe recipe) {
		all.add(recipe);
		return recipe;
	}

	@FunctionalInterface
	public interface GeneratedRecipe {
		void register(Consumer<FinishedRecipe> consumer);
	}

	protected static class Marker {
	}

	protected static class I {

		static TagKey<Item> redstone() {
			return Tags.Items.DUSTS_REDSTONE;
		}

		static TagKey<Item> planks() {
			return ItemTags.PLANKS;
		}

		static TagKey<Item> woodSlab() {
			return ItemTags.WOODEN_SLABS;
		}

		static TagKey<Item> gold() {
			return AllTags.forgeItemTag("ingots/gold");
		}

		static TagKey<Item> goldSheet() {
			return AllTags.forgeItemTag("plates/gold");
		}

		static TagKey<Item> stone() {
			return Tags.Items.STONE;
		}

		static TagKey<Item> brass() {
			return AllTags.forgeItemTag("ingots/brass");
		}

		static TagKey<Item> brassSheet() {
			return AllTags.forgeItemTag("plates/brass");
		}

		static TagKey<Item> iron() {
			return Tags.Items.INGOTS_IRON;
		}

		static TagKey<Item> ironNugget() {
			return AllTags.forgeItemTag("nuggets/iron");
		}

		static TagKey<Item> zinc() {
			return AllTags.forgeItemTag("ingots/zinc");
		}

		static TagKey<Item> ironSheet() {
			return AllTags.forgeItemTag("plates/iron");
		}

		
		static ItemLike copperBlock() {
			return Items.COPPER_BLOCK;
		}

		static TagKey<Item> brassBlock() {
			return AllTags.forgeItemTag("storage_blocks/brass");
		}

		static TagKey<Item> zincBlock() {
			return AllTags.forgeItemTag("storage_blocks/zinc");
		}

		static ItemLike copper() {
			return Items.COPPER_INGOT;
		}

		static TagKey<Item> copperSheet() {
			return AllTags.forgeItemTag("plates/copper");
		}

		static TagKey<Item> copperNugget() {
			return AllTags.forgeItemTag("nuggets/copper");
		}

		static TagKey<Item> brassNugget() {
			return AllTags.forgeItemTag("nuggets/brass");
		}

		static TagKey<Item> zincNugget() {
			return AllTags.forgeItemTag("nuggets/zinc");
		}

	}
}
