package shagejack.industrimania.registries.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import shagejack.industrimania.registries.AllRecipeTypes;
import shagejack.industrimania.registries.item.AllItems;

public class SandpaperRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe

            PLANK_OAK = sand(AllItems.plankOak.get(), AllItems.plankOakSmooth.get(), 32),
            PLANK_DARKOAK = sand(AllItems.plankDarkOak.get(), AllItems.plankDarkOakSmooth.get(), 32),
            PLANK_ACACIA = sand(AllItems.plankAcacia.get(), AllItems.plankAcaciaSmooth.get(), 32),
            PLANK_BIRCH = sand(AllItems.plankBirch.get(), AllItems.plankBirchSmooth.get(), 32),
            PLANK_JUNGLE = sand(AllItems.plankJungle.get(), AllItems.plankJungleSmooth.get(), 32),
            PLANK_SPRUCE = sand(AllItems.plankSpruce.get(), AllItems.plankSpruceSmooth.get(), 32)

                    ;

    GeneratedRecipe sand(Item input, Item sanded, int duration) {
        return create(() -> input, b -> b.duration(duration)
                .output(sanded));
    }

    public SandpaperRecipeGen(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.SANDPAPER;
    }
}
