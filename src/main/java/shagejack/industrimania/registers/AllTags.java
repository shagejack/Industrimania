package shagejack.industrimania.registers;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import shagejack.industrimania.Industrimania;

import java.util.List;
import java.util.function.Function;

public class AllTags {

    public static class ToolType {
        public static final String pickaxe = "mineable/pickaxe";
    }

    public static class IndustrimaniaToolType {
        public static final String saw = "industrimania:mineable/saw";
        public static final String knife = "industrimania:mineable/knife";
    }

    public static class IndustrimaniaMaterial {
        public static final String flint = "industrimania:items/flint";
    }

    public static class IndustrimaniaToolTier {
        public static String miningLevelTag(int level) {
            return "industrimania:need_mining_level_" + level;
        }
    }

    public static class IndustrimaniaTags {
        public static final String rock = "industrimania:rock";
        public static final String ore = "industrimania:ore";
        public static final String oreTypeEntry = "industrimania:ore_type/";
        public static final String sedimentaryStones = "industrimania:sedimentary_stones";
        public static final String metamorphicStones = "industrimania:metamorphic_stones";
        public static final String igneousStones = "industrimania:igneous_stones";

        public static final String rockPiece = "industrimania:rock_piece";
        public static final String charcoalIngredient = "industrimania:charcoal_ingredient";
    }

    public static <T> TagKey<T> tag(Function<ResourceLocation, TagKey<T>> wrapperFactory, String namespace,
                                    String path) {
        return wrapperFactory.apply(new ResourceLocation(namespace, path));
    }

    public static <T> TagKey<T> forgeTag(Function<ResourceLocation, TagKey<T>> wrapperFactory, String path) {
        return tag(wrapperFactory, "forge", path);
    }

    public static TagKey<Block> forgeBlockTag(String path) {
        return forgeTag(BlockTags::create, path);
    }

    public static TagKey<Item> forgeItemTag(String path) {
        return forgeTag(ItemTags::create, path);
    }

    public static TagKey<Fluid> forgeFluidTag(String path) {
        return forgeTag(FluidTags::create, path);
    }

    public static <T> TagKey<T> modTag(Function<ResourceLocation, TagKey<T>> wrapperFactory, String path) {
        return wrapperFactory.apply(new ResourceLocation(path));
    }

    public static TagKey<Block> modBlockTag(String path) {
        return modTag(BlockTags::create, path);
    }

    public static TagKey<Item> modItemTag(String path) {
        return modTag(ItemTags::create, path);
    }

    public static TagKey<Fluid> modFluidTag(String path) {
        return modTag(FluidTags::create, path);
    }

}
