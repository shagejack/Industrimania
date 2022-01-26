package shagejack.industrimania.foundation.utility.recipe;

import java.util.function.Predicate;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import shagejack.industrimania.foundation.tileEntity.behaviour.filtering.FilteringBehaviour;

/**
 * Commonly used Predicates for searching through recipe collections.
 * 
 * @author simibubi
 *
 */
public class RecipeConditions {

	public static Predicate<Recipe<?>> isOfType(RecipeType<?>... otherTypes) {
		return recipe -> {
			RecipeType<?> recipeType = recipe.getType();
			for (RecipeType<?> other : otherTypes)
				if (recipeType == other)
					return true;
			return false;
		};
	}

	public static Predicate<Recipe<?>> firstIngredientMatches(ItemStack stack) {
		return r -> !r.getIngredients().isEmpty() && r.getIngredients().get(0).test(stack);
	}

	public static Predicate<Recipe<?>> ingredientsMatches(NonNullList<ItemStack> stacks) {
		return r -> {
			if (r.getIngredients().isEmpty())
				return false;

			NonNullList<Ingredient> ingredients = r.getIngredients();

			for (int i = 0; i < stacks.size(); i++) {
				if (!ingredients.get(i).test(stacks.get(i)))
					return false;
			}

			return true;
		};
	}

	public static Predicate<Recipe<?>> outputMatchesFilter(FilteringBehaviour filtering) {
		return r -> filtering.test(r.getResultItem());

	}

}
