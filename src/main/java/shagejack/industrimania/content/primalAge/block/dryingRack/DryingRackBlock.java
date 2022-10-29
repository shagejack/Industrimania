package shagejack.industrimania.content.primalAge.block.dryingRack;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
import shagejack.industrimania.content.contraptions.processing.ProcessingInventory;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.foundation.item.ItemHelper;
import shagejack.industrimania.registries.AllTileEntities;

public class DryingRackBlock extends Block implements ITE<DryingRackTileEntity> {

    private static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D);

    public DryingRackBlock(Properties properties) {
        super(properties);
    }

    public RenderShape getRenderShape(BlockState p_52986_) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        return SHAPE;
    }

    public boolean useShapeForLightOcclusion(BlockState p_52997_) {
        return true;
    }

    @Override
    public Class<DryingRackTileEntity> getTileEntityClass() {
        return DryingRackTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends DryingRackTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends DryingRackTileEntity>) AllTileEntities.drying_rack.get();
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

            withTileEntityDo(level, pos, dryingRack -> {
                ProcessingInventory inv = dryingRack.inventory;
                ItemStack stackInSlot = inv.getStackInSlot(0);
                if (!player.getItemInHand(handIn).isEmpty() && stackInSlot.isEmpty()) {
                    inv.setStackInSlot(0, player.getItemInHand(handIn).split(1));
                    dryingRack.setChanged();
                    dryingRack.sendData();
                } else if (!stackInSlot.isEmpty()) {
                    player.getInventory().placeItemBackInInventory(inv.getStackInSlot(0));
                    inv.setStackInSlot(0, ItemStack.EMPTY);
                    dryingRack.setChanged();
                    dryingRack.sendData();
                }
            });

        return InteractionResult.PASS;
    }

}
