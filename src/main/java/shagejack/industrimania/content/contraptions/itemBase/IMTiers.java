package shagejack.industrimania.content.contraptions.itemBase;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registers.AllTags;

import java.util.function.Supplier;

public enum IMTiers implements Tier {
    FLINT(0, 59, 2.0F, 0.0F, 15, () -> Ingredient.of(AllTags.modItemTag(AllTags.IndustrimaniaMaterial.flint)));

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    IMTiers(int p_43332_, int p_43333_, float p_43334_, float p_43335_, int p_43336_, Supplier<Ingredient> p_43337_) {
        this.level = p_43332_;
        this.uses = p_43333_;
        this.speed = p_43334_;
        this.damage = p_43335_;
        this.enchantmentValue = p_43336_;
        this.repairIngredient = new LazyLoadedValue<>(p_43337_);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @javax.annotation.Nullable public TagKey<Block> getTag() { return getTagFromTier(this); }

    public static TagKey<Block> getTagFromTier(IMTiers tier)
    {
        return AllTags.modBlockTag(AllTags.IndustrimaniaToolTier.miningLevelTag(tier.getLevel()));
    }

}
