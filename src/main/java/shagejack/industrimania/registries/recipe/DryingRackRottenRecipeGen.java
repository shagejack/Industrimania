package shagejack.industrimania.registries.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import shagejack.industrimania.registries.AllRecipeTypes;
import shagejack.industrimania.registries.item.AllItems;

public class DryingRackRottenRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe

    GRASS_ROT = rot(AllItems.grass.get(), AllItems.rottenGrass.get()),
    HAY_ROT = rot(AllItems.hay.get(), AllItems.rottenGrass.get())

    ;

    GeneratedRecipe rot(Item input, Item rot) {
        return create(() -> input, b -> b.duration(50)
                .output(rot));
    }

    public DryingRackRottenRecipeGen(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.DRYING_RACK_ROTTEN;
    }

}
