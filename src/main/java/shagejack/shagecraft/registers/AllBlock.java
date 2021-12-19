package shagejack.shagecraft.registers;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.RegistryObject;
import shagejack.shagecraft.ShageCraft;
import shagejack.shagecraft.registers.AllItem.ItemBuilder;
import shagejack.shagecraft.registers.dataGen.DataGenHandle;

import java.util.Objects;
import java.util.function.Consumer;

import static shagejack.shagecraft.registers.dataGen.DataGenHandle.checkTextureFileExist;
import static shagejack.shagecraft.registers.dataGen.DataGenHandle.checkTextureFileExist;

public class AllBlock {

    public static final ItemBlock building_fine_clay
            = new BlockBuilder()
            .name("building_fine_clay")
            .autoFullCubeModel()
            .buildBlockWithItem();

    static class BlockBuilder {
        private String name;
        private RegistryObject<Block> block;
        private Properties property;

        public RegistryObject<Block> buildBlock() {
            Objects.requireNonNull(name);
            if (property == null) property = BlockBehaviour.Properties.of(Material.STONE);
            block = RegisterHandle.BLOCK_REGISTER.register(name, () -> new Block(property));
            ShageCraft.LOGGER.debug("register Block:{}", name);
            return block;
        }

        public ItemBlock buildBlockWithItem() {
            var block = buildBlock();
            final ItemBuilder itemModelBuilder = new ItemBuilder().name(this.name);
            ShageCraft.LOGGER.debug("register Block:{} with Item:{}", name, name);
            return new ItemBlock(itemModelBuilder.build(block), block);
        }

        public ItemBlock buildBlockWithItem(Consumer<ItemBuilder> consumer) {
            var block = buildBlock();
            final ItemBuilder itemBuilder = new ItemBuilder();
            consumer.accept(itemBuilder);
            ShageCraft.LOGGER.debug("register Block:{} with Item:{}", name, itemBuilder);
            return new ItemBlock(itemBuilder.build(block), block);
        }

        public BlockBuilder allSameModel(String path) {
            DataGenHandle.addBlockModelTask((provider) -> {
                var block = this.block;
                ShageCraft.LOGGER.debug("set cube all model for Block:{}", name);
                provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                        .parent(DataGenHandle.blockCubeAll.get()).texture("all", path);
            });
            return this;
        }

        public BlockBuilder allSpecificModel(String up, String down, String north, String south, String west, String east) {
            DataGenHandle.addBlockModelTask(provider -> {
                var block = this.block;
                ShageCraft.LOGGER.debug("""
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
         * uo <p>
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
                        ShageCraft.LOGGER.debug("automatically set cube all model for Block:{}", name);
                        provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                                .parent(DataGenHandle.blockCubeAll.get()).texture("all", allSame);
                        return;
                    }
                    final var defaultTexture = String.format("%s/%s", name, name);
                    if (!checkTextureFileExist(provider, defaultTexture)) {
                        throw new IllegalStateException(String.format("can't find all same texture and default texture for Block:%s",name));
                    }
                    var front = defaultTexture;
                    var back = defaultTexture;
                    var left = defaultTexture;
                    var right = defaultTexture;
                    var up = defaultTexture;
                    var down = defaultTexture;

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

                    ShageCraft.LOGGER.debug("""
                        automatically set full cube block model for Block:{} with
                        up:{}
                        down:{}
                        front/north:{}
                        back/south:{}
                        left/west:{}
                        right/east:{}""", name, up, down, front, back, left, right);
                    provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                            .parent(DataGenHandle.blockCube.get())
                            .texture("up", up)
                            .texture("down", down)
                            .texture("north", front)
                            .texture("south", back)
                            .texture("west", left)
                            .texture("east", right);

                }catch (IllegalStateException e){
                    ShageCraft.LOGGER.error(e.getMessage());
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

