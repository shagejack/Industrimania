package shagejack.industrimania.content.primalAge.block.mixingBasin;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.ItemHandlerHelper;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.registries.AllTileEntities;

public class MixingBasinBlock extends Block implements ITE<MixingBasinTileEntity> {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public MixingBasinBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Class<MixingBasinTileEntity> getTileEntityClass() {
        return MixingBasinTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends MixingBasinTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends MixingBasinTileEntity>) AllTileEntities.mixing_basin.get();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {

        MixingBasinTileEntity te = getTileEntity(level, pos);
        ItemStack handStack = player.getItemInHand(hand);

        if (te == null)
            return InteractionResult.FAIL;

        if (te.outputInventory.isEmpty()) {
            if (player.isShiftKeyDown()) {
                ItemStack stack = ItemStack.EMPTY;

                for (int i = 8; i >= 0; i--) {
                    if (!te.getInputInventory().extractItem(i, true).isEmpty()) {
                        stack = te.getInputInventory().extractItem(i, false);
                        break;
                    }
                }

                if (stack.isEmpty())
                    return InteractionResult.FAIL;

                if (!stack.isEmpty())
                    ItemHandlerHelper.giveItemToPlayer(player, stack, EquipmentSlot.MAINHAND.getIndex());

                te.setStirCount(0);
                te.setMixing(false);
                if (level.isClientSide())
                    level.playSound((Player) null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 2F);
                return InteractionResult.SUCCESS;
            } else {

                if (handStack.isEmpty()) {
                    // do stir
                    if(!te.getMixing()) {
                        te.setMixing(true);
                        te.setStirCount(te.getStirCount() + 1);
                        if(level.isClientSide()) level.playSound((Player)null, pos, SoundEvents.PLAYER_SWIM, SoundSource.BLOCKS, 0.5F, 0.25F);
                        return InteractionResult.SUCCESS;
                    }

                } else {
                    LazyOptional<IFluidHandlerItem> handler = FluidUtil.getFluidHandler(handStack);

                    if (handler.isPresent()) {
                        if (FluidUtil.interactWithFluidHandler(player, hand, te.inputTank)) {
                            return InteractionResult.CONSUME_PARTIAL;
                        }
                    } else {
                        if (ItemHandlerHelper.insertItem(te.getInputInventory(), handStack, true).isEmpty()) {
                            ItemHandlerHelper.insertItem(te.getInputInventory(), handStack, false);
                        }
                    }

                }
            }
        } else {
            ItemStack stack = ItemStack.EMPTY;

            for (int i = 8; i >= 0; i--) {
                if (!te.getOutputInventory().extractItem(i, true).isEmpty()) {
                    stack = te.getOutputInventory().extractItem(i, false);
                    break;
                }
            }

            if (stack.isEmpty())
                return InteractionResult.FAIL;

            if (!stack.isEmpty())
                ItemHandlerHelper.giveItemToPlayer(player, stack, EquipmentSlot.MAINHAND.getIndex());

            te.setStirCount(0);
            te.setMixing(false);
            if (level.isClientSide())
                level.playSound((Player) null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5F, 2F);
            return InteractionResult.SUCCESS;
        }

        return super.use(state, level, pos, player, hand, result);
    }

}
