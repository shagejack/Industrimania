package shagejack.industrimania.registers.block.grouped;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import shagejack.industrimania.content.contraptions.ore.BlockOre;
import shagejack.industrimania.content.worldGen.OreTypeRegistry;
import shagejack.industrimania.registers.AllTabs;
import shagejack.industrimania.registers.ItemBlock;
import shagejack.industrimania.registers.block.BlockBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AllRocks extends AsBase{

    //TODO: ore block auto registry
    List<String> ROCKS = Lists.newArrayList(
    );

    Map<String, Float> ROCKS_HARDNESS = new HashMap<>();
    Map<String, Float> ROCKS_EXPLOSION_RESISTANCE = new HashMap<>();

    // stone block replacements that are Sedimentary
    public static final List<ItemBlock> sedimentaryStones = Lists.newArrayList(
    );
    // stone block replacements that are Metamorphic
    public static final List<ItemBlock> metamorphicStones = Lists.newArrayList(
    );
    // stone block replacements that are Igneous
    public static final List<ItemBlock> igneousStones = Lists.newArrayList(
    );


    static void initRocks(){

    }
}
