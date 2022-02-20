package shagejack.industrimania.content.primalAge.block.simpleCraftingTable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ForgeHooks;
import shagejack.industrimania.content.contraptions.blockBase.BlockDirectionalHorizontalBase;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBaseTileEntity;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.foundation.item.ItemHelper;
import shagejack.industrimania.registers.AllTileEntities;

public class SimpleCraftingTableBlock extends BlockDirectionalHorizontalBase implements ITE<SimpleCraftingTableTileEntity> {

    public SimpleCraftingTableBlock(Properties properties) {
        super(properties);
    }

    public boolean useShapeForLightOcclusion(BlockState p_52997_) {
        return true;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {

            Direction direction = state.getValue(FACING);
            double hitX = result.getLocation().x - result.getBlockPos().getX();
            double hitZ = result.getLocation().z - result.getBlockPos().getZ();
            ItemStack stack = player.getItemInHand(hand);

            if (level.getBlockEntity(pos) instanceof SimpleCraftingTableTileEntity) {
                SimpleCraftingTableTileEntity tile = (SimpleCraftingTableTileEntity) level.getBlockEntity(pos);
                if (result.getDirection() == Direction.UP) {
                    int slot = getSlotClicked(direction, hitX, hitZ);
                    if (slot != 10 && tile.craftingTick == -1) {
                        if (!stack.isEmpty() && tile.inventory.getStackInSlot(slot).isEmpty()) {
                            tile.inventory.setStackInSlot(slot, stack.split(1));
                            //RESET CRAFTING PROGRESSION
                            tile.resetProgression();
                        } else {
                            ItemStack stack2 = tile.inventory.getStackInSlot(slot);
                            if (!stack2.isEmpty()) {
                                if (!player.addItem(stack2))
                                    ForgeHooks.onPlayerTossEvent(player, stack2, false);
                                tile.inventory.setStackInSlot(slot, ItemStack.EMPTY);
                                //RESET CRAFTING PROGRESSION
                                tile.resetProgression();
                            }
                        }
                    }
                }
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void attack(BlockState state, Level level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof SimpleCraftingTableTileEntity tile) {
            if (player.getFoodData().getFoodLevel() > 0) {
                tile.hit();
                player.getFoodData().addExhaustion(1.0F);
            } else {
                if (level.getRandom().nextBoolean())
                    tile.hit();

                player.hurt(DamageSource.STARVE, 1.0F);
            }
        }
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean p_48717_) {
        if (oldState.hasBlockEntity() && oldState.getBlock() != newState.getBlock()) {
            withTileEntityDo(level, pos, te -> ItemHelper.dropContents(level, pos, te.inventory));
            level.removeBlockEntity(pos);
        }
    }

    @Override
    public Class<SimpleCraftingTableTileEntity> getTileEntityClass() {
        return SimpleCraftingTableTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends SimpleCraftingTableTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends SimpleCraftingTableTileEntity>) AllTileEntities.simple_crafting_table.get();
    }

    public int getSlotClicked(Direction direction, double hitX, double hitZ) {
        int slot = 10;
        if(hitX >= 0.4 && hitX <= 0.6 && hitZ >= 0.4 && hitZ <= 0.6) {
            slot = 4;
        }
        if(hitX >= 0.125 && hitX <= 0.33 && hitZ >= 0.125 && hitZ <= 0.33) {
            if(direction == Direction.NORTH)
                slot = 8;
            if(direction == Direction.EAST)
                slot = 2;
            if(direction == Direction.WEST)
                slot = 6;
            if(direction == Direction.SOUTH)
                slot = 0;
        }

        if(hitX >= 0.4 && hitX <= 0.6 && hitZ >= 0.125 && hitZ <= 0.33) {
            if(direction == Direction.NORTH)
                slot = 7;
            if(direction == Direction.EAST)
                slot = 5;
            if(direction == Direction.WEST)
                slot = 3;
            if(direction == Direction.SOUTH)
                slot = 1;
        }

        if(hitX >= 0.67 && hitX <= 0.875 && hitZ >= 0.125 && hitZ <= 0.33) {
            if(direction == Direction.NORTH)
                slot = 6;
            if(direction == Direction.EAST)
                slot = 8;
            if(direction == Direction.WEST)
                slot = 0;
            if(direction == Direction.SOUTH)
                slot = 2;
        }

        if(hitX >= 0.125 && hitX <= 0.33 && hitZ >= 0.4 && hitZ <= 0.6) {
            if(direction == Direction.NORTH)
                slot = 5;
            if(direction == Direction.EAST)
                slot = 1;
            if(direction == Direction.WEST)
                slot = 7;
            if(direction == Direction.SOUTH)
                slot = 3;
        }

        if(hitX >= 0.67 && hitX <= 0.875 && hitZ >= 0.4 && hitZ <= 0.6) {
            if(direction == Direction.NORTH)
                slot = 3;
            if(direction == Direction.EAST)
                slot = 7;
            if(direction == Direction.WEST)
                slot = 1;
            if(direction == Direction.SOUTH)
                slot = 5;
        }

        if(hitX >= 0.125 && hitX <= 0.33 && hitZ >= 0.67 && hitZ <= 0.875) {
            if(direction == Direction.NORTH)
                slot = 2;
            if(direction == Direction.EAST)
                slot = 0;
            if(direction == Direction.WEST)
                slot = 8;
            if(direction == Direction.SOUTH)
                slot = 6;
        }

        if(hitX >= 0.4 && hitX <= 0.6 && hitZ >= 0.67 && hitZ <= 0.875) {
            if(direction == Direction.NORTH)
                slot = 1;
            if(direction == Direction.EAST)
                slot = 3;
            if(direction == Direction.WEST)
                slot = 5;
            if(direction == Direction.SOUTH)
                slot = 7;
        }

        if(hitX >= 0.67 && hitX <= 0.875 && hitZ >= 0.67 && hitZ <= 0.875) {
            if(direction == Direction.NORTH)
                slot = 0;
            if(direction == Direction.EAST)
                slot = 6;
            if(direction == Direction.WEST)
                slot = 2;
            if(direction == Direction.SOUTH)
                slot = 8;
        }
        return slot;
    }

}
