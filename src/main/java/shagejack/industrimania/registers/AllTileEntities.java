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
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import shagejack.industrimania.content.metallurgyAge.block.smeltery.bronzeTube.BronzeTubeTileEntity;
import shagejack.industrimania.content.metallurgyAge.block.smeltery.clayFurnace.ClayFurnaceBottomTileEntity;
import shagejack.industrimania.content.primalAge.block.dryingRack.DryingRackRenderer;
import shagejack.industrimania.content.primalAge.block.dryingRack.DryingRackTileEntity;
import shagejack.industrimania.content.primalAge.block.nature.mulberry.silkworm.SilkwormRearingBoxTileEntity;
import shagejack.industrimania.content.primalAge.block.nature.rubberTree.RubberTreeLogTileEntity;
import shagejack.industrimania.content.primalAge.block.simpleCraftingTable.SimpleCraftingTableRenderer;
import shagejack.industrimania.content.primalAge.block.simpleCraftingTable.SimpleCraftingTableTileEntity;
import shagejack.industrimania.content.primalAge.block.stoneChoppingBoard.StoneChoppingBoardRenderer;
import shagejack.industrimania.content.primalAge.block.stoneChoppingBoard.StoneChoppingBoardTileEntity;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBaseTileEntity;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableRenderer;
import shagejack.industrimania.registers.block.AllBlocks;
import shagejack.industrimania.registers.record.ItemBlock;

import java.util.*;
import java.util.function.Supplier;

public class AllTileEntities {

    public static final RegistryObject<BlockEntityType<?>> silkworm_rearing_box
            = new TileEntityBuilder<SilkwormRearingBoxTileEntity>()
            .name("silkworm_rearing_box")
            .tileEntity(SilkwormRearingBoxTileEntity::new)
            .validBlocks(AllBlocks.mechanic_silkworm_rearing_box)
            .build();

    public static final RegistryObject<BlockEntityType<?>> rubber_tree_log
            = new TileEntityBuilder<RubberTreeLogTileEntity>()
            .name("rubber_tree_log")
            .tileEntity(RubberTreeLogTileEntity::new)
            .validBlocks(AllBlocks.nature_rubber_tree_log)
            .build();

    public static final RegistryObject<BlockEntityType<?>> simple_crafting_table
            = new TileEntityBuilder<SimpleCraftingTableTileEntity>()
            .name("simple_crafting_table")
            .tileEntity(SimpleCraftingTableTileEntity::new)
            .validBlocks(AllBlocks.mechanic_simple_crafting_table)
            .renderer(() -> SimpleCraftingTableRenderer::new)
            .build();

    public static final RegistryObject<BlockEntityType<?>> stone_chopping_board
            = new TileEntityBuilder<StoneChoppingBoardTileEntity>()
            .name("stone_chopping_board")
            .tileEntity(StoneChoppingBoardTileEntity::new)
            .validBlocks(AllBlocks.mechanic_stone_chopping_block)
            .renderer(() -> StoneChoppingBoardRenderer::new)
            .build();

    public static final RegistryObject<BlockEntityType<?>> drying_rack
            = new TileEntityBuilder<DryingRackTileEntity>()
            .name("drying_rack")
            .tileEntity(DryingRackTileEntity::new)
            .validBlocks(AllBlocks.mechanic_drying_rack)
            .renderer(() -> DryingRackRenderer::new)
            .build();

    public static final RegistryObject<BlockEntityType<?>> item_placeable
            = new TileEntityBuilder<ItemPlaceableBaseTileEntity>()
            .name("item_placeable")
            .tileEntity(ItemPlaceableBaseTileEntity::new)
            .validBlocks(AllBlocks.mechanic_item_placeable)
            .renderer(() -> ItemPlaceableRenderer::new)
            .build();

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
            = new TileEntityBuilder<BronzeTubeTileEntity>()
            .name("bronze_tube")
            .tileEntity(BronzeTubeTileEntity::new)
            .validBlocks(AllBlocks.mechanic_bronze_tube_block)
//            .renderer(() -> (context) -> new TestTer()) //workable test ,remove this after test
            .build();


    public static class TileEntityBuilder<T extends BlockEntity> {

        private RegistryObject<BlockEntityType<?>> blockEntityType;
        public static final List<Binder<?>> tasks = new ArrayList<>();

        @FunctionalInterface
        public interface BlockEntityFactory<T extends BlockEntity> {
            T create(BlockPos pos, BlockState state);
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
                    -> BlockEntityType.Builder.of(factory::create,
                            validBlocks.stream().map(RegistryObject::get).toArray(Block[]::new))
                    .build(null));
            return blockEntityType;
        }

        public TileEntityBuilder<T> name(String name) {
            this.name = name;
            return this;
        }

        public TileEntityBuilder<T> renderer(NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> renderer) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                    tasks.add(new Binder(() -> blockEntityType, context -> renderer.get().apply(context))));
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

        public static void bind(final FMLClientSetupEvent event) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> tasks.forEach(Binder::register));
        }

        private record Binder<T extends BlockEntity>(
                Supplier<RegistryObject<BlockEntityType<? extends T>>> blockEntityTypeSupplier,
                BlockEntityRendererProvider<T> render) {
            private void register() {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                        BlockEntityRenderers.register(blockEntityTypeSupplier.get().get(), render)
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