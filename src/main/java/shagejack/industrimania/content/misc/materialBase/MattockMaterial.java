package shagejack.industrimania.content.misc.materialBase;

import net.minecraft.world.item.crafting.Ingredient;

public interface MattockMaterial {
    int getDurability();

    int getEnchantmentValue();

    Ingredient getRepairIngredient();

    String getName();

    float getSharpness();
}
