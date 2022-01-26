package shagejack.industrimania.content.contraptions.materialBase;

import net.minecraft.world.item.crafting.Ingredient;

public interface KnifeMaterial {
    int getDurability();

    int getEnchantmentValue();

    Ingredient getRepairIngredient();

    String getName();

    float getSharpness();
}
