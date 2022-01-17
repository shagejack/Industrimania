package shagejack.industrimania.registers;

import com.google.common.collect.Lists;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.contraptions.base.BlockDirectionalBase;
import shagejack.industrimania.content.contraptions.ore.BlockOre;
import shagejack.industrimania.content.metallurgyAge.block.smeltery.clayFurnace.ClayFurnaceBottomBlock;
import shagejack.industrimania.content.metallurgyAge.block.smeltery.ironOreSlag.IronOreSlagBlock;
import shagejack.industrimania.content.worldGen.OreTypeRegistry;
import shagejack.industrimania.content.worldGen.record.OreType;
import shagejack.industrimania.registers.AllItems.ItemBuilder;
import shagejack.industrimania.registers.dataGen.DataGenHandle;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static shagejack.industrimania.registers.dataGen.DataGenHandle.checkTextureFileExist;

public class AllBlocks {

    public static Map<RegistryObject<Block>, List<String>> BLOCK_TAGS = new HashMap<>();

    //TODO: ore block auto registry
    public static List<String> ROCKS = Lists.newArrayList(
            "rock_andesite",
            "rock_granite",
            "rock_diorite",
            "rock_deepslate"
    );
    public static Map<String, Float> ROCKS_HARDNESS = new HashMap<>();
    public static Map<String, Float> ROCKS_EXPLOSION_RESISTANCE = new HashMap<>();

    static {
        ROCKS_HARDNESS.put("rock_andesite", 1.5F);
        ROCKS_HARDNESS.put("rock_granite", 1.5F);
        ROCKS_HARDNESS.put("rock_diorite", 1.5F);
        ROCKS_HARDNESS.put("rock_deepslate", 3.0F);
        ROCKS_EXPLOSION_RESISTANCE.put("rock_andesite", 6.0F);
        ROCKS_EXPLOSION_RESISTANCE.put("rock_granite", 6.0F);
        ROCKS_EXPLOSION_RESISTANCE.put("rock_diorite", 6.0F);
        ROCKS_EXPLOSION_RESISTANCE.put("rock_deepslate", 6.0F);
    }

    public static Map<String, ItemBlock> ORES = new HashMap<>();

    //Plant Sign
    public static final ItemBlock plant_lactuca_raddeana
            = new BlockBuilder()
            .name("plant_lactuca_raddeana")
            .material(Material.GRASS)
            .crossTextureModel()
            .simpleBlockState()
            .buildBlockWithItem(AllTabs.tabNature);

    //Ore Cap
    public static final ItemBlock rock_silicon_cap
            = new BlockBuilder()
            .name("rock_silicon_cap")
            .material(Material.STONE)
            .strength(0.5F, 0.5F)
            .tag("mineable/pickaxe")
            //.specialModel()
            .simpleBlockState()
            .buildBlockWithItem(AllTabs.tabOre);

    //Igneous Rocks
    public static final ItemBlock rock_dacite
            = new BlockBuilder()
            .name("rock_dacite")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_rhyolite
            = new BlockBuilder()
            .name("rock_rhyolite")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_trachyte
            = new BlockBuilder()
            .name("rock_trachyte")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_basalt
            = new BlockBuilder()
            .name("rock_basalt")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_gabbro
            = new BlockBuilder()
            .name("rock_gabbro")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_porphyry
            = new BlockBuilder()
            .name("rock_porphyry")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    //Sedimentary Rocks
    public static final ItemBlock rock_chalk
            = new BlockBuilder()
            .name("rock_chalk")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_limestone
            = new BlockBuilder()
            .name("rock_limestone")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_shale
            = new BlockBuilder()
            .name("rock_shale")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_conglomeratee
            = new BlockBuilder()
            .name("rock_conglomerate")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_dolomite
            = new BlockBuilder()
            .name("rock_dolomite")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_mudstone
            = new BlockBuilder()
            .name("rock_mudstone")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_coal
            = new BlockBuilder()
            .name("rock_coal")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_oilshale
            = new BlockBuilder()
            .name("rock_oilshale")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    //Metamorphic rocks
    public static final ItemBlock rock_quartzite
            = new BlockBuilder()
            .name("rock_quartzite")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_greisen
            = new BlockBuilder()
            .name("rock_greisen")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_phyllite
            = new BlockBuilder()
            .name("rock_phyllite")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_marble
            = new BlockBuilder()
            .name("rock_marble")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_gneiss
            = new BlockBuilder()
            .name("rock_gneiss")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    public static final ItemBlock rock_granulite
            = new BlockBuilder()
            .name("rock_granulite")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildRockWithItem();

    //Common Blocks

    public static final ItemBlock building_fine_clay
            = new BlockBuilder()
            .name("building_fine_clay")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlockWithItem();

    public static final ItemBlock building_scorched_clay
            = new BlockBuilder()
            .name("building_scorched_clay")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlockWithItem();

