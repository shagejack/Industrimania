package shagejack.industrimania.registers;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.contraptions.base.BlockDirectionalBase;
import shagejack.industrimania.content.contraptions.ore.BlockOre;
import shagejack.industrimania.content.metallurgyAge.block.smeltery.clayFurnace.ClayFurnaceBottomBlock;
import shagejack.industrimania.content.metallurgyAge.block.smeltery.ironOreSlag.IronOreSlagBlock;
import shagejack.industrimania.content.worldGen.OreTypeRegistry;
import shagejack.industrimania.content.worldGen.RockRegistry;
import shagejack.industrimania.registers.AllItems.ItemBuilder;
import shagejack.industrimania.registers.dataGen.DataGenHandle;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static shagejack.industrimania.registers.dataGen.DataGenHandle.checkTextureFileExist;

public class AllBlocks {

    public static Map<RegistryObject<Block>, List<String>> BLOCK_TAGS = new HashMap<>();

    //TODO: ore block auto registry
    public static List<String> ROCKS = Lists.newArrayList(
            "rock_andesite",
            "rock_granite",
            "rock_diorite"
    );

    public static Map<String, Float> ROCKS_HARDNESS = new HashMap<>();
    public static Map<String, Float> ROCKS_EXPLOSION_RESISTANCE = new HashMap<>();

    static {
        ROCKS_HARDNESS.put("rock_andesite", 1.5F);
        ROCKS_HARDNESS.put("rock_granite", 1.5F);
        ROCKS_HARDNESS.put("rock_diorite", 1.5F);
        ROCKS_EXPLOSION_RESISTANCE.put("rock_andesite", 6.0F);
        ROCKS_EXPLOSION_RESISTANCE.put("rock_granite", 6.0F);
        ROCKS_EXPLOSION_RESISTANCE.put("rock_diorite", 6.0F);
    }

