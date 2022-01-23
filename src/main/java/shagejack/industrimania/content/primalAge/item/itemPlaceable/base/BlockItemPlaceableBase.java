package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.registers.AllTileEntities;

import java.util.Objects;

public class BlockItemPlaceableBase extends Block implements ITE<ItemPlaceableBaseTileEntity> {

    public BlockItemPlaceableBase(Properties properties) {
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
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState state, boolean p_48717_) {
        BlockEntity te = level.getBlockEntity(pos);
        if (te instanceof ItemPlaceableBaseTileEntity) {
            ((ItemPlaceableBaseTileEntity) te).onBreak(level);
        }
        super.onRemove(oldState, level, pos, state, p_48717_);
    }

    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.isEmpty() || player.isShiftKeyDown()) {
                BlockEntity te = level.getBlockEntity(blockPos);
                if (te instanceof ItemPlaceableBaseTileEntity) {
                    String name = ((ItemPlaceableBaseTileEntity) te).removeItem();
                    if (!name.isEmpty()) {
                        player.addItem(getItemStackFromRegistryName(name));
                    }
                }
            } else if (stack.getItem() instanceof ItemPlaceableBase) {
                BlockEntity te = level.getBlockEntity(blockPos);
                if (te instanceof ItemPlaceableBaseTileEntity) {
                    if (((ItemPlaceableBaseTileEntity) te).addItem(Objects.requireNonNull(stack.getItem().getRegistryName()).toString())) {
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

    public ItemStack getItemStackFromRegistryName(String name) {
        if (name != null && !name.isEmpty()) {
            String[] temp = name.split(":");

            //Normally, this case will never not happen
            if (temp.length > 2) {
                for (int i = 2; i < temp.length; i++) {
                    temp[1] += temp[i];
                }
            }

            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(temp[0], temp[1]));
            return new ItemStack(item);
        }
        return null;
    }

}
