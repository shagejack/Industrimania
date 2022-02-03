package shagejack.industrimania.content.world.gen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class CommonBlockReplacerFeature extends Feature<FeatureConfiguration> {

    public CommonBlockReplacerFeature(Codec<FeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<FeatureConfiguration> context) {
        return false;
    }
}