    public static Map<String, ItemBlock> ORES = new HashMap<>();

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
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_rhyolite
            = new BlockBuilder()
            .name("rock_rhyolite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_trachyte
            = new BlockBuilder()
            .name("rock_trachyte")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_basalt
            = new BlockBuilder()
            .name("rock_basalt")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_gabbro
            = new BlockBuilder()
            .name("rock_gabbro")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_porphyry
            = new BlockBuilder()
            .name("rock_porphyry")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.igneousStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    //Sedimentary Rocks
    public static final ItemBlock rock_sandstone
            = new BlockBuilder()
            .name("rock_sandstone")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones + "NoGen")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_chalk
            = new BlockBuilder()
            .name("rock_chalk")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_limestone
            = new BlockBuilder()
            .name("rock_limestone")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_shale
            = new BlockBuilder()
            .name("rock_shale")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_conglomeratee
            = new BlockBuilder()
            .name("rock_conglomerate")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_dolomite
            = new BlockBuilder()
            .name("rock_dolomite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_mudstone
            = new BlockBuilder()
            .name("rock_mudstone")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_coal
            = new BlockBuilder()
            .name("rock_coal")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_oilshale
            = new BlockBuilder()
            .name("rock_oilshale")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.sedimentaryStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    //Metamorphic rocks
    public static final ItemBlock rock_slate
            = new BlockBuilder()
            .name("rock_slate")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_quartzite
            = new BlockBuilder()
            .name("rock_quartzite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_greisen
            = new BlockBuilder()
            .name("rock_greisen")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_phyllite
            = new BlockBuilder()
            .name("rock_phyllite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_marble
            = new BlockBuilder()
            .name("rock_marble")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_gneiss
            = new BlockBuilder()
            .name("rock_gneiss")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

    public static final ItemBlock rock_granulite
            = new BlockBuilder()
            .name("rock_granulite")
            .asRock(1.5F, 6.0F, AllTags.IndustrimaniaTags.metamorphicStones)
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlock()
            .buildItem(builder->builder.tab(AllTabs.tabRock));

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

    //Register Ores

    public static void initOres() {
        OreTypeRegistry.oreTypeMap.forEach((oreTypeName, oreType) -> {
                    for (String rockName : ROCKS) {
                        for (int grade = 0; grade <= 2; grade++) {
                            String key = rockName + "_" + oreType.name() + "_" + grade;
                            ItemBlock oreBlock
                                    = new BlockBuilder()
                                    .name(key)
                                    .material(Material.STONE)
                                    .strength(ROCKS_HARDNESS.get(rockName), ROCKS_EXPLOSION_RESISTANCE.get(rockName))
                                    .asOre(oreType.name())
                                    .oreTextureModel()
                                    .simpleBlockState()
                                    .renderLayer(()-> RenderType::cutoutMipped)
                                    .buildBlock(BlockOre::new)
                                    .buildItem((ItemBuilder) -> ItemBuilder.tab(AllTabs.tabOre));

                            ORES.put(key, oreBlock);
                        }
                    }
                }
        );
    }


    static class BlockBuilder {

        private String name;
        private RegistryObject<Block> block;
        private Properties property;
        private List<String> tags = new ArrayList<>();
        private List<String> modTags = new ArrayList<>();
        private float hardness;
        private float explosionResistance;
        private Material material;
        private boolean requiresCorrectToolForDrops = false;
        private Map<String, Object> extraParam = new HashMap();

        static final List<Supplier<Runnable>> setupRenderLayerTasks= new ArrayList<>();

        private void checkProperty() {
            Objects.requireNonNull(name);
            if (property == null) {
                if (material == null) {
                    this.material = Material.STONE;
                }
                property = BlockBehaviour.Properties.of(material).strength(1.5f, 6.0f);
            }
            if (hardness != 0 && explosionResistance != 0) {
                property.strength(hardness, explosionResistance);
            }
            if (requiresCorrectToolForDrops) {
                property.requiresCorrectToolForDrops();
            }
        }

        public RegistryObject<Block> getBlock() {
            return block;
        }

        public BlockBuilder buildBlock(Supplier<Block> blockSupplier) {
            checkProperty();
            block = RegisterHandle.BLOCK_REGISTER.register(name, blockSupplier);
            Industrimania.LOGGER.debug("register Block:{}", name);
            if (!tags.isEmpty()) {
                BLOCK_TAGS.put(block, tags);
                Industrimania.LOGGER.debug("for block:{} add tags:{}", name, tags.toString());
            }
            return this;
        }

        public <T extends Block> BlockBuilder buildBlock(Function<Properties, T> factory) {
            return buildBlock(() -> factory.apply(property));
        }

        public <T extends Block> BlockBuilder buildBlock(BiFunction<Properties, Map<String, Object>, T> factory) {
            return buildBlock(() -> factory.apply(property, extraParam));
        }

        public BlockBuilder buildBlock() {
            return buildBlock(() -> new Block(property));
        }

        public BlockBuilder asRock(float hardness, float explosionResistance, String rockTag) {
            checkProperty();
            ROCKS.add(name);
            ROCKS_HARDNESS.put(name, hardness);
            ROCKS_EXPLOSION_RESISTANCE.put(name, explosionResistance);
            this.tags(AllTags.ToolType.pickaxe, AllTags.IndustrimaniaTags.rock, rockTag);
            return this;
        }

        public BlockBuilder asOre(String oreType) {
            checkProperty();
            this.tags(AllTags.ToolType.pickaxe, AllTags.IndustrimaniaTags.ore, AllTags.IndustrimaniaTags.oreTypeEntry + oreType);
            return this;
        }

        RegistryObject<Block> checkAlreadyBuild() {
            return Objects.requireNonNull(block, "can't build ItemBlock before block is built");
        }

        public ItemBlock buildItem(String itemName, Function<ItemBuilder, ItemBuilder> factory) {
            final var block = checkAlreadyBuild();
            final ItemBuilder itemBuilder = new ItemBuilder().name(itemName).blockModel("block/" + this.name);
            factory.apply(itemBuilder);
            Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, itemName);
            return new ItemBlock(itemBuilder.build(block), block);
        }

        public ItemBlock buildItem(Function<ItemBuilder, ItemBuilder> factory) {
            return buildItem(name, factory);
        }

        public ItemBlock buildItem() {
            return buildItem(name, (itemBuilder) -> itemBuilder);
        }


        public BlockBuilder renderLayer(Supplier<Supplier<RenderType>> renderType){
            setupRenderLayerTasks.add(()->()-> ItemBlockRenderTypes.setRenderLayer(block.get(), renderType.get().get()));
            return this;
        }

        public BlockBuilder tags(String... tags) {
            this.tags.clear();
            this.tags.addAll(Arrays.stream(tags).toList());
            return this;
        }

        public BlockBuilder material(Material material) {
            this.material = material;
            return this;
        }

        public BlockBuilder strength(float hardness, float explosionResistance) {
            this.hardness = hardness;
            this.explosionResistance = explosionResistance;
            return this;
        }

        public BlockBuilder requiresCorrectToolForDrops() {
            this.requiresCorrectToolForDrops = true;
            return this;
        }

        public BlockBuilder addExtraParam(String name, Object param) {
            this.extraParam.put(name, param);
            return this;
        }

        public BlockBuilder blockState(Consumer<BlockStateProvider> blockStateProviderConsumer){
            //Objects.requireNonNull(block);
            DataGenHandle.addBlockStateTask(blockStateProviderConsumer);
            return this;
        }

        public BlockBuilder complexBlockState(Consumer<BlockStateProvider> blockStateProviderConsumer){
            return blockState((provider)->{
                
            });
        }

        public BlockBuilder simpleBlockState() {
            blockState((provider) -> {
                var block = this.block;
                Industrimania.LOGGER.debug("set block state file for Block:{}", name);
                provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                        .forAllStates(state -> ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, "block/" + this.name))).build());
            });
            return this;
        }

