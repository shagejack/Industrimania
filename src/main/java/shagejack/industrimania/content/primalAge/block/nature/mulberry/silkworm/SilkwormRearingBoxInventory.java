package shagejack.industrimania.content.primalAge.block.nature.mulberry.silkworm;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class SilkwormRearingBoxInventory extends ItemStackHandler {

    public SilkwormRearingBoxInventory() {
        super(16);
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return 1;
    }

    public void clear() {
        for (int i = 0; i < getSlots(); i++)
            setStackInSlot(i, ItemStack.EMPTY);
    }

    public boolean isEmpty() {
        for (int i = 0; i < getSlots(); i++)
            if (!getStackInSlot(i).isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
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

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return slot == 0 && isEmpty();
    }
}
