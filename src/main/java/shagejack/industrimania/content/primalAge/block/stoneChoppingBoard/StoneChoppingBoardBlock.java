package shagejack.industrimania.content.primalAge.block.stoneChoppingBoard;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.foundation.item.ItemHelper;
import shagejack.industrimania.registers.AllTileEntities;

public class StoneChoppingBoardBlock extends Block implements ITE<StoneChoppingBoardTileEntity> {

    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);

    public StoneChoppingBoardBlock(Properties properties) {
        super(properties);
    }

    public RenderShape getRenderShape(BlockState p_52986_) {
        return RenderShape.MODEL;
    }

    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        return SHAPE;
    }

    public boolean useShapeForLightOcclusion(BlockState p_52997_) {
        return true;
    }

    @Override
    public Class<StoneChoppingBoardTileEntity> getTileEntityClass() {
        return StoneChoppingBoardTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends StoneChoppingBoardTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends StoneChoppingBoardTileEntity>) AllTileEntities.stone_chopping_board.get();
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean p_48717_) {
        if (oldState.hasBlockEntity() && oldState.getBlock() != newState.getBlock()) {
            withTileEntityDo(level, pos, te -> ItemHelper.dropContents(level, pos, te.inventory));
            level.removeBlockEntity(pos);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {

        if (level.isClientSide)
            return InteractionResult.SUCCESS;

        withTileEntityDo(level, pos, choppingBoard -> {
            StoneChoppingBoardInventory inv = choppingBoard.inventory;
            ItemStack stackInSlot = inv.getStackInSlot(0);
            if (!player.getItemInHand(handIn).isEmpty() && stackInSlot.isEmpty()) {
                inv.setStackInSlot(0, player.getItemInHand(handIn).split(1));
                choppingBoard.setChanged();
                choppingBoard.sendData();
            } else if (!stackInSlot.isEmpty()) {
                player.getInventory().placeItemBackInInventory(inv.getStackInSlot(0));
                inv.setStackInSlot(0, ItemStack.EMPTY);
                choppingBoard.setChanged();
                choppingBoard.sendData();
            }
        });

        return InteractionResult.PASS;
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof StoneChoppingBoardTileEntity tile) {
            if (player.getFoodData().getFoodLevel() > 0) {
                tile.hit(player.getItemInHand(InteractionHand.MAIN_HAND));
                player.getFoodData().addExhaustion(1.0F);
            } else {
                if (level.getRandom().nextBoolean())
                    tile.hit(player.getItemInHand(InteractionHand.MAIN_HAND));

                player.hurt(DamageSource.STARVE, 1.0F);
            }
        }
    }

}