        public BlockBuilder rotatableBlockState() {
            DataGenHandle.addBlockStateTask((provider) -> {
                var block = this.block;
                var modelFile = new ModelFile.UncheckedModelFile(new ResourceLocation(Industrimania.MOD_ID, "block/" + this.name));

                Industrimania.LOGGER.debug("set rotatable block state for Block:{}", name);

                provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                        .partialState()
                        .with(BlockDirectionalBase.FACING, Direction.NORTH)
                        .modelForState()
                        .modelFile(modelFile)
                        .rotationY(0)
                        .addModel();

                provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                        .partialState()
                        .with(BlockDirectionalBase.FACING, Direction.SOUTH)
                        .modelForState()
                        .modelFile(modelFile)
                        .rotationY(180)
                        .addModel();

                provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                        .partialState()
                        .with(BlockDirectionalBase.FACING, Direction.WEST)
                        .modelForState()
                        .modelFile(modelFile)
                        .rotationY(90)
                        .addModel();

                provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                        .partialState()
                        .with(BlockDirectionalBase.FACING, Direction.EAST)
                        .modelForState()
                        .modelFile(modelFile)
                        .rotationY(270)
                        .addModel();

                provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                        .partialState()
                        .with(BlockDirectionalBase.FACING, Direction.UP)
                        .modelForState()
                        .modelFile(modelFile)
                        .rotationX(270)
                        .addModel();

                provider.getVariantBuilder(Objects.requireNonNull(block.get()))
                        .partialState()
                        .with(BlockDirectionalBase.FACING, Direction.DOWN)
                        .modelForState()
                        .modelFile(modelFile)
                        .rotationX(90)
                        .addModel();
            });
            return this;
        }

        public BlockBuilder allSameModel(String path) {
            DataGenHandle.addBlockModelTask((provider) -> {
                var block = this.block;
                Industrimania.LOGGER.debug("set cube all model for Block:{}", name);
                provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                        .parent(DataGenHandle.blockCubeAll.get()).texture("all", path);
            });
            return this;
        }

        public BlockBuilder allSpecificModel(String up, String down, String north, String south, String west, String east) {
            DataGenHandle.addBlockModelTask(provider -> {
                var block = this.block;
                Industrimania.LOGGER.debug("""
                         set all specific model for Block:{} with
                          up:{}
                         down:{}
                         north:{}
                         south:{}
                         west:{}
                        east:{}""", name, up, down, north, south, west, east);
                provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                        .parent(DataGenHandle.blockCube.get())
                        .texture("up", up)
                        .texture("down", down)
                        .texture("north", north)
                        .texture("south", south)
                        .texture("west", west)
                        .texture("east", east);

            });
            return this;
        }

