package shagejack.industrimania.registers.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registers.AllTags;

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

		static Tag.Named<Item> redstone() {
			return Tags.Items.DUSTS_REDSTONE;
		}

		static Tag.Named<Item> planks() {
			return ItemTags.PLANKS;
		}

		static Tag.Named<Item> woodSlab() {
			return ItemTags.WOODEN_SLABS;
		}

		static Tag.Named<Item> gold() {
			return AllTags.forgeItemTag("ingots/gold");
		}

		static Tag.Named<Item> goldSheet() {
			return AllTags.forgeItemTag("plates/gold");
		}

		static Tag.Named<Item> stone() {
			return Tags.Items.STONE;
		}

		static Tag.Named<Item> brass() {
			return AllTags.forgeItemTag("ingots/brass");
		}

		static Tag.Named<Item> brassSheet() {
			return AllTags.forgeItemTag("plates/brass");
		}

		static Tag.Named<Item> iron() {
			return Tags.Items.INGOTS_IRON;
		}

		static Tag.Named<Item> ironNugget() {
			return AllTags.forgeItemTag("nuggets/iron");
		}

		static Tag.Named<Item> zinc() {
			return AllTags.forgeItemTag("ingots/zinc");
		}

		static Tag.Named<Item> ironSheet() {
			return AllTags.forgeItemTag("plates/iron");
		}

		
		static ItemLike copperBlock() {
			return Items.COPPER_BLOCK;
		}

		static Tag.Named<Item> brassBlock() {
			return AllTags.forgeItemTag("storage_blocks/brass");
		}

		static Tag.Named<Item> zincBlock() {
			return AllTags.forgeItemTag("storage_blocks/zinc");
		}

		static ItemLike copper() {
			return Items.COPPER_INGOT;
		}

		static Tag.Named<Item> copperSheet() {
			return AllTags.forgeItemTag("plates/copper");
		}

		static Tag.Named<Item> copperNugget() {
			return AllTags.forgeItemTag("nuggets/copper");
		}

		static Tag.Named<Item> brassNugget() {
			return AllTags.forgeItemTag("nuggets/brass");
		}

		static Tag.Named<Item> zincNugget() {
			return AllTags.forgeItemTag("nuggets/zinc");
		}

	}
}
