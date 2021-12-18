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

public class AllBlock {

//    public static final ItemBlock testBlock
//            = new BlockBuilder().buildBlockWithItem();

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

        public BlockBuilder allSpecificModel(String down, String up, String north, String south, String east, String west) {
            DataGenHandle.addBlockModelTask(provider -> {
                var block = this.block;
                ShageCraft.LOGGER.debug("""
                         set all specific model for Block:{} with
                         down:{}
                         up:{}
                         north:{}
                         south:{}
                        east:{}""", name, down, up, north, south, east);
                provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                        .parent(DataGenHandle.blockCube.get())
                        .texture("down", down)
                        .texture("up", up)
                        .texture("north", north)
                        .texture("south", south)
                        .texture("east", east)
                        .texture("west", west);
            });
            return this;
        }

        public BlockBuilder name(String name) {
            this.name = name;
            return this;
        }
    }
}

