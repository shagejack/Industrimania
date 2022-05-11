package shagejack.industrimania.registers.block.grouped;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.registers.block.AllBlocks;
import shagejack.industrimania.registers.record.ItemBlock;

import java.util.*;

public interface AllRocks extends AsBase{

    //TODO: ore block auto registry
    List<String> ROCKS_NAME = Lists.newArrayList(
    );

    Map<String, Float> ROCKS_HARDNESS = new HashMap<>();
    Map<String, Float> ROCKS_EXPLOSION_RESISTANCE = new HashMap<>();

    // stone block replacements that are Sedimentary
    List<ItemBlock> sedimentaryStones = Lists.newArrayList();
    // stone block replacements that are Metamorphic
    List<ItemBlock> metamorphicStones = Lists.newArrayList();
    // stone block replacements that are Igneous
    List<ItemBlock> igneousStones = Lists.newArrayList();

    List<ItemBlock> ROCKS = new ArrayList<>();

    List<ItemBlock> rareStones = Lists.newArrayList();

    static void initRocks(){
        ROCKS.addAll(sedimentaryStones);
        ROCKS.addAll(metamorphicStones);
        ROCKS.addAll(igneousStones);

        // set stone rare
        rareStones.add(AllBlocks.rock_coal);
        rareStones.add(AllBlocks.rock_oilshale);
    }

    static Block getRockFromName(String name) {
        ResourceLocation registryName = new ResourceLocation(Industrimania.MOD_ID, "rock_" + name);
        return ForgeRegistries.BLOCKS.getValue(registryName);
    }
}
