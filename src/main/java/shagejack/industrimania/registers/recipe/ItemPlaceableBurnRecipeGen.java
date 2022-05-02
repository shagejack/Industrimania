package shagejack.industrimania.registers.recipe;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registers.AllRecipeTypes;
import shagejack.industrimania.registers.item.AllItems;

import java.util.List;

public class ItemPlaceableBurnRecipeGen extends ProcessingRecipeGen {

    GeneratedRecipe xxx



                    ;

    GeneratedRecipe burn(List<Ingredient> input, Block output, int time) {
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
                .output(output));
    }

    public ItemPlaceableBurnRecipeGen(DataGenerator p_i48262_1_) {
        super(p_i48262_1_);
    }

    @Override
    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.ITEM_PLACEABLE_BURN;
    }

}
