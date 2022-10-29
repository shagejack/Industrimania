package shagejack.industrimania.registries.item.grouped;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBase;
import shagejack.industrimania.registries.AllTabs;
import shagejack.industrimania.registries.block.grouped.AllRocks;
import shagejack.industrimania.registries.item.ItemBuilder;

import java.util.HashMap;
import java.util.Map;

public interface AllRockPieces extends AsBase {
    Map<String, RegistryObject<Item>> ORE_PIECES = new HashMap<>();

    Map<String, Float> ROCK_PIECES = new HashMap<>();

    static void initRockPieces() {
        for (String rockName : AllRocks.ROCKS_NAME) {
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
