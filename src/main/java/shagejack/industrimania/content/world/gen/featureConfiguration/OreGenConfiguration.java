package shagejack.industrimania.content.world.gen.featureConfiguration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class OreGenConfiguration implements FeatureConfiguration {

    public static final Codec<OreGenConfiguration> CODEC = RecordCodecBuilder.create((config) -> {
        return config.group(Codec.DOUBLE.fieldOf("genChance").forGetter((c) -> {
            return c.genChance;
        })).apply(config, OreGenConfiguration::new);
    });

    public final double genChance;

    protected OreGenConfiguration(double genChance) {
        this.genChance = genChance;
    }

    public static class OreGenConfigurationBuilder {
        public double genChance = 0.1d; // default deposit gen chance

        public OreGenConfigurationBuilder() {

        }

        public OreGenConfigurationBuilder genChance(double chance) {
            this.genChance = chance;
            return this;
        }

        public OreGenConfigurationBuilder forceGen() {
            this.genChance = 1.0d;
            return this;
        }

        public OreGenConfiguration build() {
            return new OreGenConfiguration(this.genChance);
        }
    }

}
