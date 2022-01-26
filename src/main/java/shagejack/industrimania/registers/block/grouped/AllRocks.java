package shagejack.industrimania.registers.block.grouped;

import com.google.common.collect.Lists;
import shagejack.industrimania.registers.ItemBlock;

import java.util.*;

public interface AllRocks extends AsBase{

    //TODO: ore block auto registry
    List<String> ROCKS_NAME = Lists.newArrayList(
    );

    Map<String, Float> ROCKS_HARDNESS = new HashMap<>();
    Map<String, Float> ROCKS_EXPLOSION_RESISTANCE = new HashMap<>();

    // stone block replacements that are Sedimentary
    List<ItemBlock> sedimentaryStones = Lists.newArrayList(
    );
    // stone block replacements that are Metamorphic
    List<ItemBlock> metamorphicStones = Lists.newArrayList(
    );
    // stone block replacements that are Igneous
    List<ItemBlock> igneousStones = Lists.newArrayList(
    );

    List<ItemBlock> ROCKS = new ArrayList<>();

    static void initRocks(){
        ROCKS.addAll(sedimentaryStones);
        ROCKS.addAll(metamorphicStones);
        ROCKS.addAll(igneousStones);
    }
}
