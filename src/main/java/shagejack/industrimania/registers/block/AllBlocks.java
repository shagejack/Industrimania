package shagejack.industrimania.registers.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.content.contraptions.base.BlockDirectionalBase;
import shagejack.industrimania.content.metallurgyAge.block.smeltery.clayFurnace.ClayFurnaceBottomBlock;
import shagejack.industrimania.content.metallurgyAge.block.smeltery.ironOreSlag.IronOreSlagBlock;
import shagejack.industrimania.registers.AllTabs;
import shagejack.industrimania.registers.AllTags;
import shagejack.industrimania.registers.ItemBlock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllBlocks {

    public static Map<RegistryObject<Block>, List<String>> BLOCK_TAGS = new HashMap<>();

    //Plant Sign
    public static final ItemBlock plant_lactuca_raddeana
            = new BlockBuilder()
            .name("plant_lactuca_raddeana")
            .material(Material.GRASS)
            .strength(0.1F, 0.5F)
            .crossTextureModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem((itemBuilder -> itemBuilder.tab(AllTabs.tabNature)));

    //Ore Cap
    public static final ItemBlock rock_silicon_cap
            = new BlockBuilder()
            .name("rock_silicon_cap")
            .material(Material.STONE)
            .strength(0.5F, 0.5F)
            .tags("mineable/pickaxe")
            //.specialModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem((itemBuilder -> itemBuilder.tab(AllTabs.tabOre)));

    //Igneous Rocks
    public static final ItemBlock rock_dacite
            = new BlockBuilder()
            .name("rock_dacite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_rhyolite
            = new BlockBuilder()
            .name("rock_rhyolite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_trachyte
            = new BlockBuilder()
            .name("rock_trachyte")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_basalt
            = new BlockBuilder()
            .name("rock_basalt")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_gabbro
            = new BlockBuilder()
            .name("rock_gabbro")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_porphyry
            = new BlockBuilder()
            .name("rock_porphyry")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    //Sedimentary Rocks
    public static final ItemBlock rock_sandstone
            = new BlockBuilder()
            .name("rock_sandstone")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones + "NoGen")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_chalk
            = new BlockBuilder()
            .name("rock_chalk")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_limestone
            = new BlockBuilder()
            .name("rock_limestone")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_shale
            = new BlockBuilder()
            .name("rock_shale")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_conglomeratee
            = new BlockBuilder()
            .name("rock_conglomerate")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_dolomite
            = new BlockBuilder()
            .name("rock_dolomite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_mudstone
            = new BlockBuilder()
            .name("rock_mudstone")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_coal
            = new BlockBuilder()
            .name("rock_coal")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_oilshale
            = new BlockBuilder()
            .name("rock_oilshale")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    //Metamorphic rocks
    public static final ItemBlock rock_slate
            = new BlockBuilder()
            .name("rock_slate")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_quartzite
            = new BlockBuilder()
            .name("rock_quartzite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_greisen
            = new BlockBuilder()
            .name("rock_greisen")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_phyllite
            = new BlockBuilder()
            .name("rock_phyllite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_marble
            = new BlockBuilder()
            .name("rock_marble")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_gneiss
            = new BlockBuilder()
            .name("rock_gneiss")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_granulite
            = new BlockBuilder()
            .name("rock_granulite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    //Common Blocks

    public static final ItemBlock building_fine_clay
            = new BlockBuilder()
            .name("building_fine_clay")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem();

    public static final ItemBlock building_scorched_clay
            = new BlockBuilder()
            .name("building_scorched_clay")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem();

    public static final ItemBlock gravity_calcite
            = new BlockBuilder()
            .name("gravity_calcite")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(GravelBlock::new)
            .buildItem();

    public static final ItemBlock gravity_charcoal
            = new BlockBuilder()
            .name("gravity_charcoal")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(GravelBlock::new)
            .buildItem();

    public static final ItemBlock gravity_dust
            = new BlockBuilder()
            .name("gravity_dust")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(GravelBlock::new)
            .buildItem();

    public static final ItemBlock gravity_iron_oxide
            = new BlockBuilder()
            .name("gravity_iron_oxide")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(GravelBlock::new)
            .buildItem();

    public static final ItemBlock mechanic_clay_furnace_bottom
            = new BlockBuilder()
            .name("mechanic_clay_furnace_bottom")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(ClayFurnaceBottomBlock::new)
            .buildItem();

    public static final ItemBlock mechanic_iron_ore_slag
            = new BlockBuilder()
            .name("mechanic_iron_ore_slag")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(IronOreSlagBlock::new)
            .buildItem();

    public static final ItemBlock mechanic_bronze_tube_block
            = new BlockBuilder()
            .name("mechanic_bronze_tube")
            .autoFullCubeModel()
            .rotatableBlockState()
            .buildBlock(BlockDirectionalBase::new)
            .buildItem();

}

