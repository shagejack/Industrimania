package shagejack.industrimania.content.misc.materialBase;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum MattockMaterials implements MattockMaterial {



    ;

    private final String name;
    private final int durability;
    private final int enchantmentValue;
    private final float sharpness;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    MattockMaterials(String name, int durability, int enchantmentValue, float sharpness, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durability = durability;
        this.enchantmentValue = enchantmentValue;
        this.sharpness = sharpness;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    @Override
    public int getDurability() {
        return this.durability;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public float getSharpness() {
        return this.sharpness;
    }
}
