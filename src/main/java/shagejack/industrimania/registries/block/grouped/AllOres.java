package shagejack.industrimania.registries.block.grouped;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import shagejack.industrimania.content.misc.ore.BlockOre;
import shagejack.industrimania.content.misc.ore.BlockOreItem;
import shagejack.industrimania.content.world.gen.OreGrade;
import shagejack.industrimania.content.world.gen.OreTypeRegistry;
import shagejack.industrimania.content.world.gen.record.OreType;
import shagejack.industrimania.registries.AllTabs;
import shagejack.industrimania.registries.tags.AllTags;
import shagejack.industrimania.registries.record.ItemBlock;
import shagejack.industrimania.registries.block.BlockBuilder;

import java.util.HashMap;
import java.util.Map;

public interface AllOres extends AsBase{

    Map<String, ItemBlock> ORES = new HashMap<>();

    static void initOres() {
        OreTypeRegistry.oreTypeMap.forEach((oreTypeName, oreType) -> {
                    for (String rockName : AllRocks.ROCKS_NAME) {
                        for (int grade = 0; grade <= 2; grade++) {
                            String key = rockName + "_" + oreType.name() + "_" + grade;
                            ItemBlock oreBlock
                                    = new BlockBuilder()
                                    .name(key)
                                    .material(Material.STONE)
                                    .properties(properties -> properties.strength(AllRocks.ROCKS_HARDNESS.get(rockName), AllRocks.ROCKS_EXPLOSION_RESISTANCE.get(rockName)))
                                    .asOre(oreType)
                                    .oreTextureModel()
                                    .simpleBlockState()
                                    .renderLayer(() -> RenderType::cutoutMipped)
                                    .buildBlock(BlockOre::new)
                                    .blockItemFactory(BlockOreItem::new)
                                    .buildItem((ItemBuilder) -> ItemBuilder.tab(AllTabs.tabOre));

                            ORES.put(key, oreBlock);
                        }
                    }
                }
        );
    }

    default BlockBuilder asOre(OreType oreType) {
        asBase().checkName();
        asBase().tags(AllTags.ToolType.pickaxe, AllTags.IndustrimaniaTags.ore, AllTags.IndustrimaniaTags.oreTypeEntry + oreType.name(), AllTags.IndustrimaniaToolTier.miningLevelTag(oreType.harvestLevel()));
        asBase().properties(BlockBehaviour.Properties::requiresCorrectToolForDrops);
        return (BlockBuilder) this;
    }

    static String getRockName(String string) {
        String[] rockName = string.split("_");
        return rockName[0] + "_" + rockName[1];
    }

    static String getOreType(String string) {
        return string.split("_")[2];
    }

    static OreGrade getOreGrade(String string) {
        return OreGrade.getGradeFromIndex(Integer.parseInt(string.split("_")[3]));
    }
}
