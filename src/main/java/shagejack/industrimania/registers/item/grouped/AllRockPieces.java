package shagejack.industrimania.registers.item.grouped;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.content.contraptions.ore.BlockOre;
import shagejack.industrimania.content.contraptions.ore.ItemOreChunk;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBase;
import shagejack.industrimania.registers.AllTabs;
import shagejack.industrimania.registers.ItemBlock;
import shagejack.industrimania.registers.block.BlockBuilder;
import shagejack.industrimania.registers.block.grouped.AllRocks;
import shagejack.industrimania.registers.item.AllItems;
import shagejack.industrimania.registers.item.ItemBuilder;

import java.util.HashMap;
import java.util.Map;

public interface AllRockPieces extends AsBase {
    Map<String, RegistryObject<Item>> ORE_PIECES = new HashMap<>();

    Map<String, Float> ROCK_PIECES = new HashMap<>();

    static void initRockPieces() {
        for (String rockName : AllRocks.ROCKS) {
            String key = "piece_" + rockName;
            RegistryObject<Item> pieceRock
                    = new ItemBuilder()
                    .name(key)
                    .tab(AllTabs.tabRock)
                    .simpleModel("chunk_" + key)
                    .build(ItemPlaceableBase::new);

            ORE_PIECES.put(key, pieceRock);
        }
    }

}
