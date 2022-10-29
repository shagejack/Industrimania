package shagejack.industrimania.registries.fluid;

import net.minecraft.tags.FluidTags;
import shagejack.industrimania.registries.record.FluidPair;

public class AllFluids {

    public static final FluidPair rawRubber
            = new FluidBuilder("raw_rubber")
            .tags(FluidTags.WATER)
            .density(1024)
            .viscosity(1024)
            .build();

}
