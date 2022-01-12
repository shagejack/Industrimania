package shagejack.industrimania.registers;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.NonNullFunction;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.content.metallurgy.block.smeltery.clayFurnace.ClayFurnaceBottomTileEntity;

import java.util.*;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AllTileEntities {

    public static final RegistryObject<BlockEntityType<?>> clay_furnace_bottom
            = new TileEntityBuilder<ClayFurnaceBottomTileEntity>()
            .name("clay_furnace_bottom")
            .tileEntity(ClayFurnaceBottomTileEntity::new)
            .validBlocks(AllBlocks.mechanic_clay_furnace_bottom)
            .build();

    public static final RegistryObject<BlockEntityType<?>> iron_ore_slag
            = new TileEntityBuilder<ClayFurnaceBottomTileEntity>()
            .name("iron_ore_slag")
            .tileEntity(ClayFurnaceBottomTileEntity::new)
            .validBlocks(AllBlocks.mechanic_iron_ore_slag)
            .build();

    public static final RegistryObject<BlockEntityType<?>> bronze_tube
            = new TileEntityBuilder<ClayFurnaceBottomTileEntity>()
            .name("bronze_tube")
            .tileEntity(ClayFurnaceBottomTileEntity::new)
            .validBlocks(AllBlocks.mechanic_bronze_tube_block)
//            .renderer(() -> (context) -> new TestTer()) //workable test ,remove this after test
            .build();


    static class TileEntityBuilder<T extends BlockEntity> {

        private RegistryObject<BlockEntityType<?>> blockEntityType;
        private static final List<Binder<?>> tasks = new ArrayList<>();

        @FunctionalInterface
        public interface BlockEntityFactory<T extends BlockEntity> {
            public T create(BlockPos pos, BlockState state);
        }

        public TileEntityBuilder<T> tileEntity(BlockEntityFactory<T> factory) {
            this.factory = factory;
            return this;
        }

        private String name;
        private BlockEntityFactory<T> factory = null;
        private final Set<RegistryObject<Block>> validBlocks = new HashSet<>();

        public RegistryObject<BlockEntityType<?>> build() {
            BlockEntityFactory<T> factory = this.factory;
            blockEntityType = RegisterHandle.BLOCK_ENTITY_TYPE_REGISTER.register(name, ()
                    -> BlockEntityType.Builder.of((pos, state)
                            -> factory.create(pos, state),
                            validBlocks.stream().map(RegistryObject::get).toArray(Block[]::new))
                    .build(null));
            return blockEntityType;
        }

        public TileEntityBuilder<T> name(String name) {
            this.name = name;
            return this;
        }

        public TileEntityBuilder<T> renderer(NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> renderer) {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () ->
                    tasks.add(new Binder(blockEntityType, context -> renderer.get().apply(context))));
            return this;
        }

        public TileEntityBuilder<T> validBlocks(ItemBlock... block) {
            validBlocks.addAll(Arrays.stream(block).map(ItemBlock::block).toList());
            return this;
        }

        public TileEntityBuilder<T> validBlocks(RegistryObject<Block>... block) {
            validBlocks.addAll(Arrays.stream(block).toList());
            return this;
        }

        @SubscribeEvent
        public static void bind(final FMLClientSetupEvent event) {
            DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> tasks.forEach(Binder::register));
        }

        private record Binder<T extends BlockEntity>(RegistryObject<BlockEntityType<? extends T>> blockEntityType,
                                                     BlockEntityRendererProvider<T> render) {
            private void register() {
                DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () ->
                        BlockEntityRenderers.register(blockEntityType.get(), render)
                );
            }
        }

    }
}

//class TestTer implements BlockEntityRenderer<ClayFurnaceBottomTileEntity> { //workable test , remove this after use
//
//    @Override
//    public void render(ClayFurnaceBottomTileEntity blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
//
//    }
//}