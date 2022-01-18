package shagejack.industrimania.registers.item.grouped;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.content.contraptions.ore.ItemOreChunk;
import shagejack.industrimania.content.worldGen.OreTypeRegistry;
import shagejack.industrimania.registers.AllTabs;
import shagejack.industrimania.registers.block.grouped.AllOres;
import shagejack.industrimania.registers.item.AllItems;
import shagejack.industrimania.registers.item.ItemBuilder;

public interface AllOre extends AsBase{

    static void initOres() {
        OreTypeRegistry.oreTypeMap.forEach( (oreTypeName, oreType) -> {
               for(String rockName : AllOres.ROCKS) {
                    for (int grade = 0; grade <= 2; grade ++) {
                        String key = rockName + "_" + oreType.name() + "_" + grade;
                        RegistryObject<Item> oreChunk
                                = new ItemBuilder()
                                .name("chunk_" + key)
                                .tab(AllTabs.tabOre)
                                .simpleModel("chunk_" + key)
                                .build(ItemOreChunk::new);

                        AllItems.ORE_CHUNKS.put(key, oreChunk);
                    }
               }
            }
        );
    }
}
