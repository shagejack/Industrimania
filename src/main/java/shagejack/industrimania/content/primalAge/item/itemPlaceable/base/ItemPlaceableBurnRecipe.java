package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import shagejack.industrimania.content.contraptions.processing.ProcessingRecipe;
import shagejack.industrimania.content.contraptions.processing.ProcessingRecipeBuilder;
import shagejack.industrimania.registers.AllRecipeTypes;

public class ItemPlaceableBurnRecipe extends ProcessingRecipe<RecipeWrapper> {

    public ItemPlaceableBurnRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(AllRecipeTypes.ITEM_PLACEABLE_BURN, params);
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        if (inv.isEmpty())
            return false;
        return ingredients.get(0)
                .test(inv.getItem(0));
    }

    @Override
    protected int getMaxOutputCount() {
        return 16;
    }

    @Override
    protected int getMaxInputCount() {
        return 16;
    }
}