        //TODO: ore texture model (IDK how to implement this anyway)
        public BlockBuilder oreTextureModel() {
            DataGenHandle.addBlockModelTask(provider -> {
                try {
                    var temp = Objects.requireNonNull(this.name).split("_");
                    var rock = temp[1];
                    var oreType = temp[2];
                    var grade = temp[3];
                    final var rockLayerPath = "block/rock/" + rock + "/default";
                    final var oreLayerPath = "block/ore/layer/" + oreType + "/" + grade;


                    if (checkTextureFileExist(provider, rockLayerPath) && checkTextureFileExist(provider, oreLayerPath)) {
                        Industrimania.LOGGER.debug("automatically set ore texture model for Block:{}", name);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                                .parent(DataGenHandle.blockOre.get())
                                .texture("rock", rockLayerPath)
                                .texture("ore", oreLayerPath);
                    } else {
                        Industrimania.LOGGER.debug("failed to set ore texture model for Block:{}, 'cause its texture doesn't exist.", name);
                    }


                } catch (IllegalStateException e) {
                    Industrimania.LOGGER.error("failed to set ore texture model for Ore:{},reason:{}", name, e.getMessage());
                }
            });
            return this;
        }

        public BlockBuilder crossTextureModel() {
            DataGenHandle.addBlockModelTask(provider -> {
                try {
                    var category = Objects.requireNonNull(this.name).split("_")[0];
                    var bName = Objects.requireNonNull(this.name).substring(category.length() + 1);
                    final var path = "block/" + Objects.requireNonNull(category) + "/" + Objects.requireNonNull(bName) + "/cross";
                    if (checkTextureFileExist(provider, path)) {
                        Industrimania.LOGGER.debug("automatically set cross texture model for Block:{}", name);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                                .parent(DataGenHandle.blockCrossTexture.get()).texture("cross", path);
                    } else {
                        Industrimania.LOGGER.debug("failed to set cross texture model for Block:{}, 'cause its texture doesn't exist.", name);
                    }
                } catch (IllegalStateException e) {
                    Industrimania.LOGGER.error("failed to set cross texture model for Block:{},reason:{}", name, e.getMessage());
                }
            });
            return this;
        }

