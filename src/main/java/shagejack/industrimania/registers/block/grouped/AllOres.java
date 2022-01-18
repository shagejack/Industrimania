package shagejack.industrimania.registers.block.grouped;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.Material;
import shagejack.industrimania.content.contraptions.ore.BlockOre;
import shagejack.industrimania.content.worldGen.OreTypeRegistry;
import shagejack.industrimania.registers.AllTabs;
import shagejack.industrimania.registers.AllTags;
import shagejack.industrimania.registers.ItemBlock;
import shagejack.industrimania.registers.block.BlockBuilder;

import java.util.List;

public interface AllOres extends AsBase{

    //TODO: ore block auto registry
    List<String> ROCKS = Lists.newArrayList(
            "rock_andesite",
            "rock_granite",
            "rock_diorite"
    );

    static void initOres() {
        OreTypeRegistry.oreTypeMap.forEach((oreTypeName, oreType) -> {
                    for (String rockName : ROCKS) {
                        for (int grade = 0; grade <= 2; grade++) {
                            String key = rockName + "_" + oreType.name() + "_" + grade;
                            ItemBlock oreBlock
                                    = new BlockBuilder()
                                    .name(key)
                                    .material(Material.STONE)
                                    .strength(AllRocks.ROCKS_HARDNESS.get(rockName), AllRocks.ROCKS_EXPLOSION_RESISTANCE.get(rockName))
                                    .asOre(oreType.name())
                                    .oreTextureModel()
                                    .simpleBlockState()
                                    .renderLayer(() -> RenderType::cutoutMipped)
                                    .buildBlock(BlockOre::new)
                                    .buildItem((ItemBuilder) -> ItemBuilder.tab(AllTabs.tabOre));

                            AllRocks.ORES.put(key, oreBlock);
                        }
                    }
                }
        );
    }

    default BlockBuilder asOre(String oreType) {
        asBase().checkProperty();
        asBase().tags(AllTags.ToolType.pickaxe, AllTags.IndustrimaniaTags.ore, AllTags.IndustrimaniaTags.oreTypeEntry + oreType);
        return (BlockBuilder) this;
    }
}
