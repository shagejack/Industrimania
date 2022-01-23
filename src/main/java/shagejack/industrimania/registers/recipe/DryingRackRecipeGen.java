package shagejack.industrimania.registers.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import shagejack.industrimania.registers.AllRecipeTypes;
import shagejack.industrimania.registers.item.AllItems;

public class DryingRackRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe

    GRASS_DRY = dry(AllItems.grass.get(), AllItems.hay.get(), 3600)

                    ;

    GeneratedRecipe dry(Item input, Item dry, int time) {
        return create(() -> input, b -> b.duration(time)
                .output(dry));
    }

    public DryingRackRecipeGen(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.DRYING_RACK;
    }

}
