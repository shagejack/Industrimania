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

import java.util.function.Consumer;

public class ItemPlaceableInventory extends ItemStackHandler {

    public float remainingTime;
    public float recipeDuration;
    public boolean appliedRecipe;

    public ItemPlaceableInventory(int size) {
        super(size);
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

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        nbt.putFloat("ProcessingTime", remainingTime);
        nbt.putFloat("RecipeTime", recipeDuration);
        nbt.putBoolean("AppliedRecipe", appliedRecipe);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        remainingTime = nbt.getFloat("ProcessingTime");
        recipeDuration = nbt.getFloat("RecipeTime");
        appliedRecipe = nbt.getBoolean("AppliedRecipe");
        super.deserializeNBT(nbt);
        if(isEmpty())
            appliedRecipe = false;
    }

    public void dropInventory(Level level, BlockPos pos) {
        stacks.forEach(stack -> dropItem(level, pos, stack));
        clear();
    }

    public void clear() {
        for (int i = 0; i < getSlots(); i++)
            setStackInSlot(i, ItemStack.EMPTY);
        remainingTime = 0;
        recipeDuration = 0;
        appliedRecipe = false;
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
