package shagejack.industrimania.content.world.dimension;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class IMAbstractBiomeSource extends BiomeSource {

    protected IMAbstractBiomeSource(Stream<Supplier<Biome>> p_47896_) {
        super(p_47896_);
    }

    protected IMAbstractBiomeSource(List<Biome> p_47894_) {
        super(p_47894_);
    }

    @Override
    protected abstract Codec<? extends BiomeSource> codec();

    @Override
    public abstract BiomeSource withSeed(long p_47916_);

    @Override
    public abstract Biome getNoiseBiome(int p_186735_, int p_186736_, int p_186737_, Climate.Sampler p_186738_);
}
