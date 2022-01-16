package shagejack.industrimania.content.worldGen;

import com.google.common.collect.Lists;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import shagejack.industrimania.content.worldGen.record.Ore;
import shagejack.industrimania.registers.AllBlocks;

import java.util.ArrayList;
import java.util.List;

public class OreRegistry {

    //hematite
    public static final List<Block> rockHematite = Lists.newArrayList(
            Blocks.ANDESITE,
            AllBlocks.rock_basalt.block().get(),
            AllBlocks.rock_dacite.block().get(),
            AllBlocks.rock_rhyolite.block().get()
    );
    public static final Ore hematite = new Ore(OreTypeRegistry.hematite, rockHematite, -32, 32, 0, AllBlocks.lactuca_raddeana.block().get(), null);


    //galena
    public static final List<Block> rockGalena = Lists.newArrayList(
            Blocks.DEEPSLATE,
            AllBlocks.rock_marble.block().get(),
            AllBlocks.rock_phyllite.block().get(),
            AllBlocks.rock_gneiss.block().get(),
            AllBlocks.rock_quartzite.block().get()
    );
    public static final Ore galena = new Ore(OreTypeRegistry.galena, rockGalena, -16, 48, 0, null, AllBlocks.silicon_cap.block().get(), OreParagenesisRegistry.sphalerite);

}
