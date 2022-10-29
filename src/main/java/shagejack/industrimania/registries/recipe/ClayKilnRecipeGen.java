package shagejack.industrimania.registries.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import shagejack.industrimania.foundation.utility.recipe.IRecipeTypeInfo;
import shagejack.industrimania.registries.AllRecipeTypes;
import shagejack.industrimania.registries.block.AllBlocks;

public class ClayKilnRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe

    FLINT = burn(Blocks.GRAVEL.asItem(), AllBlocks.gravity_hot_gravel.item().get(), 400)



            ;

    GeneratedRecipe burn(Item input, Item output, int time) {
        return create(() -> input, b -> b.duration(time)
                .output(output));
    }

    public ClayKilnRecipeGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected IRecipeTypeInfo getRecipeType() {
        return AllRecipeTypes.CLAY_KILN.getType();
    }


}
