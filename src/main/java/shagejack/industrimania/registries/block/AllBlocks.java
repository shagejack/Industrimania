package shagejack.industrimania.registries.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import shagejack.industrimania.content.misc.blockBase.BlockDirectionalBase;
import shagejack.industrimania.content.metallurgyAge.block.smeltery.clayFurnace.ClayFurnaceBottomBlock;
import shagejack.industrimania.content.metallurgyAge.block.smeltery.ironOreSlag.IronOreSlagBlock;
import shagejack.industrimania.content.pollution.block.BlockAshes;
import shagejack.industrimania.content.pollution.block.BlockAshesLayers;
import shagejack.industrimania.content.primalAge.block.clayKiln.ClayKilnBlock;
import shagejack.industrimania.content.primalAge.block.dryingRack.DryingRackBlock;
import shagejack.industrimania.content.primalAge.block.mixingBasin.MixingBasinBlock;
import shagejack.industrimania.content.world.nature.OreCapBlock;
import shagejack.industrimania.content.world.nature.cobble.Cobble;
import shagejack.industrimania.content.world.nature.mulberry.bush.MulberryBush;
import shagejack.industrimania.content.world.nature.mulberry.silkworm.SilkwormRearingBoxBlock;
import shagejack.industrimania.content.world.nature.mulberry.tree.MulberryTreeLeaves;
import shagejack.industrimania.content.world.nature.mulberry.tree.MulberryTreeLog;
import shagejack.industrimania.content.world.nature.mulberry.tree.MulberryTreeSapling;
import shagejack.industrimania.content.world.nature.rush.RushBlockBottom;
import shagejack.industrimania.content.world.nature.rush.RushBlockTop;
import shagejack.industrimania.content.world.nature.rubberTree.RubberTreeLeaves;
import shagejack.industrimania.content.world.nature.rubberTree.RubberTreeLog;
import shagejack.industrimania.content.world.nature.rubberTree.RubberTreeSapling;
import shagejack.industrimania.content.primalAge.block.simpleCraftingTable.SimpleCraftingTableBlock;
import shagejack.industrimania.content.primalAge.block.stack.GrassStackBlock;
import shagejack.industrimania.content.primalAge.block.stack.hay.HayStackBlock;
import shagejack.industrimania.content.primalAge.block.stack.moldyGrass.MoldyGrassStackBlock;
import shagejack.industrimania.content.primalAge.block.stack.rottenGrass.RottenGrassStackBlock;
import shagejack.industrimania.content.primalAge.block.stoneChoppingBoard.StoneChoppingBoardBlock;
import shagejack.industrimania.content.primalAge.block.woodenBarrel.WoodenBarrelBlock;
import shagejack.industrimania.content.primalAge.block.woodenFaucet.WoodenFaucetBlock;
import shagejack.industrimania.content.primalAge.item.handOilLamp.FakeAirLightBlock;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBaseBlock;
import shagejack.industrimania.content.steamAge.block.boiler.BoilerBlock;
import shagejack.industrimania.registries.AllTabs;
import shagejack.industrimania.registries.tags.AllTags;
import shagejack.industrimania.registries.record.ItemBlock;
import shagejack.industrimania.registries.item.ItemBuilder;

import java.awt.*;
import java.util.function.Function;

public class AllBlocks {

    private static final Function<ItemBuilder, ItemBuilder> DO_NOTHING = builder -> builder;

    //Pollution Blocks
    public static final ItemBlock pollution_ashes_block
            = new BlockBuilder()
            .name("pollution_ashes_block")
            .material(Material.SNOW)
            .properties(p -> p.sound(SoundType.SNOW).strength(0.2F, 0.0F))
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(BlockAshes::new)
            .buildItem();

    public static final ItemBlock pollution_ashes_layers
            = new BlockBuilder()
            .name("pollution_ashes_layers")
            .material(Material.SNOW)
            .properties(p -> p.sound(SoundType.SNOW).strength(0.2F, 0.0F))
            .snowLikeModel()
            .snowLikeBlockState()
            .buildBlock(BlockAshesLayers::new)
            .buildItem();

    //Common Blocks

    /*
     * =============
     *  Primal Age
     * =============
     */

