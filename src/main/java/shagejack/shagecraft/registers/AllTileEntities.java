package shagejack.shagecraft.registers;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.registries.RegistryObject;
import shagejack.shagecraft.content.metallurgy.block.smeltery.clayFurnace.ClayFurnaceBottomTileEntity;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class AllTileEntities {

    public static final RegistryObject<BlockEntityType<?>> clay_furnace_bottom
            = new TileEntityBuilder<ClayFurnaceBottomTileEntity>()
            .name("clay_furnace_bottom")
            .tileEntity(ClayFurnaceBottomTileEntity::new)
            .validBlock(AllBlocks.mechanic_clay_furnace_bottom.block().get())
            .build();

    public static final RegistryObject<BlockEntityType<?>> iron_ore_slag
            = new TileEntityBuilder<ClayFurnaceBottomTileEntity>()
            .name("iron_ore_slag")
            .tileEntity(ClayFurnaceBottomTileEntity::new)
            .validBlock(AllBlocks.mechanic_iron_ore_slag.block().get())
            .build();


    static class TileEntityBuilder<T extends BlockEntity> {

        RegistryObject<BlockEntityType<?>> registryObject;

        public interface BlockEntityFactory<T extends BlockEntity> {

            public T create(BlockPos pos, BlockState state);

        }

        public TileEntityBuilder<T> tileEntity(BlockEntityFactory<T> factory) {
            this.factory = factory;
            return this;
        }

        private String name;
        private BlockEntityFactory<T> factory = null;
        private final Set<Block> validBlocks = new HashSet<>();

        @Nullable
        private NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> renderer;

        public RegistryObject<BlockEntityType<?>> build() {
            BlockEntityFactory<T> factory = this.factory;
            registryObject = RegisterHandle.BLOCK_ENTITY_TYPE_REGISTER.register(name, () -> BlockEntityType.Builder.of((pos, state) -> factory.create(pos, state), validBlocks.toArray(Block[]::new))
                    .build(null));
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
