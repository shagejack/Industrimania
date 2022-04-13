package shagejack.industrimania.content.primalAge.block.woodenBarrel;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.foundation.utility.TileEntityUtils;
import shagejack.industrimania.registers.AllTileEntities;
import shagejack.industrimania.registers.block.AllBlocks;
import shagejack.industrimania.registers.item.AllItems;

import javax.annotation.ParametersAreNonnullByDefault;

public class WoodenBarrelBlock extends Block implements ITE<WoodenBarrelTileEntity> {

    public static final BooleanProperty SEALED = BooleanProperty.create("SEALED");

    private static final VoxelShape SHAPE = Block.box(1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D);

    public WoodenBarrelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(SEALED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SEALED);
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<WoodenBarrelTileEntity> getTileEntityClass() {
        return WoodenBarrelTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends WoodenBarrelTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends WoodenBarrelTileEntity>) AllTileEntities.wooden_barrel.get();
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        super.destroy(level, pos, state);

        if (level.isClientSide())
            return;

        TileEntityUtils.get(getTileEntityClass(), level, pos).ifPresent(barrel -> barrel.destroy(state.getValue(SEALED)));
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        super.use(state, level, pos, player, hand, result);

        if (state.getValue(SEALED) && player.isShiftKeyDown()) {
            level.setBlock(pos, state.setValue(SEALED, false), 3);
            player.addItem(new ItemStack(AllItems.woodenBarrelCover.get()));
            return InteractionResult.SUCCESS;
        } else {
            if (player.getItemInHand(hand).is(AllItems.woodenBarrelCover.get())) {
                player.setItemInHand(hand, ItemStack.EMPTY);
                level.setBlock(pos, state.setValue(SEALED, true), 3);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.FAIL;

    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);

        TileEntityUtils.get(getTileEntityClass(), level, pos).ifPresent(barrel -> barrel.initByItem(stack));
    }
}
