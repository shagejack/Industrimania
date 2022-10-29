package shagejack.industrimania.content.primalAge.item.sandpaper;

import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import shagejack.industrimania.content.misc.processing.ProcessingRecipe;
import shagejack.industrimania.content.misc.processing.ProcessingRecipeBuilder;
import shagejack.industrimania.registries.AllRecipeTypes;

public class SandpaperRecipe extends ProcessingRecipe<RecipeWrapper> {

    public SandpaperRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(AllRecipeTypes.SANDPAPER, params);
    }

    @Override
    public boolean matches(RecipeWrapper inv, Level level) {
        if (inv.isEmpty())
            return false;

        return ingredients.get(0)
                .test(inv.getItem(0));
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 1;
    }

}
