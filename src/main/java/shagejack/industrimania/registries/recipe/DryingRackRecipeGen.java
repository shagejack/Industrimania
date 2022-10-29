package shagejack.industrimania.registries.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import shagejack.industrimania.registries.AllRecipeTypes;
import shagejack.industrimania.registries.item.AllItems;

public class DryingRackRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe

    GRASS_DRY = dry(AllItems.grass.get(), AllItems.hay.get(), 0.8f, 3600)

                    ;

    GeneratedRecipe dry(Item input, Item dry, float chance, int time) {
        return create(() -> input, b -> b.duration(time)
                .output(chance, dry));
    }

    public DryingRackRecipeGen(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.DRYING_RACK;
    }

}
