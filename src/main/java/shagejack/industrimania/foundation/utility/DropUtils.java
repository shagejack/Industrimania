package shagejack.industrimania.foundation.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DropUtils {

    private DropUtils() {
        throw new IllegalStateException(this.getClass().toString() + "should not be instantiated as it's a utility class.");
    }

    public static void dropItemStack(Level level, BlockPos pos, ItemStack stack) {
        ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
    }

    public static void dropItemStackWithRandomOffset(Level level, BlockPos pos, ItemStack stack) {
        double d0 = (double)(level.random.nextFloat() * 0.7F) + (double)0.15F;
        double d1 = (double)(level.random.nextFloat() * 0.7F) + (double)0.060000002F + 0.6D;
        double d2 = (double)(level.random.nextFloat() * 0.7F) + (double)0.15F;
        ItemEntity itemEntity = new ItemEntity(level, pos.getX() + d0, pos.getY() + d1, pos.getZ() + d2, stack);
        itemEntity.setDefaultPickUpDelay();
        level.addFreshEntity(itemEntity);
    }


}
