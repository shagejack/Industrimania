package shagejack.industrimania.registries.item.grouped;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.content.contraptions.ore.ItemOreChunk;
import shagejack.industrimania.content.world.gen.OreTypeRegistry;
import shagejack.industrimania.registries.AllTabs;
import shagejack.industrimania.registries.block.grouped.AllRocks;
import shagejack.industrimania.registries.item.ItemBuilder;

import java.util.HashMap;
import java.util.Map;

public interface AllOreChunks extends AsBase{

    Map<String, RegistryObject<Item>> ORE_CHUNKS = new HashMap<>();

    static void initOreChunks() {
        OreTypeRegistry.oreTypeMap.forEach( (oreTypeName, oreType) -> {
               for(String rockName : AllRocks.ROCKS_NAME) {
                    for (int grade = 0; grade <= 2; grade ++) {
                        String key = rockName + "_" + oreType.name() + "_" + grade;
                        RegistryObject<Item> oreChunk
                                = new ItemBuilder()
                                .name("chunk_" + key)
                                .tab(AllTabs.tabOre)
                                .simpleModel("chunk_" + key)
                                .build(ItemOreChunk::new);

                        ORE_CHUNKS.put(key, oreChunk);
                    }
               }
            }
        );
    }
}
