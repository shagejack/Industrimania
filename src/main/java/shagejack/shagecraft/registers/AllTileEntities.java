package shagejack.shagecraft.registers;

import com.mojang.datafixers.types.Type;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import shagejack.shagecraft.ShageCraft;
import shagejack.shagecraft.content.metallurgy.block.smelter.clayFurnace.ClayFurnaceBottomTileEntity;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class AllTileEntities {

    public static final RegistryObject<BlockEntityType<?>> clay_furnace_bottom = new TileEntityBuilder<ClayFurnaceBottomTileEntity>()
            .name("clay_furnace_bottom")
            .validBlock(AllBlocks.building_fine_clay.block().get())
            .build(ClayFurnaceBottomTileEntity::new);





    static class TileEntityBuilder<T extends BlockEntity> {

        private String name;
        private final Set<Block> validBlocks = new HashSet<>();

        RegistryObject<BlockEntityType<?>> registryObject;

        @Nullable
        private NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> renderer;


        public RegistryObject<BlockEntityType<?>> build(BlockEntityType.BlockEntitySupplier<T> te) {
            registryObject = RegisterHandle.BLOCK_ENTITY_TYPE_REGISTER.register(name, () -> BlockEntityType.Builder.of(te, Blocks.JUKEBOX).build(null));
            return registryObject;
        }

        public TileEntityBuilder<T> name(String name) {
            this.name = name;
            return this;
        }

        public TileEntityBuilder<T> renderer(NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> renderer) {
            if (this.renderer == null) { // First call only
                //TODO: TileEntity Renderer
                //DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::registerRenderer);
            }
            this.renderer = renderer;
            return this;
        }

        public TileEntityBuilder<T> validBlock(Block block) {
            validBlocks.add(block);
            return this;
        }


    }
}
