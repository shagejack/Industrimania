package shagejack.industrimania.content.contraptions.materialBase;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum KnifeMaterials implements KnifeMaterial {
    BONE("iron", 15, 9, 0.5F, null),
    FLINT("iron", 15, 9, 1.0F, null),
    IRON("iron", 15, 9, 2.0F, () -> Ingredient.of(Items.IRON_INGOT)),
    GOLD("gold", 7, 25, 1.0F, () -> Ingredient.of(Items.GOLD_INGOT)),
    DIAMOND("diamond", 33, 10, 4.0F, () -> Ingredient.of(Items.DIAMOND)),
    NETHERITE("netherite", 37, 15, 5.0F, () -> Ingredient.of(Items.NETHERITE_INGOT));

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durability;
    private final int enchantmentValue;
    private final float sharpness;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    private KnifeMaterials(String name, int durability, int enchantmentValue, float sharpness, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durability = durability;
        this.enchantmentValue = enchantmentValue;
        this.sharpness = sharpness;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    public int getDurability() {
        return this.durability;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public String getName() {
        return this.name;
    }

    public float getSharpness() {
        return this.sharpness;
    }

}
