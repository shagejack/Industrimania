package shagejack.industrimania.content.primalAge.block.woodenFaucet;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.foundation.utility.TileEntityUtils;
import shagejack.industrimania.registers.AllTileEntities;

import javax.annotation.Nullable;
import java.util.EnumMap;

public class WoodenFaucetBlock extends Block implements ITE<WoodenFaucetTileEntity> {
    public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;
    private static final EnumMap<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
            Direction.DOWN,  Shapes.join(box( 4, 10,  4, 12, 16, 12), box( 6, 10,  6, 10, 16, 10), BooleanOp.ONLY_FIRST),
            Direction.NORTH, Shapes.join(box( 4,  4, 10, 12, 10, 16), box( 6,  6, 10, 10, 10, 16), BooleanOp.ONLY_FIRST),
            Direction.SOUTH, Shapes.join(box( 4,  4,  0, 12, 10,  6), box( 6,  6,  0, 10, 10,  6), BooleanOp.ONLY_FIRST),
            Direction.WEST,  Shapes.join(box(10,  4,  4, 16, 10, 12), box(10,  6,  6, 16, 10, 10), BooleanOp.ONLY_FIRST),
            Direction.EAST,  Shapes.join(box( 0,  4,  4,  6, 10, 12), box( 0,  6,  6,  6, 10, 10), BooleanOp.ONLY_FIRST)));

    public WoodenFaucetBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.SOUTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction dir = context.getClickedFace();
        if (dir == Direction.UP) {
            dir = Direction.DOWN;
        }
        return this.defaultBlockState().setValue(FACING, dir);
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<WoodenFaucetTileEntity> getTileEntityClass() {
        return WoodenFaucetTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends WoodenFaucetTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends WoodenFaucetTileEntity>) AllTileEntities.wooden_faucet.get();
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);

        if (level.isClientSide())
            return;

        TileEntityUtils.get(getTileEntityClass(), level, pos).ifPresent(faucet -> faucet.neighborChanged(fromPos));

    }
}