    public static final ItemBlock gravity_calcite
            = new BlockBuilder()
            .name("gravity_calcite")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlockWithItem(GravelBlock::new);

    public static final ItemBlock gravity_charcoal
            = new BlockBuilder()
            .name("gravity_charcoal")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlockWithItem(GravelBlock::new);

    public static final ItemBlock gravity_dust
            = new BlockBuilder()
            .name("gravity_dust")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlockWithItem(GravelBlock::new);

    public static final ItemBlock gravity_iron_oxide
            = new BlockBuilder()
            .name("gravity_iron_oxide")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlockWithItem(GravelBlock::new);

    public static final ItemBlock mechanic_clay_furnace_bottom
            = new BlockBuilder()
            .name("mechanic_clay_furnace_bottom")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlockWithItem(ClayFurnaceBottomBlock::new);

    public static final ItemBlock mechanic_iron_ore_slag
            = new BlockBuilder()
            .name("mechanic_iron_ore_slag")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlockWithItem(IronOreSlagBlock::new);

    public static final ItemBlock mechanic_bronze_tube_block
            = new BlockBuilder()
            .name("mechanic_bronze_tube")
            .autoFullCubeModel()
            .rotatableBlockState()
            .buildBlockWithItem(BlockDirectionalBase::new);

    //Register Ores

    public static void initOres() {
        OreTypeRegistry.oreTypeMap.forEach( (oreTypeName, oreType) -> {
                    for (String rockName : ROCKS) {
                        for (int grade = 0; grade <= 2; grade++) {
                            String key = rockName + "_" + oreType.name() + "_" + grade;
                            ItemBlock oreBlock
                                    = new BlockBuilder()
                                    .name(key)
                                    .material(Material.STONE)
                                    .strength(ROCKS_HARDNESS.get(rockName), ROCKS_EXPLOSION_RESISTANCE.get(rockName))
                                    .tag("mineable/pickaxe")
                                    .oreTextureModel()
                                    .simpleBlockState()
                                    .buildBlockWithItem(BlockOre::new, AllTabs.tabOre);

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

        private Map<String, Object> extraParam = new HashMap();

        public <T extends Block> RegistryObject<Block> buildBlock(Function<Properties, T> factory) {
            Objects.requireNonNull(name);
            if (property == null) property = BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F);

            block = RegisterHandle.BLOCK_REGISTER.register(name, () -> factory.apply(property));
            Industrimania.LOGGER.debug("register Block:{}", name);
            if (!tags.isEmpty()) BLOCK_TAGS.put(block, tags);
            return block;
        }

        public <T extends Block> RegistryObject<Block> buildBlock(BiFunction<Properties, Map<String, Object>, T> factory) {
            Objects.requireNonNull(name);
            if (property == null) property = BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F);

            block = RegisterHandle.BLOCK_REGISTER.register(name, () -> factory.apply(property, extraParam));
            Industrimania.LOGGER.debug("register Block:{}", name);
            if (!tags.isEmpty()) BLOCK_TAGS.put(block, tags);
            return block;
        }

        public <T extends Block> RegistryObject<Block> buildBlock() {
            Objects.requireNonNull(name);
            if (property == null) property = BlockBehaviour.Properties.of(Material.STONE).strength(1.5F, 6.0F);

            block = RegisterHandle.BLOCK_REGISTER.register(name, () -> new Block(property));
            Industrimania.LOGGER.debug("register Block:{}", name);
            if (!tags.isEmpty()) BLOCK_TAGS.put(block, tags);
            return block;
        }

        public <T extends Block> ItemBlock buildBlockWithItem(Function<Properties, T> factory) {
            var block = buildBlock(factory);
            final ItemBuilder itemModelBuilder
                    = new ItemBuilder()
                    .name(this.name)
                    .blockModel("block/" + this.name);
            Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, name);
            return new ItemBlock(itemModelBuilder.build(block), block);
        }

        public <T extends Block> ItemBlock buildBlockWithItem(Function<Properties, T> factory, CreativeModeTab tab) {
            var block = buildBlock(factory);
            final ItemBuilder itemModelBuilder
                    = new ItemBuilder()
                    .name(this.name)
                    .blockModel("block/" + this.name);
            Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, name);
            return new ItemBlock(itemModelBuilder.tab(tab).build(block), block);
        }

        public <T extends Block> ItemBlock buildBlockWithItem(BiFunction<Properties, Map<String, Object>, T> factory) {
            var block = buildBlock(factory);
            final ItemBuilder itemModelBuilder
                    = new ItemBuilder()
                    .name(this.name)
                    .blockModel("block/" + this.name);
            Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, name);
            return new ItemBlock(itemModelBuilder.build(block), block);
        }

