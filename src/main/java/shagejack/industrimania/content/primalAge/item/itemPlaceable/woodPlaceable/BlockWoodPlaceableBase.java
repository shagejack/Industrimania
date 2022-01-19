package shagejack.industrimania.content.primalAge.item.itemPlaceable.woodPlaceable;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.BlockItemPlaceableBase;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBase;
import shagejack.industrimania.content.primalAge.item.itemPlaceable.base.ItemPlaceableBaseTileEntity;
import shagejack.industrimania.foundation.block.ITE;
import shagejack.industrimania.registers.AllTileEntities;

import java.util.List;
import java.util.Objects;

public class BlockWoodPlaceableBase extends Block implements ITE<WoodPlaceableTileEntity> {

    private final List<Item> logItems = Lists.newArrayList(
            Items.ACACIA_LOG,
            Items.BIRCH_LOG,
            Items.DARK_OAK_LOG,
            Items.JUNGLE_LOG,
            Items.OAK_LOG,
            Items.SPRUCE_LOG
    );

    public BlockWoodPlaceableBase(Properties properties) {
        super(properties);
    }

    @Override
    public Class<WoodPlaceableTileEntity> getTileEntityClass() {
        return WoodPlaceableTileEntity.class;
    }

    @Override
    public BlockEntityType<? extends WoodPlaceableTileEntity> getTileEntityType() {
        return (BlockEntityType<? extends WoodPlaceableTileEntity>) AllTileEntities.wood_placeable.get();
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState state, boolean p_48717_) {
        BlockEntity te = level.getBlockEntity(pos);
        if (te instanceof WoodPlaceableTileEntity) {
            ((WoodPlaceableTileEntity) te).onBreak(level);
        }
        super.onRemove(oldState, level, pos, state, p_48717_);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult result) {
        if (!level.isClientSide) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.isEmpty() || player.isShiftKeyDown()) {
                BlockEntity te = level.getBlockEntity(blockPos);
                if (te instanceof WoodPlaceableTileEntity) {
                    String name = ((WoodPlaceableTileEntity) te).removeItem();
                    if (!name.isEmpty()) {
                        player.addItem(getItemStackFromRegistryName(name));
                    }
                }
            } else if (stack.getItem() instanceof ItemWoodPlaceable) {
                BlockEntity te = level.getBlockEntity(blockPos);
                if (te instanceof WoodPlaceableTileEntity) {
                    if (((WoodPlaceableTileEntity) te).addItem(Objects.requireNonNull(stack.getItem().getRegistryName()).toString())) {
                        stack.shrink(1);
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
