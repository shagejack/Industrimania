package shagejack.industrimania.content.dynamicLights;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface DynamicLightsItem {
    boolean isLightUp(ItemStack stack);

    void setLightUp(ItemStack stack, boolean light);

    BlockPos getPrevPos(ItemStack stack);

    void setPrevPos(ItemStack stack, BlockPos pos);

    boolean shouldDamage(Level level, @Nullable Player player, ItemStack stack);
}
