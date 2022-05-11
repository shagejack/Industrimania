package shagejack.industrimania.content.world.nature.rubberTree;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import shagejack.industrimania.registers.AllFeatures;

import java.util.Random;

public class RubberTreeGrower extends AbstractTreeGrower {

    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean p_60023_) {
        return AllFeatures.RUBBER_TREE;
    }

}
