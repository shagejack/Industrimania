package shagejack.industrimania.content.primalAge.item.itemPlaceable.base;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import shagejack.industrimania.content.contraptions.processing.ProcessingInventory;
import shagejack.industrimania.foundation.item.SmartInventory;

import java.util.function.Consumer;
import java.util.stream.IntStream;

public class ItemPlaceableInventory extends SmartInventory {

    public ItemPlaceableInventory(int size, ItemPlaceableBaseTileEntity te) {
        super(size, te);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    public boolean isEmpty() {
        for (int i = 0; i < getSlots(); i++)
            if (!getStackInSlot(i).isEmpty())
                return false;
        return true;
    }

    public int getTotalItemAmount() {
        int amount = 0;
        for (int i = 0; i < getSlots(); i++)
            if (!getStackInSlot(i).isEmpty())
                amount++;
        return amount;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
    }

    public void dropInventory(Level level, BlockPos pos) {
        IntStream.range(0, getSlots()).forEach(index -> dropItem(level, pos, getStackInSlot(index)));
        clear();
    }

    public void clear() {
        for (int i = 0; i < getSlots(); i++)
            setStackInSlot(i, ItemStack.EMPTY);
    }

    public void dropItem(Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide && !stack.isEmpty() && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !level.restoringBlockSnapshots) {
            float f = EntityType.ITEM.getHeight() / 2.0F;
            double d0 = (double)((float)pos.getX() + 0.5F) + Mth.nextDouble(level.getRandom(), -0.25D, 0.25D);
            double d1 = (double)((float)pos.getY() + 0.5F) + Mth.nextDouble(level.getRandom(), -0.25D, 0.25D) - (double)f;
            double d2 = (double)((float)pos.getZ() + 0.5F) + Mth.nextDouble(level.getRandom(), -0.25D, 0.25D);
            ItemEntity itemEntity = new ItemEntity(level, d0, d1, d2, stack);
            itemEntity.setDefaultPickUpDelay();
            level.addFreshEntity(itemEntity);
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof ItemPlaceableBase;
    }


}
