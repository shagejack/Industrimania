package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.foundation.item.ItemHelper;
import shagejack.industrimania.registers.AllTileEntities;

import java.util.stream.IntStream;

public class ItemPlaceableBaseBlock extends Block implements ITE<ItemPlaceableBaseTileEntity> {

    public static final IntegerProperty AMOUNT = IntegerProperty.create("amount", 0, 16);

    private static final VoxelShape[] itemShapes = new VoxelShape[]{
            Block.box(0.0D, 0.0D, 0.0D, 4.0D, 4.0D, 16.0D),
            Block.box(4.0D, 0.0D, 0.0D, 8.0D, 4.0D, 16.0D),
            Block.box(8.0D, 0.0D, 0.0D, 12.0D, 4.0D, 16.0D),
            Block.box(12.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            Block.box(0.0D, 4.0D, 0.0D, 4.0D, 8.0D, 16.0D),
            Block.box(4.0D, 4.0D, 0.0D, 8.0D, 8.0D, 16.0D),
            Block.box(8.0D, 4.0D, 0.0D, 12.0D, 8.0D, 16.0D),
            Block.box(12.0D, 4.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            Block.box(0.0D, 8.0D, 0.0D, 4.0D, 12.0D, 16.0D),
            Block.box(4.0D, 8.0D, 0.0D, 8.0D, 12.0D, 16.0D),
            Block.box(8.0D, 8.0D, 0.0D, 12.0D, 12.0D, 16.0D),
            Block.box(12.0D, 8.0D, 0.0D, 16.0D, 12.0D, 16.0D),
            Block.box(0.0D, 12.0D, 0.0D, 4.0D, 16.0D, 16.0D),
            Block.box(4.0D, 12.0D, 0.0D, 8.0D, 16.0D, 16.0D),
            Block.box(8.0D, 12.0D, 0.0D, 12.0D, 16.0D, 16.0D),
            Block.box(12.0D, 12.0D, 0.0D, 16.0D, 16.0D, 16.0D),
    };

    //TODO: change shape according to the content.
    private static final VoxelShape[] SHAPES = makeShapes(itemShapes);

    private static VoxelShape[] makeShapes(VoxelShape[] shapes) {
        return IntStream.range(0, 17).mapToObj((index) -> makeItemPlaceableShape(index, shapes)).toArray(VoxelShape[]::new);
    }

    private static VoxelShape makeItemPlaceableShape(int index, VoxelShape[] shapes) {
        VoxelShape voxelShape = Block.box(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

        for (int i = 0; i < index; i++) {
            voxelShape = Shapes.or(voxelShape, shapes[i]);
        }

        return voxelShape;
    }

    public ItemPlaceableBaseBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AMOUNT, Integer.valueOf(0)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AMOUNT);
    }

    public RenderShape getRenderShape(BlockState p_52986_) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        return SHAPES[state.getValue(AMOUNT)];
    }

    public boolean useShapeForLightOcclusion(BlockState p_52997_) {
        return true;
    }

    @Override
    public Class<ItemPlaceableBaseTileEntity> getTileEntityClass() {
        return ItemPlaceableBaseTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends ItemPlaceableBaseTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends ItemPlaceableBaseTileEntity>) AllTileEntities.item_placeable.get();
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean p_48717_) {
        if (oldState.hasBlockEntity() && oldState.getBlock() != newState.getBlock()) {
            withTileEntityDo(level, pos, te -> ItemHelper.dropContents(level, pos, te.inventory));
            level.removeBlockEntity(pos);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.isEmpty()) {
                BlockEntity te = level.getBlockEntity(blockPos);
                if (te instanceof ItemPlaceableBaseTileEntity) {
                    ItemStack removeStack = ((ItemPlaceableBaseTileEntity) te).removeItem();
                    if (!removeStack.isEmpty()) {
                        player.addItem(removeStack);
                    }
                }
            } else if (stack.getItem() instanceof ItemPlaceableBase) {
                BlockEntity te = level.getBlockEntity(blockPos);
                if (te instanceof ItemPlaceableBaseTileEntity) {
                    if (((ItemPlaceableBaseTileEntity) te).addItem(stack)) {
                        if (!player.isCreative()) {
                            stack.shrink(1);
                        }
                        return InteractionResult.sidedSuccess(level.isClientSide());
                    }
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            BlockEntity te = level.getBlockEntity(pos);
            if (te instanceof ItemPlaceableBaseTileEntity) {
                ItemStack removeStack = ((ItemPlaceableBaseTileEntity) te).removeItem();
                if (!removeStack.isEmpty()) {
                    player.addItem(removeStack);
                }
            }
        }
    }

}