        /**
         * The folder is assets/stagecraft/models/block/$BLOCK_NAME$/ <p>
         * while file names (ended with .png) are <p>
         * default texture if others do not exist <p>
         * particle <p> THIS IS FOR BLOCK PARTICLE
         * up <p>
         * down <p>
         * left <p>
         * right <p>
         * front <p>
         * back <p>
         * And the existence of the listing files means special occasions <p>
         * x #the front and the back are the same <p>
         * y #the left and the right are the same <p>
         * z #the bottom and the top are the same <p>
         * xyz supports combine
         * files specific less will override those which specifies less except the default
         * <p>
         * Argument : north is the front
         *
         * @return BlockBuilder
         */
        public BlockBuilder autoFullCubeModel() {
            DataGenHandle.addBlockModelTask(provider -> {
                try {
                    var category = Objects.requireNonNull(this.name).split("_")[0];
                    var bName = Objects.requireNonNull(this.name).substring(category.length() + 1);
                    final var path = "block/" + Objects.requireNonNull(category) + "/" + Objects.requireNonNull(bName);
                    if (checkTextureFileExist(provider, path)) {
                        Industrimania.LOGGER.debug("automatically set cube all model for Block:{}", name);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                                .parent(DataGenHandle.blockCubeAll.get()).texture("all", path);
                        return;
                    }
                    final var defaultTexture = String.format("%s/%s", path, "default");
                    if (!checkTextureFileExist(provider, defaultTexture)) {
                        throw new IllegalStateException(String.format("can't find all same texture and default texture for Block:%s", name));
                    }
                    var front = defaultTexture;
                    var back = defaultTexture;
                    var left = defaultTexture;
                    var right = defaultTexture;
                    var up = defaultTexture;
                    var down = defaultTexture;
                    var particle = defaultTexture;

                    var xyTexture = String.format("%s/xy", path);
                    var xzTexture = String.format("%s/xy", path);
                    var yzTexture = String.format("%s/yz", path);
                    boolean xyExist = checkTextureFileExist(provider, xyTexture);
                    boolean xzExist = checkTextureFileExist(provider, xzTexture);
                    boolean yzExist = checkTextureFileExist(provider, yzTexture);
                    checkThrow(xyExist && yzExist && xzExist, "xy,xz,yz");
                    checkThrow(xyExist && xzExist, "xy,xz");
                    checkThrow(xyExist && yzExist, "xy,yz");
                    checkThrow(xzExist && yzExist, "xz,yz");
                    if (xyExist) {
                        front = xyTexture;
                        back = xyTexture;
                        left = xyTexture;
                        right = xyTexture;
                    }
                    if (xzExist) {
                        front = xzTexture;
                        back = xzTexture;
                        up = xzTexture;
                        down = xzTexture;
                    }
                    if (yzExist) {
                        left = yzTexture;
                        right = yzTexture;
                        up = yzTexture;
                        down = yzTexture;
                    }

                    var xTexture = String.format("%s/x", path);
                    var yTexture = String.format("%s/y", path);
                    var zTexture = String.format("%s/z", path);
                    boolean xExist = checkTextureFileExist(provider, xTexture);
                    boolean yExist = checkTextureFileExist(provider, yTexture);
                    boolean zExist = checkTextureFileExist(provider, zTexture);
                    checkThrow(xyExist && xExist, "xy,x");
                    checkThrow(xyExist && yExist, "xy,y");
                    checkThrow(xzExist && xExist, "xz,x");
                    checkThrow(xzExist && zExist, "xz,z");
                    checkThrow(yzExist && yExist, "yz,y");
                    checkThrow(yzExist && zExist, "yz,z");
                    if (xExist) {
                        front = xTexture;
                        back = xTexture;
                    }
                    if (yExist) {
                        left = yTexture;
                        right = yTexture;
                    }
                    if (zExist) {
                        up = zTexture;
                        down = zTexture;
                    }

                    var frontTexture = String.format("%s/front", path);
                    var backTexture = String.format("%s/back", path);
                    var leftTexture = String.format("%s/left", path);
                    var rightTexture = String.format("%s/right", path);
                    var upTexture = String.format("%s/up", path);
                    var downTexture = String.format("%s/down", path);
                    var frontExist = checkTextureFileExist(provider, frontTexture);
                    var backExist = checkTextureFileExist(provider, backTexture);
                    var leftExist = checkTextureFileExist(provider, leftTexture);
                    var rightExist = checkTextureFileExist(provider, rightTexture);
                    var upExist = checkTextureFileExist(provider, upTexture);
                    var downExist = checkTextureFileExist(provider, downTexture);
                    checkThrow(xExist && frontExist && backExist, "x,front,back");
                    checkThrow(xExist && frontExist, "x,front");
                    checkThrow(xExist && backExist, "x,back");
                    checkThrow(yExist && leftExist && rightExist, "y,left,right");
                    checkThrow(yExist && leftExist, "y,left");
                    checkThrow(yExist && rightExist, "y,right");
                    checkThrow(zExist && upExist && downExist, "z,up,down");
                    checkThrow(zExist && upExist, "z,up");
                    checkThrow(zExist && downExist, "z,down");

                    if (frontExist) {
                        front = frontTexture;
                    }
                    if (backExist) {
                        back = backTexture;
                    }
                    if (leftExist) {
                        left = leftTexture;
                    }
                    if (rightExist) {
                        right = rightTexture;
                    }
                    if (upExist) {
                        up = upTexture;
                    }
                    if (downExist) {
                        down = downTexture;
                    }

                    var particleTexture = String.format("%s/particle", path);
                    var particleExist = checkTextureFileExist(provider, particleTexture);
                    checkThrow(particleExist, "particle");

                    if (particleExist) {
                        particle = particleTexture;
                    }

                    Industrimania.LOGGER.debug("""
                            automatically set full cube block model for Block:{} with
                            particle:{}
                            up:{}
                            down:{}
                            front/north:{}
                            back/south:{}
                            left/west:{}
                            right/east:{}""", name, particle, up, down, front, back, left, right);
                    provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                            .parent(DataGenHandle.blockCube.get())
                            .texture("particle", particle)
                            .texture("up", up)
                            .texture("down", down)
                            .texture("north", front)
                            .texture("south", back)
                            .texture("west", left)
                            .texture("east", right);

                } catch (IllegalStateException e) {
                    Industrimania.LOGGER.error("failed to set autoFullCubeModel for Block: {},reason: {}", name, e.getMessage());
                }
            });
            return this;
        }

        public static void checkThrow(boolean condition, String message) {
            if (condition) {
                throw new IllegalStateException(String.format("%s specified at same at", message));
            }
        }

        public BlockBuilder name(String name) {
            this.name = name;
            return this;
        }

    }
}

