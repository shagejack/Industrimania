package shagejack.industrimania.content.world.gen;

import shagejack.industrimania.content.world.gen.record.Ore;

public class OreParagenesisRegistry {
    //the paragenesis of ores doesn't need availableRock, minY and maxY, but chanceAsParagenesis is necessary

    //sphalerite
    public static final Ore sphalerite = new Ore(OreTypeRegistry.sphalerite, null, 0, 0, 0.25, null, null);

}
