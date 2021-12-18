package shagejack.shagecraft.registers;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import shagejack.shagecraft.ShageCraft;
import shagejack.shagecraft.registers.AllItem.ItemBuilder;
import shagejack.shagecraft.registers.dataGen.DataGenHandle;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.function.Consumer;

public class AllBlock {

    static final ExistingFileHelper.ResourceType TEXTURE = new ExistingFileHelper.ResourceType(PackType.CLIENT_RESOURCES, ".png", "textures");

    public static final ItemBlock building_fine_clay
            = new BlockBuilder()
            .name("building.fine_clay")
            .specificModel(null)
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

        /**
         * Usually param just needs to be null, this function will automatically check if file exists in hard-coded locations. <p>
         * The folder is .../assets/shagecraft/models/block/$BLOCK_NAME$/ <p>
         * while file names (ended with .png) are <p>
         * side #default texture if others do not exist <p>
         * down <p>
         * up <p>
         * north <p>
         * south <p>
         * east <p>
         * west <p>
         * And the existence of the listing files means special occasions <p>
         * x #the front and the back are the same <p>
         * y #the left and the right are the same <p>
         * z #the bottom and the top are the same <p>
         * files detected later will override the earlier ones
         * @param modifier It's a String Array: {down, up, north, south, east, west}.
         * @return BlockBuilder
         */

        public BlockBuilder specificModel(@Nullable String[] modifier) {
            DataGenHandle.addBlockModelTask(provider -> {
                var block = this.block;

                String[] textures = new String[]{
                        "side", "side", "side", "side", "side", "side"
                };

                textures[0] = isValidRL(provider, makeRL(name, "down")) ? "down" : textures[0];
                textures[1] = isValidRL(provider, makeRL(name, "up")) ? "up" : textures[1];
                textures[2] = isValidRL(provider, makeRL(name, "north")) ? "north" : textures[2];
                textures[3] = isValidRL(provider, makeRL(name, "south")) ? "south" : textures[3];
                textures[4] = isValidRL(provider, makeRL(name, "east")) ? "east" : textures[4];
                textures[5] = isValidRL(provider, makeRL(name, "west")) ? "west" : textures[5];

                // the front and the back are the same
                if (isValidRL(provider,  makeRL(name,"x"))) {
                    textures[2] = "x";
                    textures[3] = "x";
                }

                //the left and the right are the same
                if (isValidRL(provider,  makeRL(name,"y"))) {
                    textures[4] = "y";
                    textures[5] = "y";
                }

                //the bottom and the top are the same
                if (isValidRL(provider,  makeRL(name,"z"))) {
                    textures[0] = "z";
                    textures[1] = "z";
                }

                if (modifier != null) {
                    for (int i = 0; i < modifier.length; i++) {
                        textures[i] = modifier[i];
                    }
                }

                /*
                ShageCraft.LOGGER.debug("""
                         set all specific model for Block:{} with
                         down:{}
                         up:{}
                         north:{}
                         south:{}
                        east:{}""", name); */



                provider.getBuilder(Objects.requireNonNull(block.get().getRegistryName()).getPath())
                        .parent(DataGenHandle.blockCube.get())
                        .texture("down", makeRL(name,textures[0]))
                        .texture("up", makeRL(name,textures[1]))
                        .texture("north", makeRL(name,textures[2]))
                        .texture("south", makeRL(name,textures[3]))
                        .texture("east", makeRL(name,textures[4]))
                        .texture("west", makeRL(name,textures[5]));
            });
            return this;
        }

        public boolean isValidRL(BlockModelProvider provider, String location) {
            return provider.existingFileHelper.exists(new ResourceLocation(ShageCraft.MOD_ID, location), TEXTURE);
        }

        public String makeRL(String name, String location) {
            return "block/" + name + "/" + location;
        }

        public BlockBuilder name(String name) {
            this.name = name;
            return this;
        }

    }
}

