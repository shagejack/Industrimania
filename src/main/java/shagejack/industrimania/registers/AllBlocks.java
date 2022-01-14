package shagejack.industrimania.registers;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.Industrimania;
import shagejack.industrimania.content.contraptions.base.BlockDirectionalBase;
import shagejack.industrimania.content.metallurgyAge.block.smeltery.clayFurnace.ClayFurnaceBottomBlock;
import shagejack.industrimania.content.metallurgyAge.block.smeltery.ironOreSlag.IronOreSlagBlock;
import shagejack.industrimania.registers.AllItems.ItemBuilder;
import shagejack.industrimania.registers.dataGen.DataGenHandle;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

import static shagejack.industrimania.registers.dataGen.DataGenHandle.checkTextureFileExist;

public class AllBlocks {

    public static final ItemBlock rock_marble
            = new BlockBuilder()
            .name("rock_marble")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlockWithItem();

    public static final ItemBlock rock_gneiss
            = new BlockBuilder()
            .name("rock_gneiss")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlockWithItem();

    public static final ItemBlock rock_limestone
            = new BlockBuilder()
            .name("rock_limestone")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlockWithItem();

    public static final ItemBlock rock_shale
            = new BlockBuilder()
            .name("rock_shale")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlockWithItem();

    public static final ItemBlock rock_conglomeratee
            = new BlockBuilder()
            .name("rock_conglomerate")
            .autoFullCubeModel()
            .simpleBlockState()
            .buildBlockWithItem();


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


    static class BlockBuilder {

        private String name;
        private RegistryObject<Block> block;
        private Properties property;

        public <T extends Block> RegistryObject<Block> buildBlock(Function<Properties, T> factory) {
            Objects.requireNonNull(name);
            if (property == null) property = BlockBehaviour.Properties.of(Material.STONE);

            block = RegisterHandle.BLOCK_REGISTER.register(name, () -> factory.apply(property));
            Industrimania.LOGGER.debug("register Block:{}", name);
            return block;
        }

        public <T extends Block> RegistryObject<Block> buildBlock() {
            Objects.requireNonNull(name);
            if (property == null) property = BlockBehaviour.Properties.of(Material.STONE);

            block = RegisterHandle.BLOCK_REGISTER.register(name, () -> new Block(property));
            Industrimania.LOGGER.debug("register Block:{}", name);
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

        public <T extends Block> ItemBlock buildBlockWithItem() {
            var block = buildBlock();
            final ItemBuilder itemModelBuilder
                    = new ItemBuilder()
                    .name(this.name)
                    .blockModel("block/" + this.name);
            Industrimania.LOGGER.debug("register Block:{} with Item:{}", name, name);
            return new ItemBlock(itemModelBuilder.build(block), block);
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
                    final var allSame = "block/"+Objects.requireNonNull(this.name);
                    if (checkTextureFileExist(provider, allSame)) {
                        Industrimania.LOGGER.debug("automatically set cube all model for Block:{}", name);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                                .parent(DataGenHandle.blockCubeAll.get()).texture("all", allSame);
                        return;
                    }
                    final var defaultTexture = String.format("block/%s/%s", name, "default");
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

                    var xyTexture = String.format("block/%s/xy", name);
                    var xzTexture = String.format("block/%s/xy", name);
                    var yzTexture = String.format("block/%s/yz", name);
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

                    var xTexture = String.format("block/%s/x", name);
                    var yTexture = String.format("block/%s/y", name);
                    var zTexture = String.format("block/%s/z", name);
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

                    var frontTexture = String.format("block/%s/front", name);
                    var backTexture = String.format("block/%s/back", name);
                    var leftTexture = String.format("block/%s/left", name);
                    var rightTexture = String.format("block/%s/right", name);
                    var upTexture = String.format("block/%s/up", name);
                    var downTexture = String.format("block/%s/down", name);
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

                    var particleTexture = String.format("block/%s/particle", name);
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

