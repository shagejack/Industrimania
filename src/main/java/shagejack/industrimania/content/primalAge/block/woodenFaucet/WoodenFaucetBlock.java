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
import shagejack.industrimania.foundation.utility.VoxelShapeUtils;
import shagejack.industrimania.registers.AllTileEntities;

import javax.annotation.Nullable;
import java.util.EnumMap;

public class WoodenFaucetBlock extends Block implements ITE<WoodenFaucetTileEntity> {
    public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;

    public static final VoxelShape SHAPE_DOWN = VoxelShapeUtils.joinAllShape(BooleanOp.OR,
            Shapes.box(0.25, 0.9375, 0.25, 0.75, 1, 0.75),
            Shapes.box(0.375, 0.8125, 0.25, 0.75, 0.9375, 0.375),
            Shapes.box(0.25, 0.8125, 0.25, 0.375, 0.9375, 0.625),
            Shapes.box(0.625, 0.8125, 0.375, 0.75, 0.9375, 0.75),
            Shapes.box(0.25, 0.8125, 0.625, 0.625, 0.9375, 0.75)
            );

    public static final VoxelShape SHAPE_SIDE = VoxelShapeUtils.joinAllShape(BooleanOp.OR,
            Shapes.box(0.25, 0.0625, 0.6875, 0.3125, 0.5, 1),
            Shapes.box(0.6875, 0.125, 0.6875, 0.75, 0.5625, 1),
            Shapes.box(0.25, 0.5, 0.6875, 0.6875, 0.5625, 1),
            Shapes.box(0.3125, 0.0625, 0.6875, 0.75, 0.125, 1)
    );


    private static final EnumMap<Direction, VoxelShape> SHAPES = Maps.newEnumMap(ImmutableMap.of(
            Direction.DOWN, SHAPE_DOWN,
            Direction.NORTH, SHAPE_SIDE,
            Direction.SOUTH, VoxelShapeUtils.rotateShape(Direction.NORTH, Direction.SOUTH, SHAPE_SIDE),
            Direction.WEST,  VoxelShapeUtils.rotateShape(Direction.NORTH, Direction.WEST, SHAPE_SIDE),
            Direction.EAST, VoxelShapeUtils.rotateShape(Direction.NORTH, Direction.EAST, SHAPE_SIDE)
            )
    );

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
        return SHAPES.getOrDefault(state.getValue(FACING), Shapes.block());
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
