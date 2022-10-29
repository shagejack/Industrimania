package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import shagejack.industrimania.content.misc.processing.ProcessingRecipe;
import shagejack.industrimania.content.misc.processing.ProcessingRecipeBuilder;
import shagejack.industrimania.registries.AllRecipeTypes;

import java.util.stream.IntStream;

public class ItemPlaceableBurnRecipe extends ProcessingRecipe<RecipeWrapper> {

    public ItemPlaceableBurnRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(AllRecipeTypes.ITEM_PLACEABLE_BURN, params);
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level worldIn) {
        if (inv.isEmpty())
            return false;

        return IntStream.range(0, 16).allMatch(index -> ingredients.get(index).test(inv.getItem(index)));
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
