package shagejack.industrimania.registers.block.grouped;

import com.google.common.collect.Lists;
import shagejack.industrimania.registers.ItemBlock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AllRocks extends AsBase{

    //TODO: ore block auto registry
    List<String> ROCKS = Lists.newArrayList(
            "rock_andesite",
            "rock_granite",
            "rock_diorite"
    );
    Map<String, ItemBlock> ORES = new HashMap<>();
    Map<String, Float> ROCKS_HARDNESS = new HashMap<>();
    Map<String, Float> ROCKS_EXPLOSION_RESISTANCE = new HashMap<>();

    static void initRocks(){
        AllRocks.ROCKS_HARDNESS.put("rock_andesite", 1.5F);
        AllRocks.ROCKS_HARDNESS.put("rock_granite", 1.5F);
        AllRocks.ROCKS_HARDNESS.put("rock_diorite", 1.5F);
        AllRocks.ROCKS_EXPLOSION_RESISTANCE.put("rock_andesite", 6.0F);
        AllRocks.ROCKS_EXPLOSION_RESISTANCE.put("rock_granite", 6.0F);
        AllRocks.ROCKS_EXPLOSION_RESISTANCE.put("rock_diorite", 6.0F);
    }
}
