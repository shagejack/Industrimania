package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.foundation.item.ItemHelper;
import shagejack.industrimania.registers.AllTileEntities;

public class ItemPlaceableBaseBlock extends Block implements ITE<ItemPlaceableBaseTileEntity> {

    public ItemPlaceableBaseBlock(Properties properties) {
        super(properties);
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
