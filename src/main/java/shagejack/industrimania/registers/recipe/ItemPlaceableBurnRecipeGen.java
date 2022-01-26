package shagejack.industrimania.registers.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registers.AllRecipeTypes;
import shagejack.industrimania.registers.item.AllItems;

import java.util.List;

public class ItemPlaceableBurnRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe

    xxx

                    ;

    GeneratedRecipe burn(List<Item> input, List<Item> output, int time) {
        return create(Industrimania.MOD_ID, b -> b.duration(time)
                .require(input.get(0))
                .require(input.get(1))
                .require(input.get(2))
                .require(input.get(3))
                .require(input.get(4))
                .require(input.get(5))
                .require(input.get(6))
                .require(input.get(7))
                .require(input.get(8))
                .require(input.get(9))
                .require(input.get(10))
                .require(input.get(11))
                .require(input.get(12))
                .require(input.get(13))
                .require(input.get(14))
                .require(input.get(15))
                .output(output.get(0))
                .output(output.get(1))
                .output(output.get(2))
                .output(output.get(3))
                .output(output.get(4))
                .output(output.get(5))
                .output(output.get(6))
                .output(output.get(7))
                .output(output.get(8))
                .output(output.get(9))
                .output(output.get(10))
                .output(output.get(11))
                .output(output.get(12))
                .output(output.get(13))
                .output(output.get(14))
                .output(output.get(15)));
    }

    public ItemPlaceableBurnRecipeGen(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.DRYING_RACK;
    }

}