        public <T extends Block> ItemBlock buildBlockWithItem(BiFunction<Properties, Map<String, Object>, T> factory, CreativeModeTab tab) {
            var block = buildBlock(factory);
            final ItemBuilder itemModelBuilder
                    = new ItemBuilder()
                    .name(this.name)
                    .blockModel("block/" + this.name);
            Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, name);
            return new ItemBlock(itemModelBuilder.tab(tab).build(block), block);
        }

        public <T extends Block> ItemBlock buildBlockWithItem() {
            var block = buildBlock();
            final ItemBuilder itemModelBuilder
                    = new ItemBuilder()
                    .name(this.name)
                    .blockModel("block/" + this.name);
            Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, name);
            return new ItemBlock(itemModelBuilder.build(block), block);
        }

        public <T extends Block> ItemBlock buildBlockWithItem(CreativeModeTab tab) {
            var block = buildBlock();
            final ItemBuilder itemModelBuilder
                    = new ItemBuilder()
                    .name(this.name)
                    .blockModel("block/" + this.name);
            Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, name);
            return new ItemBlock(itemModelBuilder.tab(tab).build(block), block);
        }

        public <T extends Block> ItemBlock buildRockWithItem(float hardness, float explosionResistance) {
            this.property = Properties.of(Material.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(hardness, explosionResistance);
            tags.add("mineable/pickaxe");
            var block = buildBlock();
            final ItemBuilder itemModelBuilder
                    = new ItemBuilder()
                    .name(this.name)
                    .blockModel("block/" + this.name);
            ROCKS.add(this.name);
            ROCKS_HARDNESS.put(this.name, hardness);
            ROCKS_EXPLOSION_RESISTANCE.put(this.name, explosionResistance);
            Industrimania.LOGGER.debug("register Rock:{} with Item:{}", name, name);
            return new ItemBlock(itemModelBuilder.tab(AllTabs.tabRock).build(block), block);
        }

        public <T extends Block> ItemBlock buildRockWithItem() {
            return this.buildRockWithItem(1.5F, 6.0F);
        }

        public <T extends Block> ItemBlock buildBlockWithItem(Consumer<ItemBuilder> consumer, @Nullable Function<Properties, T> factory) {
            var block = buildBlock(factory);
            final ItemBuilder itemBuilder
                    = new ItemBuilder()
                    .name(this.name)
                    .blockModel("block/" + this.name);
            consumer.accept(itemBuilder);
            Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, itemBuilder);
            return new ItemBlock(itemBuilder.build(block), block);
        }

        public BlockBuilder tag(String tag) {
            this.tags.add(tag);
            return this;
        }

        public BlockBuilder material(Material material) {
            this.property = BlockBehaviour.Properties.of(material);
            return this;
        }

        public BlockBuilder strength(float hardness, float explosionResistence) {
            this.property.strength(hardness, explosionResistence);
            return this;
        }

        public BlockBuilder requiresCorrectToolForDrops() {
            this.property.requiresCorrectToolForDrops();
            return this;
        }

        public BlockBuilder addExtraParam(String name, Object param) {
            this.extraParam.put(name, param);
            return this;
        }

        public BlockBuilder simpleBlockState() {
            DataGenHandle.addBlockStateTask((provider) -> {
                var block = this.block;
                Industrimania.LOGGER.debug("set block state for Block:{}", name);
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

                } catch (IllegalStateException e){
                    Industrimania.LOGGER.error("failed to set cross texture model for Ore:{},reason:{}",name,e.getMessage());
                }
            });
            return this;
        }

        public BlockBuilder crossTextureModel() {
            DataGenHandle.addBlockModelTask(provider -> {
                try {
                    var category = Objects.requireNonNull(this.name).split("_")[0];
                    var bName = Objects.requireNonNull(this.name).substring(category.length() + 1);
                    final var path = "block/"+ Objects.requireNonNull(category) + "/" + Objects.requireNonNull(bName) + "/cross";
                    if (checkTextureFileExist(provider, path)) {
                        Industrimania.LOGGER.debug("automatically set cross texture model for Block:{}", name);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                                .parent(DataGenHandle.blockCrossTexture.get()).texture("cross", path);
                    } else {
                        Industrimania.LOGGER.debug("failed to set cross texture model for Block:{}, 'cause its texture doesn't exist.", name);
                    }
                } catch (IllegalStateException e){
                    Industrimania.LOGGER.error("failed to set cross texture model for Block:{},reason:{}",name,e.getMessage());
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
                    final var path = "block/"+ Objects.requireNonNull(category) + "/" + Objects.requireNonNull(bName);
                    if (checkTextureFileExist(provider, path)) {
                        Industrimania.LOGGER.debug("automatically set cube all model for Block:{}", name);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                                .parent(DataGenHandle.blockCubeAll.get()).texture("all", path);
                        return;
                    }
                    final var defaultTexture = String.format("%s/%s", path, "default");
                    if (!checkTextureFileExist(provider, defaultTexture)) {
                        throw new IllegalStateException(String.format("can't find all same texture and default texture for Block:%s",name));
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

                }catch (IllegalStateException e){
                    Industrimania.LOGGER.error("failed to set autoFullCubeModel for Block:{},reason:{}",name,e.getMessage());
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