    //building
    public static final ItemBlock building_fine_clay
            = new BlockBuilder()
            .name("building_fine_clay")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabBuilding));

    public static final ItemBlock building_scorched_clay
            = new BlockBuilder()
            .name("building_scorched_clay")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabBuilding));

    public static final ItemBlock building_grass_stack
            = new BlockBuilder()
            .name("building_grass_stack")
            .autoFullCubeModel()
            .rotatablePillarBlockState()
            .properties(BlockBehaviour.Properties::randomTicks)
            .buildBlock(GrassStackBlock::new)
            .buildItem(builder -> builder.tab(AllTabs.tabBuilding));

    public static final ItemBlock building_hay_stack
            = new BlockBuilder()
            .name("building_hay_stack")
            .autoFullCubeModel()
            .rotatablePillarBlockState()
            .properties(BlockBehaviour.Properties::randomTicks)
            .buildBlock(HayStackBlock::new)
            .buildItem(builder -> builder.tab(AllTabs.tabBuilding));

    public static final ItemBlock building_moldy_grass_stack
            = new BlockBuilder()
            .name("building_moldy_grass_stack")
            .autoFullCubeModel()
            .rotatablePillarBlockState()
            .properties(BlockBehaviour.Properties::randomTicks)
            .buildBlock(MoldyGrassStackBlock::new)
            .buildItem(builder -> builder.tab(AllTabs.tabBuilding));

    public static final ItemBlock building_rotten_grass_stack
            = new BlockBuilder()
            .name("building_rotten_grass_stack")
            .autoFullCubeModel()
            .rotatablePillarBlockState()
            .buildBlock(RottenGrassStackBlock::new)
            .buildItem(builder -> builder.tab(AllTabs.tabBuilding));


    public static final ItemBlock building_white_obsolete_concrete = asDecoration("building_white_obsolete_concrete");
    public static final ItemBlock building_orange_obsolete_concrete = asDecoration("building_orange_obsolete_concrete");
    public static final ItemBlock building_magenta_obsolete_concrete = asDecoration("building_magenta_obsolete_concrete");
    public static final ItemBlock building_light_blue_obsolete_concrete = asDecoration("building_light_blue_obsolete_concrete");
    public static final ItemBlock building_yellow_obsolete_concrete = asDecoration("building_yellow_obsolete_concrete");
    public static final ItemBlock building_lime_obsolete_concrete = asDecoration("building_lime_obsolete_concrete");
    public static final ItemBlock building_pink_obsolete_concrete = asDecoration("building_pink_obsolete_concrete");
    public static final ItemBlock building_gray_obsolete_concrete = asDecoration("building_gray_obsolete_concrete");
    public static final ItemBlock building_light_gray_obsolete_concrete = asDecoration("building_light_gray_obsolete_concrete");
    public static final ItemBlock building_cyan_obsolete_concrete = asDecoration("building_cyan_obsolete_concrete");
    public static final ItemBlock building_purple_obsolete_concrete = asDecoration("building_purple_obsolete_concrete");
    public static final ItemBlock building_blue_obsolete_concrete = asDecoration("building_blue_obsolete_concrete");
    public static final ItemBlock building_brown_obsolete_concrete = asDecoration("building_brown_obsolete_concrete");
    public static final ItemBlock building_green_obsolete_concrete = asDecoration("building_green_obsolete_concrete");
    public static final ItemBlock building_red_obsolete_concrete = asDecoration("building_red_obsolete_concrete");
    public static final ItemBlock building_black_obsolete_concrete = asDecoration("building_black_obsolete_concrete");

    public static final ItemBlock building_white_peeling_concrete = asDecoration("building_white_peeling_concrete");
    public static final ItemBlock building_orange_peeling_concrete = asDecoration("building_orange_peeling_concrete");
    public static final ItemBlock building_magenta_peeling_concrete = asDecoration("building_magenta_peeling_concrete");
    public static final ItemBlock building_light_blue_peeling_concrete = asDecoration("building_light_blue_peeling_concrete");
    public static final ItemBlock building_yellow_peeling_concrete = asDecoration("building_yellow_peeling_concrete");
    public static final ItemBlock building_lime_peeling_concrete = asDecoration("building_lime_peeling_concrete");
    public static final ItemBlock building_pink_peeling_concrete = asDecoration("building_pink_peeling_concrete");
    public static final ItemBlock building_gray_peeling_concrete = asDecoration("building_gray_peeling_concrete");
    public static final ItemBlock building_light_gray_peeling_concrete = asDecoration("building_light_gray_peeling_concrete");
    public static final ItemBlock building_cyan_peeling_concrete = asDecoration("building_cyan_peeling_concrete");
    public static final ItemBlock building_purple_peeling_concrete = asDecoration("building_purple_peeling_concrete");
    public static final ItemBlock building_blue_peeling_concrete = asDecoration("building_blue_peeling_concrete");
    public static final ItemBlock building_brown_peeling_concrete = asDecoration("building_brown_peeling_concrete");
    public static final ItemBlock building_green_peeling_concrete = asDecoration("building_green_peeling_concrete");
    public static final ItemBlock building_red_peeling_concrete = asDecoration("building_red_peeling_concrete");
    public static final ItemBlock building_black_peeling_concrete = asDecoration("building_black_peeling_concrete");


    //gravity
    public static final ItemBlock gravity_hot_gravel
            = new BlockBuilder()
            .name("gravity_hot_gravel")
            .material(Material.SAND)
            .properties(p -> p.sound(SoundType.GRAVEL))
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(GravelBlock::new)
            .buildItem(builder -> builder.tab(AllTabs.tabBuilding));

    public static final ItemBlock gravity_calcite
            = new BlockBuilder()
            .name("gravity_calcite")
            .material(Material.SAND)
            .properties(p -> p.sound(SoundType.GRAVEL))
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(GravelBlock::new)
            .buildItem(builder -> builder.tab(AllTabs.tabBuilding));

    public static final ItemBlock gravity_charcoal
            = new BlockBuilder()
            .name("gravity_charcoal")
            .material(Material.SAND)
            .properties(p -> p.sound(SoundType.GRAVEL))
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(GravelBlock::new)
            .buildItem(builder -> builder.tab(AllTabs.tabBuilding));

    public static final ItemBlock gravity_dust
            = new BlockBuilder()
            .name("gravity_dust")
            .material(Material.SAND)
            .properties(p -> p.sound(SoundType.SAND))
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(GravelBlock::new)
            .buildItem(builder -> builder.tab(AllTabs.tabBuilding));

    public static final ItemBlock gravity_iron_oxide
            = new BlockBuilder()
            .name("gravity_iron_oxide")
            .material(Material.SAND)
            .properties(p -> p.sound(SoundType.GRAVEL))
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(GravelBlock::new)
            .buildItem(builder -> builder.tab(AllTabs.tabBuilding));

    //mechanic
    public static final ItemBlock mechanic_fake_air_light
            = new BlockBuilder()
            .name("mechanic_fake_air_light")
            .material(Material.AIR)
            .properties(p -> p.isViewBlocking(AllBlocks::never).isSuffocating(AllBlocks::never).isValidSpawn(AllBlocks::ocelotOrParrot).randomTicks().noCollission().noDrops().lightLevel(FakeAirLightBlock.LIGHT_EMISSION).air())
            .renderLayer(() -> RenderType::cutoutMipped)
            .simplePresetModel()
            .simpleBlockState()
            .buildBlock(FakeAirLightBlock::new)
            .buildItem();

    public static final ItemBlock mechanic_silkworm_rearing_box
            = new BlockBuilder()
            .name("mechanic_silkworm_rearing_box")
            .material(Material.WOOD)
            .properties(p -> p.sound(SoundType.WOOD))
            .simplePresetModel()
            .simpleBlockState()
            .buildBlock(SilkwormRearingBoxBlock::new)
            .buildItem();

    public static final ItemBlock mechanic_simple_crafting_table
            = new BlockBuilder()
            .name("mechanic_simple_crafting_table")
            .material(Material.WOOD)
            .properties(p -> p.sound(SoundType.WOOD))
            .simplePresetModel()
            .rotatableBlockState()
            .buildBlock(SimpleCraftingTableBlock::new)
            .buildItem();

    public static final ItemBlock mechanic_stone_chopping_block
            = new BlockBuilder()
            .name("mechanic_stone_chopping_block")
            .simplePresetModel()
            .simpleBlockState()
            .buildBlock(StoneChoppingBoardBlock::new)
            .buildItem();

    public static final ItemBlock mechanic_drying_rack
            = new BlockBuilder()
            .name("mechanic_drying_rack")
            .material(Material.WOOD)
            .properties(p -> p.sound(SoundType.WOOD))
            .simplePresetModel()
            .simpleBlockState()
            .renderLayer(() -> RenderType::cutout)
            .buildBlock(DryingRackBlock::new)
            .buildItem();

    public static final ItemBlock mechanic_clay_kiln
            = new BlockBuilder()
            .name("mechanic_clay_kiln")
            .material(Material.CLAY)
            .properties(p -> p.sound(SoundType.GRAVEL))
            .simplePresetModel()
            .simpleBlockState()
            .renderLayer(() -> RenderType::cutout)
            .buildBlock(ClayKilnBlock::new)
            .buildItem();

    public static final ItemBlock mechanic_mixing_basin
            = new BlockBuilder()
            .name("mechanic_mixing_basin")
            .material(Material.STONE)
            .simplePresetModel()
            .simpleBlockState()
            .renderLayer(() -> RenderType::cutout)
            .buildBlock(MixingBasinBlock::new)
            .buildItem();

    public static final ItemBlock mechanic_wooden_faucet
            = new BlockBuilder()
            .name("mechanic_wooden_faucet")
            .material(Material.WOOD)
            .properties(p -> p.sound(SoundType.WOOD))
            .renderLayer(() -> RenderType::cutout)
            .buildBlock(WoodenFaucetBlock::new)
            .presetItemModel()
            .buildItem();

    public static final ItemBlock mechanic_wooden_barrel
            = new BlockBuilder()
            .name("mechanic_wooden_barrel")
            .material(Material.WOOD)
            .properties(p -> p.sound(SoundType.WOOD).noDrops())
            .presetBinaryModel()
            .binaryBlockState(WoodenBarrelBlock.OPEN)
            .renderLayer(() -> RenderType::cutout)
            .buildBlock(WoodenBarrelBlock::new)
            .presetItemModel()
            .buildItem(builder -> builder.maxStackSize(1));

    public static final ItemBlock mechanic_item_placeable
            = new BlockBuilder()
            .name("mechanic_item_placeable")
            .simplePresetModel()
            .simpleBlockState()
            .renderLayer(() -> RenderType::cutout)
            .buildBlock(ItemPlaceableBaseBlock::new)
            .buildItem(ItemBuilder::noTab);

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

    /*
     * ======================
     *    Liquid Block
     * ======================
     */

    //TODO: Liquid Block Model and State Datagen


    /*
    * ============================================
    *    World Generation And Natural Resources
    * ============================================
     */

    public static final ItemBlock nature_clay_terracotta
            = new BlockBuilder()
            .name("nature_clay_terracotta")
            .material(Material.CLAY)
            .properties(p -> p.sound(SoundType.GRAVEL).strength(0.6F))
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabNature));

    public static final ItemBlock nature_clay_terramelas
            = new BlockBuilder()
            .name("nature_clay_terramelas")
            .material(Material.CLAY)
            .properties(p -> p.sound(SoundType.GRAVEL).strength(0.6F))
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabNature));

    public static final ItemBlock nature_cobble
            = new BlockBuilder()
            .name("nature_cobble")
            .material(Material.STONE)
            .properties(p -> p.strength(0.3F).isValidSpawn(AllBlocks::ocelotOrParrot).isSuffocating(AllBlocks::never).isViewBlocking(AllBlocks::never).noOcclusion())
            .renderLayer(() -> RenderType::cutout)
            .simplePresetModel()
            .buildBlock(Cobble::new)
            .customItemName("cobble")
            .buildItem(itemBuilder -> itemBuilder.tab(AllTabs.tabNature));

    //Tree
    public static final ItemBlock nature_rubber_tree_log
            = new BlockBuilder()
            .name("nature_rubber_tree_log")
            .material(Material.WOOD)
            .properties(p -> p.sound(SoundType.WOOD).strength(1.0F, 0.5F))
            .tags(BlockTags.LOGS)
            .autoFullCubeModel()
            .rotatablePillarBlockState()
            .buildBlock(RubberTreeLog::new)
            .buildItem(itemBuilder -> itemBuilder.tab(AllTabs.tabNature));

    public static final ItemBlock nature_rubber_tree_plank
            = new BlockBuilder()
            .name("nature_rubber_tree_plank")
            .material(Material.WOOD)
            .properties(p -> p.sound(SoundType.WOOD).strength(1.0F, 0.5F))
            .tags(BlockTags.PLANKS)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(itemBuilder -> itemBuilder.tab(AllTabs.tabNature));

    public static final ItemBlock nature_rubber_tree_leaves
            = new BlockBuilder()
            .name("nature_rubber_tree_leaves")
            .material(Material.LEAVES)
            .properties(p -> p.sound(SoundType.GRASS).strength(0.2F, 0.2F).noOcclusion().isViewBlocking(AllBlocks::never).isSuffocating(AllBlocks::never).isValidSpawn(AllBlocks::ocelotOrParrot).randomTicks())
            .renderLayer(() -> RenderType::cutout)
            .tags(BlockTags.LEAVES)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(RubberTreeLeaves::new)
            .buildItem(itemBuilder -> itemBuilder.tab(AllTabs.tabNature));

    public static final ItemBlock nature_rubber_tree_sapling
            = new BlockBuilder()
            .name("nature_rubber_tree_sapling")
            .material(Material.PLANT)
            .properties(p -> p.sound(SoundType.GRASS).strength(0.1F, 0.1F).noOcclusion().isViewBlocking(AllBlocks::never).isSuffocating(AllBlocks::never).isValidSpawn(AllBlocks::ocelotOrParrot).randomTicks())
            .renderLayer(() -> RenderType::cutout)
            .tags(BlockTags.SAPLINGS)
            .crossTextureModel()
            .simpleBlockState()
            .buildBlock(RubberTreeSapling::new)
            .buildItem(itemBuilder -> itemBuilder.tab(AllTabs.tabNature));

    public static final ItemBlock nature_mulberry_tree_log
            = new BlockBuilder()
            .name("nature_mulberry_tree_log")
            .material(Material.WOOD)
            .properties(p -> p.sound(SoundType.WOOD).strength(1.0F, 0.5F))
            .tags(BlockTags.LOGS)
            .autoFullCubeModel()
            .rotatablePillarBlockState()
            .buildBlock(MulberryTreeLog::new)
            .buildItem(itemBuilder -> itemBuilder.tab(AllTabs.tabNature));

    public static final ItemBlock nature_mulberry_tree_plank
            = new BlockBuilder()
            .name("nature_mulberry_tree_plank")
            .material(Material.WOOD)
            .properties(p -> p.sound(SoundType.WOOD).strength(1.0F, 0.5F))
            .tags(BlockTags.PLANKS)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(itemBuilder -> itemBuilder.tab(AllTabs.tabNature));

    public static final ItemBlock nature_mulberry_tree_leaves
            = new BlockBuilder()
            .name("nature_mulberry_tree_leaves")
            .material(Material.LEAVES)
            .properties(p -> p.sound(SoundType.GRASS).strength(0.2F, 0.2F).noOcclusion().isViewBlocking(AllBlocks::never).isSuffocating(AllBlocks::never).isValidSpawn(AllBlocks::ocelotOrParrot).randomTicks())
            .renderLayer(() -> RenderType::cutout)
            .tags(BlockTags.LEAVES)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock(MulberryTreeLeaves::new)
            .buildItem(itemBuilder -> itemBuilder.tab(AllTabs.tabNature));

    public static final ItemBlock nature_mulberry_tree_sapling
            = new BlockBuilder()
            .name("nature_mulberry_tree_sapling")
            .material(Material.PLANT)
            .properties(p -> p.sound(SoundType.GRASS).strength(0.1F, 0.1F).noCollission().isViewBlocking(AllBlocks::never).isSuffocating(AllBlocks::never).isValidSpawn(AllBlocks::ocelotOrParrot).randomTicks())
            .renderLayer(() -> RenderType::cutout)
            .tags(BlockTags.SAPLINGS)
            .crossTextureModel()
            .simpleBlockState()
            .buildBlock(MulberryTreeSapling::new)
            .buildItem(itemBuilder -> itemBuilder.tab(AllTabs.tabNature));

    //Bushes
    public static final ItemBlock nature_mulberry_bush
            = new BlockBuilder()
            .name("nature_mulberry_bush")
            .material(Material.PLANT)
            .tags(BlockTags.REPLACEABLE_PLANTS)
            .properties(p -> p.strength(0.1F, 0.1F).randomTicks().dynamicShape().sound(SoundType.GRASS))
            .renderLayer(() -> RenderType::cutout)
            .simplePresetModel()
            // blockState for stages
            .buildBlock(MulberryBush::new)
            .buildItem(itemBuilder -> itemBuilder.tab(AllTabs.tabNature));

    //Common Plants
    public static final ItemBlock nature_rush_top
            = new BlockBuilder()
            .name("nature_rush_top")
            .material(Material.PLANT)
            .properties(p -> p.sound(SoundType.GRASS).strength(0.5F, 0.1F).dynamicShape().noCollission())
            .simplePresetModel()
            .simpleBlockState()
            .buildBlock(RushBlockTop::new)
            .buildItem(ItemBuilder::noTab);

    public static final ItemBlock nature_rush_bottom
            = new BlockBuilder()
            .name("nature_rush_bottom")
            .material(Material.GRASS)
            .properties(p -> p.sound(SoundType.GRASS).strength(0.5F, 0.1F).dynamicShape().noCollission().randomTicks())
            .renderLayer(() -> RenderType::cutout)
            .presetCropModel(3)
            .cropBlockState(3)
            .buildBlock(RushBlockBottom::new)
            .customItemName("rush_seed")
            .buildItem(itemBuilder -> itemBuilder.tab(AllTabs.tabNature));

    /*
     * =============
     *  Stone Age
     * =============
     */


    /*
     * =============
     *  Steam Age
     * =============
     */

    public static final ItemBlock mechanic_boiler
            = new BlockBuilder()
            .name("mechanic_boiler")
            .simpleBlockState()
            .autoFullCubeModel()
            .buildBlock(BoilerBlock::new)
            .buildItem(builder -> builder.tab(AllTabs.tabSteam));


    /*
     * =============
     *  Misc
     * =============
     */

    //Plant Sign
    public static final ItemBlock nature_lactuca_raddeana
            = new BlockBuilder()
            .name("nature_lactuca_raddeana")
            .material(Material.PLANT)
            .properties(p -> p.sound(SoundType.GRASS).strength(0.1F, 0.1F).noCollission())
            .crossTextureModel()
            .simpleBlockState()
            .buildBlock(GrassBlock::new)
            .buildItem((itemBuilder -> itemBuilder.tab(AllTabs.tabNature)));

    //Ore Cap
    public static final ItemBlock rock_silicon_cap
            = new BlockBuilder()
            .name("rock_silicon_cap")
            .material(Material.STONE)
            .properties(p -> p.strength(0.5F, 0.5F))
            .tags(BlockTags.MINEABLE_WITH_PICKAXE)
            //.specialModel()
            .simpleBlockState()
            .buildBlock(OreCapBlock::new)
            .buildItem((itemBuilder -> itemBuilder.tab(AllTabs.tabOre)));

    //Igneous Rocks
    public static final ItemBlock rock_andesite
            = new BlockBuilder()
            .name("rock_andesite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_granite
            = new BlockBuilder()
            .name("rock_granite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_diorite
            = new BlockBuilder()
            .name("rock_diorite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder -> builder.tab(AllTabs.tabRock));

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
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones + "_nogen")
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

    //Test Stuff
    public static final ItemBlock test_rgb_grass
            = new BlockBuilder()
            .name("test_rgb_grass")
            .setRGBOverlay(Color.RED)
            .autoFullCubeModel(true)
            .simpleBlockState()
            .buildBlock()
            .buildItem(itemBuilder -> itemBuilder.noTab().setRGBOverlay(Color.RED));

    private static boolean always(BlockState state, BlockGetter getter, BlockPos pos) {
        return true;
    }

    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }

    private static Boolean ocelotOrParrot(BlockState p_50822_, BlockGetter p_50823_, BlockPos p_50824_, EntityType<?> p_50825_) {
        return p_50825_ == EntityType.OCELOT || p_50825_ == EntityType.PARROT;
    }

    private static ItemBlock asDecoration(String name) {
        return new BlockBuilder()
                .name(name)
                .autoFullCubeModel()
                .simpleBlockState()
                .buildBlock()
                .buildItem(builder -> builder.tab(AllTabs.tabBuilding));
    }

}

